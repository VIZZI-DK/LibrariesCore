package ru.vizzi.Utils.CustomFont;

import lombok.SneakyThrows;
import net.minecraft.util.ResourceLocation;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.vizzi.Utils.gui.drawmodule.ScaleGui;

import java.util.HashMap;
@GradleSideOnly(GradleSide.CLIENT)
public class FontAPI {

    private static HashMap<ResourceLocation, HashMap<Integer, FontContainer>> fontsCache = new HashMap<>();

    @SneakyThrows
    public static FontContainer getFontContainer(ResourceLocation rs, int size){
        if(fontsCache.containsKey(rs)){
            if(fontsCache.get(rs).containsKey(size)){
                return fontsCache.get(rs).get(size);
            } else {
                FontContainer fontContainer = new FontContainer(rs.getResourceDomain(), size, rs);
                fontsCache.get(rs).put(size, fontContainer);
                return fontContainer;
            }
        } else {
            HashMap<Integer, FontContainer> sizeCache = new HashMap<>();
            fontsCache.put(rs, sizeCache);
            return getFontContainer(rs, size);
        }
    }



}
