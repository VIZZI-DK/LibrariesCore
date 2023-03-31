package ru.vizzi.Utils.resouces;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.ITextureObject;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Hashtable;

import static org.lwjgl.opengl.GL11.*;

public class PNGTextureLoader {

    @Getter
    @RequiredArgsConstructor
    public static class ImageSimpleData {
        private final ByteBuffer byteBuffer;
        private final int width, height;
        private final boolean hasAlpha;
        private final int wrapFormat;
    }

    private static final ColorModel glAlphaColorModel;
    private static final ColorModel glColorModel;
    static {
        glAlphaColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,8},
                true,
                false,
                ComponentColorModel.TRANSLUCENT,
                DataBuffer.TYPE_BYTE);

        glColorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB),
                new int[] {8,8,8,0},
                false,
                false,
                ComponentColorModel.OPAQUE,
                DataBuffer.TYPE_BYTE);
    }

    public static void loadTexture(ITextureObject textureObject, ImageSimpleData imageSimpleData) {
        glDeleteTextures(textureObject.getGlTextureId());
        glBindTexture(GL_TEXTURE_2D, textureObject.getGlTextureId());

        int srcPixelFormat;
        int srcInternalFormat;
        if (imageSimpleData.hasAlpha) {
            srcPixelFormat = GL_RGBA;
            srcInternalFormat = GL_RGBA;
        } else {
            srcPixelFormat = GL_RGB;
            srcInternalFormat = GL_RGB;
        }

        glTexImage2D(GL_TEXTURE_2D, 0, srcInternalFormat, imageSimpleData.getWidth(), imageSimpleData.getHeight(), 0, srcPixelFormat, GL_UNSIGNED_BYTE, imageSimpleData.byteBuffer);

        if (OpenGlHelper.anisotropicFilteringSupported) {
            glTexParameterf(GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, Minecraft.getMinecraft().gameSettings.anisotropicFiltering);
        }

        int wrapFormat = imageSimpleData.getWrapFormat();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, wrapFormat);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, wrapFormat);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        imageSimpleData.byteBuffer.clear();
    }

    private static ByteBuffer loadImageData0(BufferedImage bufferedImage) {
        ByteBuffer imageBuffer;
        WritableRaster raster;
        BufferedImage texImage;

        int texWidth = bufferedImage.getWidth();
        int texHeight = bufferedImage.getHeight();
        boolean hasAlpha = bufferedImage.getColorModel().hasAlpha();

        if (hasAlpha) {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 4, null);
            texImage = new BufferedImage(glAlphaColorModel, raster, false, new Hashtable<>());
        } else {
            raster = Raster.createInterleavedRaster(DataBuffer.TYPE_BYTE, texWidth, texHeight, 3, null);
            texImage = new BufferedImage(glColorModel, raster, false, new Hashtable<>());
        }
        Graphics g = texImage.getGraphics();
        g.setColor(hasAlpha ? new Color(0f, 0f, 0f, 0f) : new Color(0f, 0f, 0f));
        g.fillRect(0, 0, texWidth, texHeight);
        g.drawImage(bufferedImage, 0, 0, null);

        byte[] data = ((DataBufferByte) texImage.getRaster().getDataBuffer()).getData();

        imageBuffer = ByteBuffer.allocateDirect(data.length);
        imageBuffer.order(ByteOrder.nativeOrder());
        imageBuffer.put(data, 0, data.length);
        imageBuffer.flip();

        return imageBuffer;
    }

    public static ImageSimpleData loadImageData(BufferedImage bufferedImage, int wrapFormat) {
        return new ImageSimpleData(loadImageData0(bufferedImage), bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getColorModel().hasAlpha(), wrapFormat);
    }
}
