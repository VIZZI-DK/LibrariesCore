package ru.vizzi.Utils.resouces;

import static org.lwjgl.opengl.GL13.GL_CLAMP_TO_BORDER;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public class Texture extends AbstractTexture {

    private final PNGTextureLoader.ImageSimpleData imageSimpleData;
    private final ResourceLocation resourceLocation;

    public Texture(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
        this.imageSimpleData = null;
    }

    public Texture(PNGTextureLoader.ImageSimpleData imageSimpleData) {
        this.imageSimpleData = imageSimpleData;
        this.resourceLocation = null;
    }

    @Override
    public void loadTexture(IResourceManager p_110551_1_) throws IOException {
        this.deleteGlTexture();
        if(imageSimpleData != null) PNGTextureLoader.loadTexture(this, imageSimpleData);
        else {
            try (InputStream inputstream = CoreAPI.getResourceInputStream(resourceLocation.getResourceDomain() + "/" + resourceLocation.getResourcePath())) {
                if (inputstream == null) throw new IOException();
                BufferedImage bufferedimage = ImageIO.read(inputstream);
                PNGTextureLoader.loadTexture(this, PNGTextureLoader.loadImageData(bufferedimage, GL_CLAMP_TO_BORDER));
            }
        }
    }
}