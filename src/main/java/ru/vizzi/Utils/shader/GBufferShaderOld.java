package ru.vizzi.Utils.shader;

import net.minecraft.util.ResourceLocation;
import ru.vizzi.Utils.shader.pbr.IPBR;

import static org.lwjgl.opengl.GL20.glBindAttribLocation;

public class GBufferShaderOld extends ShaderProgram implements IPBR {

	private int loc_useNormalMapping, loc_useSpecularMapping, loc_useEmissionMapping, loc_useLightMap, loc_useTexture, loc_emissionPower;
	private int loc_useGlossMapping;
	
	GBufferShaderOld() {
		super(new ResourceLocation("shaders","gbuffer_old.vs"), new ResourceLocation("shaders","gbuffer.fs"));
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
		loc_useNormalMapping = getUniformLocation("useNormalMapping");
		loc_useSpecularMapping = getUniformLocation("useSpecularMapping");
		loc_useEmissionMapping = getUniformLocation("useEmissionMapping");
		loc_emissionPower = getUniformLocation("emissionPower");
		loc_useLightMap = getUniformLocation("useLightMap");
		loc_useTexture = getUniformLocation("useTexture");
		loc_useGlossMapping = getUniformLocation("useGlossMapping");
	}

	@Override
	protected void init() {
		setInt(getUniformLocation("texture_diffuse"), 0);
		setInt(getUniformLocation("lightMap"), 1);
		setInt(getUniformLocation("texture_normal"), 2);
		setInt(getUniformLocation("texture_specular"), 3);
		setInt(getUniformLocation("texture_emission"), 4);
		setInt(getUniformLocation("texture_gloss"), 5);
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
	public void setLightColor(float r, float g, float b) {

	}

	@Override
	public void setLightPos(float x, float y, float z) {

	}

	@Override
	public void setColor(float r, float g, float b, float a) {
		KrogenitShaders.gBufferShader.enable();
		KrogenitShaders.gBufferShader.setColor(r, g, b, a);
		KrogenitShaders.gBufferShaderOld.enable();
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
