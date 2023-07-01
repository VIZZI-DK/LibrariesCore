package ru.vizzi.librariescore.resouces;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL13;
import ru.vizzi.librariescore.resouces.PNGTextureLoader.ImageSimpleData;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public enum ResourceType {

    PNG("png",
            (field, resourceLocation) -> {
                int wrapFormat = GL13.GL_CLAMP_TO_BORDER;
                PreLoadableTextureWrapFormat annotation = field.getAnnotation(PreLoadableTextureWrapFormat.class);
                if(annotation != null) {
                    wrapFormat = annotation.value();
                }
                return TextureLoader.preloadTexture(resourceLocation, wrapFormat);
            },
            (resourceLocation, o) -> TextureLoader.loadTexture(resourceLocation, ((ImageSimpleData) o))
    ),
    /* DDS("dds",
            (field, resourceLocation) -> TextureLoaderDDS.loadDDSFile(resourceLocation),
            (resourceLocation, o) -> TextureLoaderDDS.loadTexture(((DDSFile) o), resourceLocation, new SimpleTexture(resourceLocation))
    ),
    OBJ("obj",
            (field, resourceLocation) -> ModelLoader.loadModel(resourceLocation),
            (resourceLocation, o) -> {}
    ),
    /*OGG("ogg",
            (field, resourceLocation) -> {
                Minecraft.getMinecraft().getSoundHandler().sndManager.loadSound(resourceLocation);
                return null;
            },
            (resourceLocation, object) -> {}
    )*/;

    private final String formatString;
    private final BiFunction<Field, ResourceLocation, Object> asyncFunction;
    private final BiConsumer<ResourceLocation, Object> syncConsumer;
}
