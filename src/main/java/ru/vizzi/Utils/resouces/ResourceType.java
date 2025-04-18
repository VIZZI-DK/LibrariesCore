package ru.vizzi.Utils.resouces;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.util.ResourceLocation;
import ru.vizzi.Utils.CustomFont.FontAPI;
import ru.vizzi.Utils.CustomFont.FontContainer;
import ru.vizzi.Utils.gui.drawmodule.GuiDrawUtils;
import ru.vizzi.Utils.resouces.PNGTextureLoader.ImageSimpleData;

import org.lwjgl.opengl.GL13;


import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

@Getter
@RequiredArgsConstructor
public enum ResourceType {

    PNG("png",
            (field, resourceLocation) -> {
                int wrapFormat = GL13.GL_CLAMP_TO_BORDER;
                if(field != null) {
                    PreLoadableTextureWrapFormat annotation = field.getAnnotation(PreLoadableTextureWrapFormat.class);
                    if (annotation != null) {
                        wrapFormat = annotation.value();
                    }
                }
                return TextureLoader.preloadTexture(resourceLocation, wrapFormat);
            },
            (resourceLocation, o) -> TextureLoader.loadTexture(resourceLocation, ((PNGTextureLoader.ImageSimpleData) o))
    ),
    TTF("ttf",
            (field, resourceLocation) -> {
                return null;
            },
            (resourceLocation, object) -> {
                FontContainer font = FontAPI.getFontContainer(resourceLocation, 22);
                GuiDrawUtils.drawStringNoScaleGui(font, "test", 0, 0, 2, 0xffffff);
            }
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
