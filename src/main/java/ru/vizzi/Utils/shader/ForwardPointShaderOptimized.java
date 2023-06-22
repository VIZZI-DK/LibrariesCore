package ru.vizzi.Utils.shader;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;

public class ForwardPointShaderOptimized extends ShaderProgram {

	private int loc_useTexture;
	private int loc_oldRender;
	private int loc_pointLightPos, loc_pointLightColor, loc_pointLightAttenuation, loc_pointLightSpecular;
	
	private int loc_modelView;
	private int loc_useLightMap;
	private int loc_specularUnit, loc_shininess;
	
	//private PointLight currentLight;
	
	ForwardPointShaderOptimized() {
		super(new ResourceLocation("shaders", "forward_point_vertex.vs"), new ResourceLocation("shaders", "forward_point_vertex.fs"));
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

		loc_specularUnit = getUniformLocation("specularUnit");
		loc_shininess = getUniformLocation("shininess");

		loc_pointLightColor = getUniformLocation("pointLights[0].color");
		loc_pointLightPos = getUniformLocation("pointLights[0].position");
		loc_pointLightAttenuation = getUniformLocation("pointLights[0].attenuation");
		loc_pointLightSpecular = getUniformLocation("pointLights[0].specular");
		
		loc_modelView = getUniformLocation("modelView");
		loc_oldRender = getUniformLocation("oldRender");
	}

	@Override
	protected void init() {
		setInt(getUniformLocation("diffuse"), 0);
		setInt(getUniformLocation("lightMap"), 1);
		setInt(getUniformLocation("normalMap"), 2);
		setInt(getUniformLocation("specularMap"), 3);
		setInt(getUniformLocation("glossMap"), 5);
		setSpecularPower(1.0f);
		setSpecularUnit(1.0f);
		setShininess(50f);
	}
	
	public void setSpecularUnit(float value) {
		setFloat(loc_specularUnit, value);
	}
	
	public void setShininess(float value) {
		setFloat(loc_shininess, value);
	}
	
	public void setUseLightMap(boolean value) {
		setBoolean(loc_useLightMap, value);
	}
	
	public void setUseOldRender(boolean value) {
		setBoolean(loc_oldRender, value);
	}
	
	public void setUseTexture(boolean value) {
		setBoolean(loc_useTexture, value);
	}

	public void setSpecularPower(float value) {
		setFloat(loc_pointLightSpecular, value);
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
	
	FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
	
	public void setModelView() {
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) matrix.position(0));
		this.setMatrix(loc_modelView, matrix);
	}
	
//	public PointLight getCurrentLight() {
//		return currentLight;
//	}
}
