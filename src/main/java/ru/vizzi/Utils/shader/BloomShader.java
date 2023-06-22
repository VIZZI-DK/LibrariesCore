package ru.vizzi.Utils.shader;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class BloomShader extends ShaderProgram {

    private final FrameBuffer fbo;
    private int loc_bloomThreshold, loc_bloomIntensity;

    public BloomShader() {
        super(new ResourceLocation("shaders", "bloom.vs"), new ResourceLocation("shaders", "bloom.fs"));
        fbo = new FrameBuffer(mc.displayWidth, mc.displayHeight);
    }

    @Override
    public void bindAttribLocations() {

    }

    @Override
    protected void getAllUniformLocations() {
        loc_bloomThreshold = getUniformLocation("bloomThreshold");
        loc_bloomIntensity = getUniformLocation("bloomIntensity");
    }

    @Override
    protected void init() {
        setInt(getUniformLocation("diffuse"), 0);
        setBloomIntensity(2f);
        setBloomThreshold(0.0f);
    }

    public void resize(int width, int height) {
        fbo.resize(width, height);
        fbo.init();
    }

    public void render(int texture, float threshold, float intensity) {
        enable();
        setBloomThreshold(threshold);
        setBloomIntensity(intensity);
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

    public void setBloomThreshold(float value) {
        setFloat(loc_bloomThreshold, value);
    }

    public void setBloomIntensity(float value) {
        setFloat(loc_bloomIntensity, value);
    }

    @Override
    public void clear() {
        super.clear();
        fbo.delete();
    }
}
