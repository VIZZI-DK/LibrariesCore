package ru.vizzi.Utils.shader;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.vizzi.Utils.shader.pbr.IPBR;

import java.nio.FloatBuffer;

public class GBufferShader extends ShaderProgram implements IPBR {

    private int loc_useNormalMapping, loc_useSpecularMapping, loc_useEmissionMapping, loc_useLightMap, loc_useTexture, loc_emissionPower;
    private int loc_useGlossMapping;

    private int loc_inColor;
    private int loc_projMat;
    private int loc_viewMat;
    private int loc_inLightMapCoord;
    private int loc_lightMapMatrix;

    GBufferShader() {
        super(new ResourceLocation("shaders","gbuffer.vs"), new ResourceLocation("shaders","gbuffer.fs"));
    }

    @Override
    public void bindAttribLocations() {

    }

    @Override
    protected void getAllUniformLocations() {
        loc_useNormalMapping = getUniformLocation("useNormalMapping");
        loc_useSpecularMapping = getUniformLocation("useSpecularMapping");
        loc_useEmissionMapping = getUniformLocation("useEmissionMapping");
        loc_emissionPower = getUniformLocation("emissionPower");
        loc_useLightMap = getUniformLocation("useLightMap");
        loc_useTexture = getUniformLocation("useTexture");
        loc_useGlossMapping = getUniformLocation("useGlossMapping");

        loc_inColor = super.getUniformLocation("in_color");
        loc_projMat = super.getUniformLocation("projMat");
        loc_viewMat = super.getUniformLocation("viewMat");
        loc_inLightMapCoord = super.getUniformLocation("in_lightMapCoord");
        loc_lightMapMatrix = super.getUniformLocation("lightMapMatrix");
    }

    @Override
    protected void init() {
        setInt(getUniformLocation("texture_diffuse"), 0);
        setInt(getUniformLocation("lightMap"), 1);
        setInt(getUniformLocation("texture_normal"), 2);
        setInt(getUniformLocation("texture_specular"), 3);
        setInt(getUniformLocation("texture_emission"), 4);
        setInt(getUniformLocation("texture_gloss"), 5);

        setColor(1f, 1f, 1f, 1f);
        setMatrix(loc_lightMapMatrix, GL.getLightMapTextureMatrix());
    }

    @Override
    public void setUseTexture(boolean value) {
        setBoolean(loc_useTexture, value);
    }

    @Override
    public void setLightMapping(boolean value) {
        setBoolean(loc_useLightMap, value);
    }

    @Override
    public void setNormalMapping(boolean value) {
        if(GameSettings.normalMapping) this.setBoolean(loc_useNormalMapping, value);
    }

    @Override
    public void setSpecularMapping(boolean value) {
        if(GameSettings.specularMapping) this.setBoolean(loc_useSpecularMapping, value);
    }

    @Override
    public void setEmissionMapping(boolean value) {
        if(GameSettings.emissionMapping) this.setBoolean(loc_useEmissionMapping, value);
    }

    @Override
    public void setEmissionPower(float value) {
        if(GameSettings.emissionMapping) this.setFloat(loc_emissionPower, value);
    }

    @Override
    public void setGlossMapping(boolean value) {
        if(GameSettings.glossMapping) this.setBoolean(loc_useGlossMapping, value);
    }

    @Override
    public void setColor(float r, float g, float b, float a) {
        setVector(loc_inColor, r, g, b, a);
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
        this.setVector(loc_inLightMapCoord, x, y);
    }

    @Override
    public void useLighting(boolean value) {

    }

    @Override
    public void setLightColor(float r, float g, float b) {

    }

    @Override
    public void setLightPos(float x, float y, float z) {

    }
}
