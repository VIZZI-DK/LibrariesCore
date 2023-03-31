package ru.vizzi.Utils.resouces;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import ru.vizzi.Utils.CompletableFutureBuilder;

public class TextureLoader {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(1);

    public CompletableFuture<BufferedImage> preloadTextureAsync(InputStream inputStream) {
        return preloadTextureAsync(inputStream, "png");
    }

    public CompletableFuture<BufferedImage> preloadTextureAsync(InputStream inputStream, String imageFormat) {
        return CompletableFuture.supplyAsync(() -> preloadTexture(inputStream, imageFormat), EXECUTOR_SERVICE);
    }

    public BufferedImage preloadTexture(InputStream inputStream) {
        return preloadTexture(inputStream, "png");
    }

    @SneakyThrows
    public BufferedImage preloadTexture(InputStream inputStream, String imageFormat) {
        boolean isJPG = imageFormat.equalsIgnoreCase("jpg");
        BufferedImage bufferedImage = ImageIO.read(inputStream);
        if (isJPG) {
            ByteArrayOutputStream byteArrayOut = new ByteArrayOutputStream();
            if (ImageIO.write(bufferedImage, "png", byteArrayOut)) {
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOut.toByteArray());
                bufferedImage = ImageIO.read(byteArrayInputStream);
                byteArrayInputStream.close();
            } else {
                bufferedImage = null;
            }
            byteArrayOut.close();
        }
        return bufferedImage;
    }

    public SimpleTextureData preloadTexture(ResourceLocation resourceLocation) {
        return readTexture(preloadTexture(CoreAPI.getInputStreamFromZip(resourceLocation), "png"));
    }

    public void loadTexture(ResourceLocation resourceLocation, SimpleTextureData simpleTextureData) {
        if (Minecraft.getMinecraft().getTextureManager().getTexture(resourceLocation) != null) {
            return;
        }
        Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new DynamicTextureNew(simpleTextureData.getPixels(),simpleTextureData.getWidth(), simpleTextureData.getHeight()));
    }

    public int[] readPixels(BufferedImage bufferedImage) {
        return bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(),
                new int[bufferedImage.getWidth() * bufferedImage.getHeight() * 3], 0, bufferedImage.getWidth());
    }

    public SimpleTextureData readTexture(BufferedImage bufferedImage) {
        return new SimpleTextureData(
                readPixels(bufferedImage),
                bufferedImage.getWidth(),
                bufferedImage.getHeight()
        );
    }

    public ResourceLocation loadTexture(int[] pixels, int width, int height, String path) {
        ResourceLocation resourceLocation = new ResourceLocation(path);
        if (Minecraft.getMinecraft().getTextureManager().getTexture(resourceLocation) != null) {
            return resourceLocation;
        }
        
        System.out.println("widht:"+width);
        System.out.println("height:"+height);
        System.out.println("pixels:"+pixels.toString());
        Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new DynamicTextureNew(pixels,width, height));
        return resourceLocation;
    }

//    public CompletableFutureBuilder<ResourceLocation> loadTexture(ResourceLocation resourceLocation) {
//        return loadTexture(Utils.getInputStreamFromZip(resourceLocation), "png", resourceLocation.toString());
//    }

    public CompletableFutureBuilder<ResourceLocation> loadTexture(InputStream inputStream, String imageFormat, String path) {
        return CompletableFutureBuilder.supplyAsync(() -> preloadTexture(inputStream, imageFormat), EXECUTOR_SERVICE)
                .syncQueueTimeout(-1)
                .thenApply(bufferedImage -> {
                    try {
                        return readTexture(bufferedImage);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    return null;
                })
                .thenApplySync(simpleTextureData -> {
                    try {
                        return loadTexture(simpleTextureData.getPixels(), simpleTextureData.getWidth(), simpleTextureData.getHeight(), path);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    return null;
                });
    }

    @SneakyThrows
    public static PNGTextureLoader.ImageSimpleData preloadTexture(ResourceLocation resourceLocation, int wrapFormat) {
        return PNGTextureLoader.loadImageData(ImageIO.read(CoreAPI.getInputStreamFromZip(resourceLocation)), wrapFormat);
    }

    public static void loadTexture(ResourceLocation resourceLocation, PNGTextureLoader.ImageSimpleData imageSimpleData) {
        Minecraft.getMinecraft().getTextureManager().loadTexture(resourceLocation, new Texture(imageSimpleData));
    }
}
