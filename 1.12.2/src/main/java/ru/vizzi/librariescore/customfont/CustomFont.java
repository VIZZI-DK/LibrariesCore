package ru.vizzi.librariescore.customfont;

import net.minecraft.util.ResourceLocation;

public class CustomFont {

    public final String font;
    public final int size;
    public final ResourceLocation rs;

    private CustomFont(String font, int size, ResourceLocation rs) {
        this.font = font;
        this.size = size;
        this.rs = rs;
    }
}