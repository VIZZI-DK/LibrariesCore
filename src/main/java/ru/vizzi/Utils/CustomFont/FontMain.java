package ru.vizzi.Utils.CustomFont;

public class FontMain {

    private static final FontContainer defaultFont;
    static {
        defaultFont = FontType.FUTURA_PT_MEDIUM.getFontContainer();
    }

    public static FontContainer getDefaultFont() {
        return defaultFont;
    }
}
