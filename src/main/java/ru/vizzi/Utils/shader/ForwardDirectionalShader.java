package ru.vizzi.Utils.shader;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;
import ru.vizzi.Utils.shader.pbr.IPBR;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;

@Deprecated
public class ForwardDirectionalShader extends ShaderProgram implements IPBR {
	private int loc_useTexture;
	private int loc_textureDiffuse;
	private int loc_useLightMap;
	private int loc_textureLightMap;
	private int loc_textureNormal;
	private int loc_textureSpecular;
	private int loc_textureEmission;
	private int loc_textureGloss;
	private int loc_useNormalMapping;
	private int loc_useSpecularMapping;
	private int loc_useEmissionMapping;
	private int loc_emissionPower;
	private int loc_useGlossMapping;

	private int loc_modelView;
	
	private int loc_useDirectLight;
	private int loc_directLightColor;
	private int loc_directLightDirection;
	private int loc_directLightSpecular;

	private final Vector3f directColor = new Vector3f();
	private final Vector3f directDirection = new Vector3f();

	ForwardDirectionalShader() {
		super(new ResourceLocation("shaders", "base_shader.vs"), new ResourceLocation("shaders", "base_shader.fs"));
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
		loc_useTexture = super.getUniformLocation("useTexture");
		loc_textureDiffuse = super.getUniformLocation("diffuse");
		loc_useLightMap = super.getUniformLocation("useLightMap");
		loc_textureLightMap = super.getUniformLocation("lightMap");
		loc_textureNormal = super.getUniformLocation("normalMap");
		loc_textureSpecular = super.getUniformLocation("specularMap");
		loc_textureEmission = super.getUniformLocation("emissionMap");
		loc_textureGloss = super.getUniformLocation("glossMap");
		loc_useNormalMapping = super.getUniformLocation("useNormalMapping");
		loc_useSpecularMapping = super.getUniformLocation("useSpecularMapping");
		loc_useEmissionMapping = super.getUniformLocation("useEmissionMapping");
		loc_emissionPower = super.getUniformLocation("emissionPower");
		loc_useGlossMapping = super.getUniformLocation("useGlossMapping");

		loc_modelView = super.getUniformLocation("modelView");
		
		loc_useDirectLight = super.getUniformLocation("useDirectLight");
		loc_directLightColor = super.getUniformLocation("directLight[0].color");
		loc_directLightDirection = super.getUniformLocation("directLight[0].dir");
		loc_directLightSpecular = super.getUniformLocation("directLight[0].specular");
	}
	
	@Override
	protected void init() {
		this.setInt(loc_textureDiffuse, 0);
		this.setInt(loc_textureLightMap, 1);
		this.setInt(loc_textureNormal, 2);
		this.setInt(loc_textureSpecular, 3);
		this.setInt(loc_textureEmission, 4);
		setInt(loc_textureGloss, 5);
		setEmissionPower(1.0f);
		setSpecularPower(1.0f);
		setUseTexture(true);
		setLightMapping(true);
	}

	@Override
	public void setUseTexture(boolean value) {
		setBoolean(loc_useTexture, value);
	}

	@Override
	public void setLightMapping(boolean value) {
		setBoolean(loc_useLightMap, value);
	}
	
	public void setSpecularPower(float value) {
		setFloat(loc_directLightSpecular, value);
	}

	public void setDirectionLight(boolean value) {
		if(GameSettings.lighting > 0) this.setBoolean(loc_useDirectLight, value);
	}

	public void setDirectionLightColor(float r, float g, float b) {
		if(GameSettings.lighting > 0) this.setVector(loc_directLightColor, r, g, b);
	}
	
	public void setDirectionLightDirection(float x, float y, float z) {
		if(GameSettings.lighting > 0) this.setVector(loc_directLightDirection, x, y, z);
	}

	@Override
	public void setNormalMapping(boolean value) {
		if(GameSettings.normalMapping) this.setBoolean(loc_useNormalMapping, value);
	}

	@Override
	public void setEmissionMapping(boolean value) {
		if(GameSettings.emissionMapping) this.setBoolean(loc_useEmissionMapping, value);
	}

	@Override
	public void setSpecularMapping(boolean value) {
		if(GameSettings.specularMapping) this.setBoolean(loc_useSpecularMapping, value);
	}

	@Override
	public void setEmissionPower(float value) {
		if(GameSettings.emissionMapping) this.setFloat(loc_emissionPower, value);
	}

	@Override
	public void setGlossMapping(boolean value) {
		if(GameSettings.glossMapping) this.setBoolean(loc_useGlossMapping, value);
	}

	private final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);

	public void setModelView() {
		if(GameSettings.lighting > 0) {
			GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) matrix.position(0));
			this.setMatrix(loc_modelView, matrix);
		}
	}
	
	public Vector3f getDirectDirection() {
		return directDirection;
	}
	
	public Vector3f getDirectColor() {
		return directColor;
	}

	@Override
	public void setLightColor(float r, float g, float b) {
		this.setDirectionLightColor(r, g, b);
	}

	@Override
	public void setLightPos(float x, float y, float z) {
		this.setDirectionLightDirection(x, y, z);
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
		setDirectionLight(value);
	}
}
