package ru.vizzi.Utils.shader.pbr;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import ru.vizzi.Utils.shader.KrogenitShaders;
import ru.vizzi.Utils.shader.ShaderProgram;

import java.nio.FloatBuffer;

public class GBufferPBRDirectionalShader extends ShaderProgram {
    private int loc_directLightColor;
    private int loc_directLightDirection;
    private int loc_directLightSpecular;
    private int loc_modelView;
    private int loc_useDirectionalLight;

    public GBufferPBRDirectionalShader() {
        super(new ResourceLocation("shaders", "gbuffer_directional.vs"), new ResourceLocation("shaders", "gbuffer_pbr_directional.fs"));
    }

    @Override
    public void bindAttribLocations() {

    }

    @Override
    protected void getAllUniformLocations() {
        loc_directLightColor = getUniformLocation("directLight[0].color");
        loc_directLightDirection = getUniformLocation("directLight[0].dir");
        loc_directLightSpecular = getUniformLocation("directLight[0].specular");

        loc_modelView = getUniformLocation("modelView");

        loc_useDirectionalLight = getUniformLocation("usePointLights");
    }

    @Override
    protected void init() {
        setInt(getUniformLocation("gColor"), 0);
        setInt(getUniformLocation("lightmap"), 1);
        setInt(getUniformLocation("gLightMap"), 2);
        setInt(getUniformLocation("gPosition"), 3);
        setInt(getUniformLocation("gNormal"), 4);
        setInt(getUniformLocation("gEmission"), 5);
        setSpecularPower(1.0f);
    }

    public void setSpecularPower(float value) {
        setFloat(loc_directLightSpecular, value);
    }

    public void setDirectionalValues() {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) matrix.position(0));
        this.setMatrix(loc_modelView, matrix);

        setVector(loc_directLightDirection, KrogenitShaders.forwardPBRDirectionalShaderOld.getDirectDirection());
        setVector(loc_directLightColor, KrogenitShaders.forwardPBRDirectionalShaderOld.getDirectColor());
    }

    public void useDirectionalLight(boolean value) {
        setBoolean(loc_useDirectionalLight, value);
    }
}
