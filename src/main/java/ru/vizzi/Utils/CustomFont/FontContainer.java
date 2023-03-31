package ru.vizzi.Utils.CustomFont;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FontContainer {

    private StringCache textFont = null;
    public boolean useCustomFont = true;

    private FontContainer() {}

    public FontContainer(String fontType, int fontSize) {
        this(fontType, fontSize, null);
    }
    
    public FontContainer(String fontType, int fontSize, ResourceLocation resLoc) {
        textFont = new StringCache();
        textFont.setDefaultFont("Arial", fontSize, true);
        useCustomFont = !fontType.equalsIgnoreCase("minecraft");
        try {
            if (!useCustomFont || fontType.isEmpty() || fontType.equalsIgnoreCase("default") || resLoc == null)
            	textFont.setDefaultFont(fontType, fontSize, true);
            else
                textFont.setCustomFont(resLoc, fontSize, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public float height() {
        if (useCustomFont)
            return textFont.fontHeight;
        return Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
    }

    public float width(String text) {
        if (useCustomFont)
            return textFont.getStringWidth(text);
        return Minecraft.getMinecraft().fontRenderer.getStringWidth(text);
    }

    public FontContainer copy() {
        FontContainer font = new FontContainer();
        font.textFont = textFont;
        font.useCustomFont = useCustomFont;
        return font;
    }

    public float drawStringWithShadow(String text, float x, float y, int color) {
        float l;
        if (useCustomFont) {
            l = textFont.renderString(text, x+1, y+1, color, true);
            l = Math.max(l, textFont.renderString(text, x, y, color, false));
        } else {
            l =  Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, (int)x, (int)y, color);
        }

        return l;
    }

    public float drawString(String text, float x, float y, int color) {
        if (useCustomFont) {
            return textFont.renderString(text, x, y, color, false);
        } else {
            return Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(text, (int)x, (int)y, color);
        }
    }

    public String getName() {
        if (!useCustomFont)
            return "Minecraft";
        return textFont.usedFont().getFontName();
    }

    public StringCache getTextFont() {
        return textFont;
    }
}