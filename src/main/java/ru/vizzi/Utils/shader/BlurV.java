package ru.vizzi.Utils.shader;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BlurV extends ShaderProgram {
	
	private int loc_texture;
	private int loc_texelSize;
	private final FrameBuffer fbo;

	BlurV() {
		super(new ResourceLocation("shaders", "gaussian.vs"), new ResourceLocation("shaders","gaussian_v.fs"));
		fbo = new FrameBuffer(mc.displayWidth, mc.displayHeight);
	}

	@Override
	protected void getAllUniformLocations() {
		loc_texture = getUniformLocation("textureScene");
		loc_texelSize = getUniformLocation("texelSize");
	}

	@Override
	protected void init() {
		setInt(loc_texture, 0);
		setVector(loc_texelSize, 1.0f / (float) mc.displayWidth, 1.0f / (float) mc.displayHeight);
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
