package ru.vizzi.Utils.CustomFont;

import net.minecraft.util.ResourceLocation;
import ru.vizzi.Utils.LibrariesCore;

public enum FontType {

    FUTURA_PT_MEDIUM(new FontContainer("test", 32, new ResourceLocation(LibrariesCore.MODID, "fonts/Bitter-ExtraBold.ttf")));


    private final FontContainer fontContainer;

    FontType(FontContainer fontContainer) {
        this.fontContainer = fontContainer;
    }

    public FontContainer getFontContainer() {
        return fontContainer;
    }
}
