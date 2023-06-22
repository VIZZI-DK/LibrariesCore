package ru.vizzi.Utils.shader;

import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import ru.vizzi.Utils.shader.pbr.IPBR;


import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;

public class ForwardPointShader extends ShaderProgram implements IPBR {

	private int loc_useTexture;
	private int loc_useNormalMapping, loc_useSpecularMapping;
	private int loc_useGlossMapping;

	private int loc_pointLightPos, loc_pointLightColor, loc_pointLightAttenuation;
	
	private int loc_modelView;
	private int loc_useLightMap;
	
	//private PointLight currentLight;
	
	ForwardPointShader() {
		super(new ResourceLocation("shaders", "forward_point.vs"), new ResourceLocation("shaders", "forward_point.fs"));
	}

	@Override
	public void bindAttribLocations() {
		glBindAttribLocation(shaderProgram, ShaderLoader.loc_in_position, "in_position");
		glBindAttribLocation(shaderProgram, ShaderLoader.loc_in_textureCoords, "in_textureCoords");
		glBindAttribLocation(shaderProgram, ShaderLoader.loc_in_normal, "in_normal");
		glBindAttribLocation(shaderProgram, ShaderLoader.loc_in_tangent, "in_tangent");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_useTexture = getUniformLocation("useTexture");
		loc_useLightMap = getUniformLocation("useLightMap");

		loc_useNormalMapping = getUniformLocation("useNormalMapping");
		loc_useSpecularMapping = getUniformLocation("useSpecularMapping");
		loc_useGlossMapping = getUniformLocation("useGlossMapping");

		loc_pointLightColor = getUniformLocation("pointLights[0].color");
		loc_pointLightPos = getUniformLocation("pointLights[0].position");
		loc_pointLightAttenuation = getUniformLocation("pointLights[0].attenuation");

		loc_modelView = getUniformLocation("modelView");
	}

	@Override
	protected void init() {
		setInt(getUniformLocation("diffuse"), 0);
		setInt(getUniformLocation("lightMap"), 1);
		setInt(getUniformLocation("normalMap"), 2);
		setInt(getUniformLocation("specularMap"), 3);
		setInt(getUniformLocation("glossMap"), 5);
	}

	@Override
	public void setLightMapping(boolean value) {
		setBoolean(loc_useLightMap, value);
	}

	@Override
	public void setUseTexture(boolean value) {
		setBoolean(loc_useTexture, value);
	}

	@Override
	public void setNormalMapping(boolean value) {
		if(GameSettings.normalMapping) setBoolean(loc_useNormalMapping, value);
	}

	@Override
	public void setSpecularMapping(boolean value) {
		if(GameSettings.specularMapping) setBoolean(loc_useSpecularMapping, value);
	}

	@Override
	public void setEmissionMapping(boolean value) {

	}

	@Override
	public void setEmissionPower(float value) {

	}

	@Override
	public void setGlossMapping(boolean value) {
		if(GameSettings.glossMapping) setBoolean(loc_useGlossMapping, value);
	}

	private final Vector3f lightPos = new Vector3f();
	
//	public void setPointLight(PointLight light) {
//		lightPos.x = light.pos.x - (float)TileEntityRendererDispatcher.staticPlayerX;
//		lightPos.y = light.pos.y - (float)TileEntityRendererDispatcher.staticPlayerY;
//		lightPos.z = light.pos.z - (float)TileEntityRendererDispatcher.staticPlayerZ;
//		setVector(loc_pointLightColor, light.color);
//		setVector(loc_pointLightPos, lightPos);
//		setFloat(loc_pointLightAttenuation, light.power);
//		currentLight = light;
//	}

	public void setModelView() {
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) matrix.position(0));
		this.setMatrix(loc_modelView, matrix);
	}
	
//	public PointLight getCurrentLight() {
//		return currentLight;
//	}

	@Override
	public void setLightColor(float r, float g, float b) {
		setVector(loc_pointLightColor, r, g, b);
	}

	@Override
	public void setLightPos(float x, float y, float z) {
		lightPos.x = x - (float)TileEntityRendererDispatcher.staticPlayerX;
		lightPos.y = y - (float)TileEntityRendererDispatcher.staticPlayerY;
		lightPos.z = z - (float)TileEntityRendererDispatcher.staticPlayerZ;
	}

	@Override
	public void setColor(float r, float g, float b, float a) {

	}

	@Override
	public void setViewMatrix() {

	}

	@Override
	public void setLightMapCoords(float x, float y) {

	}

	@Override
	public void useLighting(boolean value) {

	}
}
