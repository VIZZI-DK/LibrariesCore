package ru.vizzi.Utils.shader;

import net.minecraft.util.ResourceLocation;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

import java.nio.FloatBuffer;

public class GBufferDirectionalShader extends ShaderProgram {

	private int loc_directLightColor;
	private int loc_directLightDirection;
	private int loc_directLightSpecular;
	private int loc_modelView;
	private int loc_usePointLights;
	
	GBufferDirectionalShader() {
		super(new ResourceLocation("shaders", "gbuffer_directional.vs"), new ResourceLocation("shaders", "gbuffer_directional.fs"));
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
		
		loc_usePointLights = getUniformLocation("usePointLights");
	}

	@Override
	protected void init() {
		setInt(getUniformLocation("gColor"), 0);
		setInt(getUniformLocation("lightmap"), 1);
		setInt(getUniformLocation("gLightMap"), 2);
		setInt(getUniformLocation("gPosition"), 3);
		setInt(getUniformLocation("gNormal"), 4);
		setSpecularPower(1.0f);
	}
	
	public void setSpecularPower(float value) {
		setFloat(loc_directLightSpecular, value);
	}
	
	private final FloatBuffer matrix = BufferUtils.createFloatBuffer(16);
	
	public void setDirectionalValues() {
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, (FloatBuffer) matrix.position(0));
		this.setMatrix(loc_modelView, matrix);
		
		setVector(loc_directLightDirection, KrogenitShaders.forwardPBRDirectionalShaderOld.getDirectDirection());
		setVector(loc_directLightColor, KrogenitShaders.forwardPBRDirectionalShaderOld.getDirectColor());
		setUsePointLights(GameSettings.lighting > 1);
	}
	
	private boolean prevPointLights;
	
	public void setUsePointLights(boolean value) {
		if(prevPointLights != value) {
			prevPointLights = value;
			setBoolean(loc_usePointLights, value);
		}
	}
	
}
