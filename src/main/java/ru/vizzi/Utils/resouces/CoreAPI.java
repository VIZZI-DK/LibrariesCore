package ru.vizzi.Utils.resouces;

import lombok.SneakyThrows;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class CoreAPI {
	
	
	public static boolean isDefaultScale = true;

    private static final List<IResourceLoader> resourceLoaderList = Collections.synchronizedList(new ArrayList<>());
    private static boolean isInitialized;

    @SneakyThrows
    public static void init() {
        if(isInitialized) return;
        isInitialized = true;
            registerResourceLoader(new ZipResourceLoader());

    }

//    @SneakyThrows
//    public static void main(String[] args) {
//        connect("asd", "");
////        HttpURLConnection connection = (HttpURLConnection) new URL("http://s1.shadowsproject.ru:8080/kz/sosi/moihui?a=5bd263c32663619bd83db2672a1d9533&b=Kashmir").openConnection();
////        connection.setRequestMethod("GET");
////        connection.connect();
////        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////        while() {
////
////        }
////        MainDecryptor mainDecryptor = new MainDecryptor("5bd263c32663619bd83db2672a1d9533", "yds2zg6dJ7x1iyidVloecQ==", "http://s1.shadowsproject.ru:8080/kz/sosi/moihui?a=%s&b=%s", "Kashmir");
//    }

    private static byte[] readArray(DataInputStream dataInputStream, int length) throws IOException {
        byte[] bytes = new byte[length];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = dataInputStream.readByte();
        }
        return bytes;
    }

    public static void registerResourceLoader(IResourceLoader resourceLoader) {
        resourceLoaderList.add(resourceLoader);
    }

    public static List<IResourceLoader> getResourceLoaders() {
        return resourceLoaderList;
    }

    public static InputStream getInputStreamFromZip(ResourceLocation loc) {
        return getResourceInputStream(loc.getResourceDomain() + "/" + loc.getResourcePath());
    }
    
    public static InputStream getResourceInputStream(String loc) {
        try {
            for (IResourceLoader resourceLoader : getResourceLoaders()) {
                InputStream inputStream;
                if ((inputStream = resourceLoader.getResourceInputStream(loc)) != null) {
                    return inputStream;
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
            throw new RuntimeException("An error has occurred during loading resource " + loc);
    }
    
    
    public static void bindTexture(ResourceLocation resourceLocation) {
        bindTexture(resourceLocation, true);
    }

    public static void bindTexture(ResourceLocation resourceLocation, boolean decryptBefore) {
        bindTexture(resourceLocation, decryptBefore, GL13.GL_CLAMP_TO_BORDER);
    }

    public static void bindTexture(ResourceLocation loc, boolean decryptBefore, int wrapFormat) {
        TextureManager textureManager = Minecraft.getMinecraft().getTextureManager();
        if (textureManager.getTexture(loc) == null) {
            textureManager.loadTexture(loc, new AbstractTexture() {
                @Override
                public void loadTexture(IResourceManager p_110551_1_) throws IOException {
                    this.deleteGlTexture();
                    InputStream inputStream = getResourceInputStream(loc.getResourceDomain() + "/" + loc.getResourcePath());
                    if (inputStream != null) {
                        BufferedImage bufferedimage = ImageIO.read(inputStream);
                        PNGTextureLoader.loadTexture(this, PNGTextureLoader.loadImageData(bufferedimage, wrapFormat));
                        inputStream.close();
                    }
                }
            });
        }
        textureManager.bindTexture(loc);
    }
}
