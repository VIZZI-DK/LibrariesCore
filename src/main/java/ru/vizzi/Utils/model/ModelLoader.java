package ru.vizzi.Utils.model;


import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import ru.vizzi.Utils.config.Flex;
import ru.vizzi.Utils.resouces.CoreAPI;;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelLoader {

    private static final List<Model> MODELS = new ArrayList<>();

    public static void addModel(Model model) {
        MODELS.add(model);
    }

    public static void loadModels() {
        for(Model m : MODELS) {
            loadModel(m);
        }
    }

    public static void loadModel(Model model) {
        List<MeshData> meshData = readModelMeshes(model);
        loadModelMeshes(model, meshData);
    }

    public static List<MeshData> readModelMeshes(Model model) {
        InputStream inputStream = CoreAPI.getInputStreamFromZip(model.getLoc());
        if(inputStream != null) {
            try {
                return loadModel(new BufferedReader(new InputStreamReader(inputStream)), model);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ошибка загрузки модели " + model.getPath());
            }
        }
        return null;
    }

    public static void loadModelMeshes(Model model, List<MeshData> meshes) {
        createVAOs(model, meshes);
        model.createModelBox();
    }

    public static boolean isLoaded(ResourceLocation resourceLocation) {
        Model model = getModel(resourceLocation);
        return model != null && model.isLoaded();
    }

    public static boolean deleteModel(ResourceLocation resourceLocation) {
        return Flex.removeIf(MODELS, model -> model.getLoc().equals(resourceLocation), model -> model.vaos.forEach(Vao::clear));
    }

    @Nullable
    public static Model getModel(ResourceLocation resourceLocation) {
        synchronized (MODELS) {
            return Flex.getCollectionElement(MODELS, model -> model.getLoc().equals(resourceLocation));
        }
    }

    private static void createVAOs(Model model, List<MeshData> meshes) {
        for (MeshData mesh : meshes) {
            Vao vao = createVao(mesh);
            mesh.clear();
            model.addVAO(mesh.getName(), vao);
        }
    }

    public static int loc_in_position = 4;
    public static int loc_in_textureCoords = 5;
    public static int loc_in_normal = 6;
    public static int loc_in_tangent = 11;


    private static Vao createVao(MeshData data) {
        Vao vao = Vao.create();
        vao.bind();
        vao.createIndexBuffer(data.getIndices());
        vao.createAttribute(loc_in_position, data.getVertices(), 3);
        vao.createAttribute(loc_in_textureCoords, data.getTextureCoords(), 2);
        vao.createAttribute(loc_in_normal, data.getNormals(), 3);
        vao.createAttribute(loc_in_tangent, data.getTangents(), 3);
        vao.unbind();
        return vao;
    }

    private static List<MeshData> loadModel(BufferedReader reader, Model model) {
        List<MeshData> meshes = new ArrayList<>();
        String line;
        String currentGroup = "default";

        List<VertexNM> vertices = new ArrayList<>();
        List<Vector2f> textureCoordinates = new ArrayList<>();
        List<Vector3f> normals = new ArrayList<>();
        Map<String, List<Integer>> indices = new HashMap<>();
        Map<String, List<String[]>> facesLines = new HashMap<>();
        List<String> groups =  new ArrayList<>();
        Vector3f min = null, max = null;

        List<Integer> indicies1 = new ArrayList<>();
        indices.put(currentGroup, indicies1);
        List<String[]> lines = new ArrayList<>();
        facesLines.put(currentGroup, lines);
        groups.add(currentGroup);

        try {
            while ((line = reader.readLine()) != null) {
                line = line.trim().replaceAll(" +" , " ");

                if (line.length() > 0) {
                    if (line.startsWith("v ")) {
                        String[] currentLine = line.split(" ");
                        Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                        if(min == null) {
                            min = new Vector3f(vertex);
                            max = new Vector3f(vertex);
                        } else {
                            if(vertex.x < min.x) min.x = vertex.x;
                            else if(vertex.x > max.x) max.x = vertex.x;
                            if(vertex.y < min.y) min.y = vertex.y;
                            else if(vertex.y > max.y) max.y = vertex.y;
                            if(vertex.z < min.z) min.z = vertex.z;
                            else if(vertex.z > max.z) max.z = vertex.z;
                        }
                        VertexNM newVertex = new VertexNM(vertices.size(), vertex);
                        vertices.add(newVertex);
                    } else if (line.startsWith("vt")) {
                        String[] currentLine = line.split(" ");
                        Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                        textureCoordinates.add(texture);
                    } else if (line.startsWith("vn")) {
                        String[] currentLine = line.split(" ");
                        Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                        normals.add(normal);
                    } else if (line.startsWith("f ")) {
                        String[] currentLine = line.split(" ");
                        facesLines.get(currentGroup).add(currentLine);
                    } else if (line.startsWith("g ") || line.startsWith("o ")) {
                        String group = line.substring(line.indexOf(" ") + 1);

                        currentGroup = group;
                        if(!indices.containsKey(currentGroup)) {
                            indicies1 = new ArrayList<>();
                            indices.put(currentGroup, indicies1);
                            lines = new ArrayList<>();
                            facesLines.put(currentGroup, lines);
                            groups.add(group);
                        }
                    }
                }
            }

            model.setMax(max);
            model.setMin(min);
            model.calculateInventoryScale();

            for(String group : groups) {
                lines = facesLines.get(group);
                for(String[] currentLine : lines) {
                    String[] vertex1 = currentLine[1].split("/");
                    String[] vertex2 = currentLine[2].split("/");
                    String[] vertex3 = currentLine[3].split("/");
                    List<Integer> indicesList = indices.get(group);
                    VertexNM v0 = processVertex(vertex1, vertices, indicesList);
                    VertexNM v1 = processVertex(vertex2, vertices, indicesList);
                    VertexNM v2 = processVertex(vertex3, vertices, indicesList);
                    calculateTangents(v0, v1, v2, textureCoordinates);
                }
            }

            removeUnusedVertices(vertices);
            reader.close();

            for(String group : groups) {
                MeshData currentMeshData = new MeshData(group);
                convertDataToArrays(currentMeshData, vertices, textureCoordinates, normals, indices.get(group));
                meshes.add(currentMeshData);
            }

            vertices.clear();
            textureCoordinates.clear();
            normals.clear();
            indices.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return meshes;
    }

    private static void convertDataToArrays(MeshData mesh, List<VertexNM> vertices, List<Vector2f> textures, List<Vector3f> normals, List<Integer> indices) {
        int[] indicesArray = new int[indices.size()];

        float[] verticesArray = new float[indices.size() * 3];
        float[] texturesArray = new float[indices.size() * 2];
        float[] normalsArray = new float[indices.size() * 3];
        float[] tangentsArray = new float[indices.size() * 3];

        for(int i=0;i<indices.size();i++) {
            VertexNM currentVertex = vertices.get(indices.get(i));
            Vector3f position = currentVertex.getPosition();
            Vector2f textureCoord = textures.get(currentVertex.getTextureIndex());
            Vector3f normalVector = normals.get(currentVertex.getNormalIndex());
            Vector3f tangent = currentVertex.getAverageTangent();
            indicesArray[i] = i;
            verticesArray[i * 3] = position.x;
            verticesArray[i * 3 + 1] = position.y;
            verticesArray[i * 3 + 2] = position.z;
            texturesArray[i * 2] = textureCoord.x;
            texturesArray[i * 2 + 1] = 1 - textureCoord.y;
            normalsArray[i * 3] = normalVector.x;
            normalsArray[i * 3 + 1] = normalVector.y;
            normalsArray[i * 3 + 2] = normalVector.z;
            tangentsArray[i * 3] = tangent.x;
            tangentsArray[i * 3 + 1] = tangent.y;
            tangentsArray[i * 3 + 2] = tangent.z;
        }

        mesh.setIndices(indicesArray);
        mesh.setNormals(normalsArray);
        mesh.setVertices(verticesArray);
        mesh.setTangents(tangentsArray);
        mesh.setTextureCoords(texturesArray);
        indices.clear();
    }

    private static VertexNM processVertex(String[] vertex, List<VertexNM> vertices, List<Integer> indices) {
        int index = Integer.parseInt(vertex[0]) - 1;
        VertexNM currentVertex = vertices.get(index);
        int textureIndex = Integer.parseInt(vertex[1]) - 1;
        int normalIndex = Integer.parseInt(vertex[2]) - 1;
        if (!currentVertex.isSet()) {
            currentVertex.setTextureIndex(textureIndex);
            currentVertex.setNormalIndex(normalIndex);
            indices.add(index);
            return currentVertex;
        } else {
            return dealWithAlreadyProcessedVertex(currentVertex, textureIndex, normalIndex, indices, vertices);
        }
    }

    private static VertexNM dealWithAlreadyProcessedVertex(VertexNM previousVertex, int newTextureIndex, int newNormalIndex, List<Integer> indices, List<VertexNM> vertices) {
        if (previousVertex.hasSameTextureAndNormal(newTextureIndex, newNormalIndex)) {
            indices.add(previousVertex.getIndex());
            return previousVertex;
        } else {
            VertexNM anotherVertex = previousVertex.getDuplicateVertex();
            if (anotherVertex != null) {
                return dealWithAlreadyProcessedVertex(anotherVertex, newTextureIndex, newNormalIndex, indices, vertices);
            } else {
                VertexNM duplicateVertex = new VertexNM(vertices.size(), previousVertex.getPosition());
                duplicateVertex.setTextureIndex(newTextureIndex);
                duplicateVertex.setNormalIndex(newNormalIndex);
                previousVertex.setDuplicateVertex(duplicateVertex);
                vertices.add(duplicateVertex);
                indices.add(duplicateVertex.getIndex());
                return duplicateVertex;
            }
        }
    }

    private static void removeUnusedVertices(List<VertexNM> vertices) {
        for (VertexNM vertex : vertices) {
            vertex.averageTangents();
            if (!vertex.isSet()) {
                vertex.setTextureIndex(0);
                vertex.setNormalIndex(0);
            }
        }
    }

    private static void calculateTangents(VertexNM v0, VertexNM v1, VertexNM v2, List<Vector2f> textures) {
        Vector3f edge1 = Vector3f.sub(v1.getPosition(), v0.getPosition(), null);
        Vector3f edge2  = Vector3f.sub(v2.getPosition(), v0.getPosition(), null);
        Vector2f uv0 = textures.get(v0.getTextureIndex());
        Vector2f uv1 = textures.get(v1.getTextureIndex());
        Vector2f uv2 = textures.get(v2.getTextureIndex());
        Vector2f deltaUv1 = Vector2f.sub(uv1, uv0, null);
        Vector2f deltaUv2 = Vector2f.sub(uv2, uv0, null);

        float f = 1.0f / (deltaUv1.x * deltaUv2.y - deltaUv1.y * deltaUv2.x);

        Vector3f tangent = new Vector3f(f * (deltaUv2.y * edge1.x - deltaUv1.y * edge2.x), f * (deltaUv2.y * edge1.y - deltaUv1.y * edge2.y), f * (deltaUv2.y * edge1.z - deltaUv1.y * edge2.z));
        if(tangent.length() > 0) tangent.normalise();
        v0.addTangent(tangent);
        v1.addTangent(tangent);
        v2.addTangent(tangent);
    }
}

