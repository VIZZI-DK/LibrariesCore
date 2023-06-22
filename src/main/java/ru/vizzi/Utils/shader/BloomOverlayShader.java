package ru.vizzi.Utils.shader;

import net.minecraft.util.ResourceLocation;

public class BloomOverlayShader extends ShaderProgram {

	private int loc_useTexture;
	private int loc_bloomThreshold, loc_bloomIntensity;

	public BloomOverlayShader() {
		super(new ResourceLocation("shaders", "bloom_overlay.vs"), new ResourceLocation("shaders", "bloom_overlay.fs"));
	}

	@Override
	public void bindAttribLocations() {

	}

	@Override
	protected void getAllUniformLocations() {
		loc_useTexture = getUniformLocation("useTexture");
		loc_bloomThreshold = getUniformLocation("bloomThreshold");
		loc_bloomIntensity = getUniformLocation("bloomIntensity");
	}

	@Override
	protected void init() {
		setInt(getUniformLocation("diffuse"), 0);
		setBoolean(loc_useTexture, true);
		setBloomIntensity(2f);
		setBloomThreshold(0.0f);
	}

	public void setUseTexture(boolean value) {
		setBoolean(loc_useTexture, value);
	}

	public void setBloomThreshold(float value) {
		setFloat(loc_bloomThreshold, value);
	}

	public void setBloomIntensity(float value) {
		setFloat(loc_bloomIntensity, value);
	}

}
