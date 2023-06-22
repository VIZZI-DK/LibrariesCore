package ru.vizzi.Utils.shader.pbr;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import ru.vizzi.Utils.shader.GameSettings;

import java.nio.FloatBuffer;

public class ForwardPBRPointShader extends AbstractPBRShader {
    private int loc_useTexture;
    private int loc_useNormalMapping, loc_useSpecularMapping;
    private int loc_useGlossMapping;
    private int loc_pointLightPos, loc_pointLightColor, loc_pointLightAttenuation;
    private int loc_modelView;

   // private PointLight currentLight;

    private int loc_inColor;
    private int loc_projMat;
    private int loc_viewMat;

    public ForwardPBRPointShader() {
        super(new ResourceLocation("shaders", "forward_point.vs"), new ResourceLocation("shaders", "forward_pbr_point.fs"));
    }

    @Override
    public void bindAttribLocations() {

    }

    @Override
    protected void getAllUniformLocations() {
        loc_useTexture = getUniformLocation("useTexture");

        loc_useNormalMapping = getUniformLocation("useNormalMapping");
        loc_useSpecularMapping = getUniformLocation("useSpecularMapping");
        loc_useGlossMapping = getUniformLocation("useGlossMapping");

        loc_pointLightColor = getUniformLocation("pointLights[0].color");
        loc_pointLightPos = getUniformLocation("pointLights[0].position");
        loc_pointLightAttenuation = getUniformLocation("pointLights[0].attenuation");

        loc_modelView = getUniformLocation("modelView");

        loc_inColor = getUniformLocation("in_color");
        loc_projMat = getUniformLocation("projMat");
        loc_viewMat = getUniformLocation("viewMat");
    }

    @Override
    protected void init() {
        setInt(getUniformLocation("diffuse"), 0);
        setInt(getUniformLocation("normalMap"), 2);
        setInt(getUniformLocation("specularMap"), 3);
        setInt(getUniformLocation("glossMap"), 5);
        setColor(1f, 1f, 1f, 1f);
    }

    @Override
    public void setLightMapping(boolean value) {

    }

    @Override
    public void setUseTexture(boolean value) {
        if(prevUseTexture != value) {
            setBoolean(loc_useTexture, value);
            prevUseTexture = value;
        }
    }

    @Override
    public void setNormalMapping(boolean value) {
        if(GameSettings.normalMapping) {
            if(prevNormalMapping != value) {
                setBoolean(loc_useNormalMapping, value);
                prevNormalMapping = value;
            }
        }
    }

    @Override
    public void setSpecularMapping(boolean value) {
        if(GameSettings.specularMapping) {
            if(prevSpecularMapping != value) {
                setBoolean(loc_useSpecularMapping, value);
                prevSpecularMapping = value;
            }
        }
    }

    @Override
    public void setEmissionMapping(boolean value) {

    }

    @Override
    public void setEmissionPower(float value) {

    }

    @Override
    public void setGlossMapping(boolean value) {
        if(GameSettings.glossMapping) {
            if(prevGlossMapping != value) {
                setBoolean(loc_useGlossMapping, value);
                prevGlossMapping = value;
            }
        }
    }

    private final Vector3f lightPos = new Vector3f();
    private float prevAttenuation;

//    public void setPointLight(PointLight light) {
//        setLightPos(light.pos.x, light.pos.y, light.pos.z);
//        setLightColor(light.color.x, light.color.y, light.color.z);
//        if(prevAttenuation != light.power) {
//            setFloat(loc_pointLightAttenuation, light.power);
//            prevAttenuation = light.power;
//        }
//
//        currentLight = light;
//    }

    public void setModelView() {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) matrix.position(0));
        this.setMatrix(loc_modelView, matrix);
    }

//    public PointLight getCurrentLight() {
//        return currentLight;
//    }

    @Override
    public void setLightColor(float r, float g, float b) {
        if(prevDirColor.x != r || prevDirColor.y != g || prevDirColor.z != b) {
            setVector(loc_pointLightColor, r, g, b);
            prevDirColor.x = r;
            prevDirColor.y = g;
            prevDirColor.z = b;
        }
    }

    @Override
    public void setLightPos(float x, float y, float z) {
        float newX = x - (float) TileEntityRendererDispatcher.staticPlayerX;
        float newY = y - (float) TileEntityRendererDispatcher.staticPlayerY;
        float newZ = z - (float) TileEntityRendererDispatcher.staticPlayerZ;
        if(prevDir.x != newX || prevDir.y != newY || prevDir.z != newZ) {
            setVector(loc_pointLightPos, newX, newY, newZ);
            prevDir.x = x;
            prevDir.y = y;
            prevDir.z = z;
        }
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        if(prevColor.x != r || prevColor.y != g || prevColor.z != b || prevColor.w != a) {
            setVector(loc_inColor, r, g, b, a);
            prevColor.x = r;
            prevColor.y = g;
            prevColor.z = b;
            prevColor.w = a;
        }
    }

    public void setProjectionMatrix() {
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, (FloatBuffer) matrix.position(0));
        this.setMatrix(loc_projMat, matrix);
    }

    @Override
    public void setViewMatrix() {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) matrix.position(0));
        this.setMatrix(loc_viewMat, matrix);
    }

    @Override
    public void setLightMapCoords(float x, float y) {

    }

    @Override
    public void useLighting(boolean value) {
    }
}
