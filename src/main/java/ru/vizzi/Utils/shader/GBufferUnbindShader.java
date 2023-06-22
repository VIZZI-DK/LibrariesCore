package ru.vizzi.Utils.shader;

import net.minecraft.util.ResourceLocation;

public class GBufferUnbindShader extends ShaderProgram {
	
	private int loc_usePointLights;
	
	GBufferUnbindShader() {
		super(new ResourceLocation("shaders", "gbufferunbind.vs"), new ResourceLocation("shaders", "gbufferunbind.fs"));
	}

	@Override
	public void bindAttribLocations() {
		
	}

	@Override
	protected void getAllUniformLocations() {
		loc_usePointLights = getUniformLocation("usePointLights");
	}

	@Override
	protected void init() {
		setInt(getUniformLocation("gColor"), 0);
		setInt(getUniformLocation("lightmap"), 1);
		setInt(getUniformLocation("gLightMap"), 2);
		setInt(getUniformLocation("gPosition"), 3);
		setInt(getUniformLocation("gNormal"), 4);
	}
	
	private boolean prevPointLights;
	
	public void setUsePointLights(boolean value) {
		if(prevPointLights != value) {
			prevPointLights = value;
			setBoolean(loc_usePointLights, value);
		}
	}
	
}
