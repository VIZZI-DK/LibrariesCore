package ru.vizzi.Utils.shader;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.shader.Framebuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class PostProcessing {

	static Minecraft mc = Minecraft.getMinecraft();
	private static final Framebuffer mcBuffer = mc.getFramebuffer();

	public static BloomShader bloom = new BloomShader();
	private static BlurH blurH = new BlurH();
	private static BlurV blurV = new BlurV();
	private static DownsampleFirstShader dsFirst = new DownsampleFirstShader();
	private static DownsampleSecondShader dsSecond = new DownsampleSecondShader();
	private static UnsampleBloomShader us = new UnsampleBloomShader();
	private static FinalShader finalShader = new FinalShader();

	static {
		bloom.preInit();
		blurH.preInit();
		blurV.preInit();
		dsFirst.preInit();
		dsSecond.preInit();
		us.preInit();
		finalShader.preInit();
	}

	public static void render() {
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0.0D, 1.0D, 0.0D, 1.0D, 0.0D, 1.0D);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		glEnable(GL_TEXTURE_2D);
		glDisable(GL_LIGHTING);
		glDisable(GL_ALPHA_TEST);
		glDisable(GL_BLEND);
		glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		glClearColor(0, 0, 0, 1);

		bloom.render(mcBuffer.framebufferTexture, 1.3f, 0.75f);
		dsFirst.render(bloom.getTexture());
		dsSecond.render(dsFirst.getTexture());

		blurH.render(dsSecond.getTexture());
		blurV.render(blurH.getTexture());

		us.render(dsSecond.getTexture(), blurV.getTexture());

		mcBuffer.bindFramebuffer(true);
		finalShader.enable();
		finalShader.setUseGammaAndExposure(true);
		finalShader.setGamma(0.9f);
		finalShader.setExposure(1.35f);
		glBindTexture(GL_TEXTURE_2D, mcBuffer.framebufferTexture);
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, us.getTexture());
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.addVertexWithUV(0f, 0f, 0f, 0f, 0f);
		tessellator.addVertexWithUV(1f, 0f, 0f, 1f, 0f);
		tessellator.addVertexWithUV(1f, 1f, 0f, 1f, 1f);
		tessellator.addVertexWithUV(0f, 1f, 0f, 0f, 1f);
		tessellator.draw();
		finalShader.disable();

		glActiveTexture(GL_TEXTURE0);
		mcBuffer.unbindFramebufferTexture();
		glDepthMask(true);
	}

	public static void resize(int width, int height) {
		bloom.resize(width, height);
		blurH.resize(width, height);
		blurV.resize(width, height);
		dsFirst.resize(width, height);
		dsSecond.resize(width, height);
		us.resize(width, height);
	}

	static void reload() {
		bloom.clear();
		bloom = new BloomShader();
		blurH.clear();
		blurH = new BlurH();
		blurV.clear();
		blurV = new BlurV();
		dsFirst.clear();
		dsFirst = new DownsampleFirstShader();
		dsSecond.clear();
		dsSecond = new DownsampleSecondShader();
		finalShader.clear();
		finalShader = new FinalShader();
		us.clear();
		us = new UnsampleBloomShader();
		bloom.preInit();
		blurH.preInit();
		blurV.preInit();
		dsFirst.preInit();
		dsSecond.preInit();
		us.preInit();
		finalShader.preInit();
	}
}
