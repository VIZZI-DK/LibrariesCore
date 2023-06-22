package ru.vizzi.Utils.shader;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.*;

public class UnsampleBloomShader extends ShaderProgram {

	private int loc_texelSize;
	private int loc_unsampleTexture, loc_gaussianTexture;
	
	private final FrameBuffer fbo;
	private static final Tessellator tessellator = Tessellator.instance;
	
	UnsampleBloomShader() {
		super(new ResourceLocation("shaders","screen.vs"), new ResourceLocation("shaders","unsample.fs"));
		fbo = new FrameBuffer(mc.displayWidth, mc.displayHeight);
	}
	
	@Override
	public void bindAttribLocations() {

	}

	@Override
	protected void getAllUniformLocations() {
		loc_texelSize = getUniformLocation("texelSize");
		loc_unsampleTexture = getUniformLocation("downsampleTexture");
		loc_gaussianTexture = getUniformLocation("gaussianTexture");
	}

	@Override
	protected void init() {
		setVector(loc_texelSize, 1.0f / (float) mc.displayWidth, 1.0f / (float) mc.displayHeight);
		setInt(loc_unsampleTexture, 0);
		setInt(loc_gaussianTexture, 2);
	}

	public void resize(int width, int height) {
		enable();
		setVector(loc_texelSize, 1.0f / (float) mc.displayWidth, 1.0f / (float) mc.displayHeight);
		disable();
		fbo.resize(width, height);
		fbo.init();
	}
	
	public void render(int textureDownsample, int textureGaussian) {
		enable();
        fbo.bind(false);
        fbo.clear();
        glBindTexture(GL_TEXTURE_2D, textureDownsample);
        glActiveTexture(GL_TEXTURE2);
        glBindTexture(GL_TEXTURE_2D, textureGaussian);
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0f, 0f, 0f, 0f, 0f);
		tessellator.addVertexWithUV(1f, 0f, 0f, 1f, 0f);
		tessellator.addVertexWithUV(1f, 1f, 0f, 1f, 1f);
		tessellator.addVertexWithUV(0f, 1f, 0f, 0f, 1f);
		tessellator.draw();
		fbo.unbind();
		disable();
        glActiveTexture(GL_TEXTURE0);
	}
	
	public int getTexture() {
		return fbo.getTexture();
	}
	
	@Override
	public void clear() {
		super.clear();
		fbo.delete();
	}
}
