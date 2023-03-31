package ru.vizzi.Utils.resouces;


import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;

@SideOnly(Side.CLIENT)
public class DynamicTextureNew extends AbstractTexture
{
    private final int[] dynamicTextureData;
    /** width of this icon in pixels */
    private final int width;
    /** height of this icon in pixels */
    private final int height;


    public DynamicTextureNew(BufferedImage p_i1270_1_)
    {
        this(p_i1270_1_.getWidth(), p_i1270_1_.getHeight());
        p_i1270_1_.getRGB(0, 0, p_i1270_1_.getWidth(), p_i1270_1_.getHeight(), this.dynamicTextureData, 0, p_i1270_1_.getWidth());
        this.updateDynamicTexture();
    }

    public DynamicTextureNew(int p_i1271_1_, int p_i1271_2_)
    {
        this.width = p_i1271_1_;
        this.height = p_i1271_2_;
        this.dynamicTextureData = new int[p_i1271_1_ * p_i1271_2_];
        TextureUtil.allocateTexture(this.getGlTextureId(), p_i1271_1_, p_i1271_2_);
    }
    public DynamicTextureNew(int[] pixels, int p_i1271_1_, int p_i1271_2_)
    {
        this.width = p_i1271_1_;
        this.height = p_i1271_2_;
        this.dynamicTextureData = pixels;
        TextureUtil.allocateTexture(this.getGlTextureId(), p_i1271_1_, p_i1271_2_);
        this.updateDynamicTexture();
    }

    public void loadTexture(IResourceManager p_110551_1_) throws IOException {}

    public void updateDynamicTexture()
    {
        TextureUtil.uploadTexture(this.getGlTextureId(), this.dynamicTextureData, this.width, this.height);
    }

    public int[] getTextureData()
    {
        return this.dynamicTextureData;
    }
}