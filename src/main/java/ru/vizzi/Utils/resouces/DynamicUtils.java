package ru.vizzi.Utils.resouces;

import lombok.experimental.UtilityClass;
import net.minecraft.client.renderer.texture.DynamicTexture;
import ru.vizzi.Utils.LibrariesCore;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@UtilityClass
public class DynamicUtils {

    public static HashMap<String, DynamicTexture> customImage = new HashMap<>();

    private static HashSet<String> loading = new HashSet<>();
    private static ExecutorService executorService;

    static {
        if(executorService == null){
            executorService = Executors.newFixedThreadPool(4);
        }
    }

    public DynamicTexture getImage(String url) {
        if(customImage.containsKey(url)){
            return customImage.get(url);
        } else {
            CompletableFuture<BufferedImage> completableFuture = getAsyncImage(url);
            if(completableFuture != null){
                completableFuture.handle((data, error) ->{
                    if(error != null){
                        error.printStackTrace();
                    } else if(data != null){
                        LibrariesCore.instance.runUsingMainClientThread(()->{
                            try{
                                customImage.put(url, new DynamicTexture(data));
                                loading.remove(url);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        });
                    }
                    return null;
                });
            }
        }
        return null;
    }

    private CompletableFuture<BufferedImage> getAsyncImage(String url){
        if(loading.contains(url)){
            return null;
        } else {
            loading.add(url);
            return CompletableFuture.supplyAsync(()->{
                try{

                    BufferedImage read = ImageIO.read(new URL(url));


                    return read;
                } catch (Exception e){
                    e.printStackTrace();

                }
                return null;
            }, executorService);
        }
    }

}
