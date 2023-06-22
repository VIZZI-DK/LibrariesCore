package ru.vizzi.Utils.shader;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class DownsampleFirstShader extends ShaderProgram {

	private int loc_texelSize;
	private int loc_bloomTexture;
	private final FrameBuffer fbo;
	
	DownsampleFirstShader() {
		super(new ResourceLocation("shaders","screen.vs"), new ResourceLocation("shaders","downsample_first.fs"));
		fbo = new FrameBuffer(mc.displayWidth, mc.displayHeight);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_texelSize = getUniformLocation("texelSize");
		loc_bloomTexture = getUniformLocation("bloomTexture");
	}

	@Override
	protected void init() {
		setVector(loc_texelSize, 1.0f / (float) mc.displayWidth, 1.0f / (float) mc.displayHeight);
		setInt(loc_bloomTexture, 0);
	}

	public void resize(int width, int height) {
		enable();
		setVector(loc_texelSize, 1.0f / (float) width, 1.0f / (float) height);
		disable();
		fbo.resize(width, height);
		fbo.init();
	}
	
	public void render(int texture) {
		enable();
        fbo.bind(false);
        fbo.clear();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0f, 0f, 0f, 0f, 0f);
		tessellator.addVertexWithUV(1f, 0f, 0f, 1f, 0f);
		tessellator.addVertexWithUV(1f, 1f, 0f, 1f, 1f);
		tessellator.addVertexWithUV(0f, 1f, 0f, 0f, 1f);
		tessellator.draw();
		fbo.unbind();
		disable();
	}
	
	public int getTexture() {
		return fbo.getTexture();
	}

	@Override
	public void bindAttribLocations() {

	}
	
	@Override
	public void clear() {
		super.clear();
		fbo.delete();
	}
}
