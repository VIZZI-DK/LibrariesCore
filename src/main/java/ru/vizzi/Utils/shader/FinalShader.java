package ru.vizzi.Utils.shader;

import net.minecraft.util.ResourceLocation;

public class FinalShader extends ShaderProgram {

	private int loc_texture, loc_bloomTexture;
	private int loc_exposure, loc_gamma;
	private int loc_useGammaAndExposure;
	
	FinalShader() {
		super(new ResourceLocation("shaders","screen.vs"), new ResourceLocation("shaders","final.fs"));
	}

	@Override
	protected void getAllUniformLocations() {
		loc_texture = getUniformLocation("textureScene");
		loc_bloomTexture = getUniformLocation("textureBloom");
		loc_gamma = getUniformLocation("gamma");
		loc_exposure = getUniformLocation("exposure");
		loc_useGammaAndExposure = getUniformLocation("useGammaAndExposure");
	}

	@Override
	protected void init() {
		setInt(loc_texture, 0);
		setInt(loc_bloomTexture, 2);
		setInt(getUniformLocation("colorTexture"), 3);
		setExposure(1.0f);
		setGamma(1.0f);
	}

	void setGamma(float value) {
		setFloat(loc_gamma, value);
	}
	
	void setExposure(float value) {
		setFloat(loc_exposure, value);
	}

	public void setUseGammaAndExposure(boolean value) {
		setBoolean(loc_useGammaAndExposure, value);
	}

	@Override
	public void bindAttribLocations() {
		
	}
}
