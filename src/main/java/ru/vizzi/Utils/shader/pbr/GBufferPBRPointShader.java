package ru.vizzi.Utils.shader.pbr;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import ru.vizzi.Utils.shader.ShaderProgram;

import java.nio.FloatBuffer;

public class GBufferPBRPointShader extends ShaderProgram {

    private int loc_pointLightPos, loc_pointLightColor, loc_pointLightAttenuation, loc_pointLightSpecular;
    private int loc_modelView;

    public GBufferPBRPointShader() {
        super(new ResourceLocation("shaders", "gbuffer_point.vs"), new ResourceLocation("shaders", "gbuffer_pbr_point.fs"));
    }

    @Override
    public void bindAttribLocations() {

    }

    @Override
    protected void getAllUniformLocations() {
        loc_pointLightColor = getUniformLocation("pointLights[0].color");
        loc_pointLightPos = getUniformLocation("pointLights[0].position");
        loc_pointLightAttenuation = getUniformLocation("pointLights[0].attenuation");
        loc_pointLightSpecular = getUniformLocation("pointLights[0].specular");

        loc_modelView = getUniformLocation("modelView");
    }

    @Override
    protected void init() {
        setInt(getUniformLocation("gColor"), 0);
        setInt(getUniformLocation("lightmap"), 1);
        setInt(getUniformLocation("gLightMap"), 2);
        setInt(getUniformLocation("gPosition"), 3);
        setInt(getUniformLocation("gNormal"), 4);
    }

    private final Vector3f lightPos = new Vector3f();

    public void setSpecularPower(float value) {
        setFloat(loc_pointLightSpecular, value);
    }

//    public void setPointLight(PointLight light) {
//        lightPos.x = light.pos.x - (float)TileEntityRendererDispatcher.staticPlayerX;
//        lightPos.y = light.pos.y - (float)TileEntityRendererDispatcher.staticPlayerY;
//        lightPos.z = light.pos.z - (float)TileEntityRendererDispatcher.staticPlayerZ;
//        setVector(loc_pointLightColor, light.color);
//        setVector(loc_pointLightPos, lightPos);
//        setFloat(loc_pointLightAttenuation, light.power);
//    }

    private final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);

    public void setModelView() {
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) matrix.position(0));
        this.setMatrix(loc_modelView, matrix);
    }

    public void setLightColor(Vector3f color) {
        setVector(loc_pointLightColor, color);
    }

    public void setLightPos(Vector3f pos) {
        setVector(loc_pointLightPos, pos);
    }

    public void setLightAtt(float value) {
        setFloat(loc_pointLightAttenuation, value);
    }

}
