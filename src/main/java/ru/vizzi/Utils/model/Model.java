package ru.vizzi.Utils.model;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Vector3f;
import ru.vizzi.Utils.resouces.xlv.AbstractResource;
import ru.vizzi.Utils.resouces.xlv.ResourceLoadingState;
import ru.vizzi.Utils.resouces.xlv.ResourceManager;
import ru.vizzi.Utils.shader.pbr.IPBR;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Model extends AbstractResource {

    protected List<Vao> vaos = new ArrayList<>();
    private Map<String, Vao> partByName = new HashMap<>();
    private List<MeshData> meshData;
    @Getter private final String path;

    @Getter @Setter private ModelBox modelBox;
    @Setter @Getter private Vector3f min, max;
    @Getter private float invScale;

    protected static final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);

    public Model(ResourceLocation resourceLocation) {
        super(resourceLocation);
        this.path = resourceLocation.toString();
    }

    protected void setMatrix(int location, FloatBuffer matrix) {
        GL20.glUniformMatrix4(location, false, matrix);
    }


    public void renderPart(String name, IPBR shader) {
        resetLifeTime();
        if(isLoaded()) {
            Vao vao = partByName.get(name);
            if (vao != null) {
                shader.setViewMatrix();
                vao.bindAttribs();
                GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
                vao.unbindAttribs();
            }
        } else {
            ResourceManager.loadResource(this);
        }
    }


    public void render(IPBR shader) {
        resetLifeTime();
        if(isLoaded()) {
            shader.setViewMatrix();
            for (Vao vao : vaos) {
                vao.bindAttribs();
                GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
                vao.unbindAttribs();
            }
        } else {
            ResourceManager.loadResource(this);
        }
    }




    public void renderPart(String name) {
        resetLifeTime();
        if(isLoaded()) {
            Vao vao = partByName.get(name);
            if (vao != null) {
                //setViewMatrix();
                vao.bindAttribs();
                GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
                vao.unbindAttribs();
            }
        } else {
            ResourceManager.loadResource(this);
        }
    }

    public void render() {
        resetLifeTime();
        if(isLoaded()) {
            for (Vao vao : vaos) {
                //setViewMatrix();
                vao.bindAttribs();
                GL11.glDrawElements(GL11.GL_TRIANGLES, vao.getIndexCount(), GL11.GL_UNSIGNED_INT, 0);
                vao.unbindAttribs();
            }
        } else {
            ResourceManager.loadResource(this);
        }
    }

    void addVAO(String name, Vao vao) {
        this.vaos.add(vao);
        this.partByName.put(name, vao);
    }

    @Override
    public void loadFromFile() {
        meshData = ModelLoader.readModelMeshes(this);
    }

    @Override
    public void loadToMemory() {
        ModelLoader.loadModelMeshes(this, meshData);
    }

    @Override
    public void unload() {
        if(meshData != null) {
            meshData.clear();
        }

        if(vaos != null) {
            for (Vao vao : vaos) {
                vao.clear();
            }

            vaos.clear();
        }
    }

    @Override
    public void setFrom(AbstractResource resource) {
        Model that = (Model) resource;
        vaos = that.vaos;
        partByName = that.partByName;
        modelBox = that.modelBox;
        min = that.min;
        max = that.max;
    }

    ResourceLocation getLoc() {
        return resourceLocation;
    }

    public AxisAlignedBB getRotatedModelBox(Vector3f rot, Vector3f scale) {
        return this.modelBox.getRotatedModelBox(rot, scale);
    }

    void createModelBox() {
        this.modelBox = new ModelBox(min, max, path);
    }

    public void calculateInventoryScale() {
        float max = Math.max(Math.max(Math.max(Math.abs(min.x), this.max.x), Math.max(Math.abs(min.y), this.max.y)), Math.max(Math.abs(min.z), this.max.z));
        invScale = 1.0f / max;
    }

    public boolean isLoaded() {
        return loadingState == ResourceLoadingState.DONE;
    }
}
