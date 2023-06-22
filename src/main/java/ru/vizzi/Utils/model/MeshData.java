package ru.vizzi.Utils.model;

public class MeshData {

    private static final int DIMENSIONS = 3;

    private String name;
    private float[] vertices;
    private float[] textureCoords;
    private float[] normals;
    private int[] indices;
    private int[] jointIds;
    private float[] vertexWeights;
    private float[] tangents;

    MeshData(String name) {
        this.name = name;
    }

    public MeshData(float[] vertices, float[] textureCoords, float[] normals, int[] indices, int[] jointIds, float[] vertexWeights, float[] tangents) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.jointIds = jointIds;
        this.vertexWeights = vertexWeights;
        this.tangents = tangents;
    }

    void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

    void setTextureCoords(float[] textureCoords) {
        this.textureCoords = textureCoords;
    }

    void setNormals(float[] normals) {
        this.normals = normals;
    }

    public void setIndices(int[] indices) {
        this.indices = indices;
    }

    public void setJointIds(int[] jointIds) {
        this.jointIds = jointIds;
    }

    public void setVertexWeights(float[] vertexWeights) {
        this.vertexWeights = vertexWeights;
    }

    void setTangents(float[] tangents) {
        this.tangents = tangents;
    }

    public int[] getJointIds() {
        return jointIds;
    }

    public float[] getVertexWeights() {
        return vertexWeights;
    }

    float[] getVertices() {
        return vertices;
    }

    float[] getTangents() {
        return tangents;
    }

    float[] getTextureCoords() {
        return textureCoords;
    }

    float[] getNormals() {
        return normals;
    }

    public int[] getIndices() {
        return indices;
    }

    public int getVertexCount() {
        return vertices.length / DIMENSIONS;
    }

    public void clear() {
        vertices = null;
        textureCoords = null;
        normals = null;
        indices = null;
        jointIds = null;
        vertexWeights = null;
        tangents = null;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
