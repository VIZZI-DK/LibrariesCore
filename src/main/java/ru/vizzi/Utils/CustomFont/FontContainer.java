//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ru.vizzi.Utils.CustomFont;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.vizzi.Utils.gui.drawmodule.ScaleGui;
@GradleSideOnly(GradleSide.CLIENT)
@Getter
@Setter
public class FontContainer {

    String fontName;
    int fontSize;
    ResourceLocation rs;

    private FontContainer() {

    }

    public FontContainer(String fontType, int fontSize) {
        this(fontType, fontSize, (ResourceLocation)null);
    }

    public FontContainer(String fontType, int fontSize, ResourceLocation resLoc) {
        this.fontName = fontType;
        this.fontSize = fontSize;
        this.rs = resLoc;

    }

    public float height(String text) {
        return TextRenderUtils.getTextHeight(text, this);
        // return this.useCustomFont ? this.textFont.fontHeight : (float)Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
    }

    public float width(String text) {
        return CustomFontRenderer.getStringWidth(this, text);
    }


//    public float drawStringWithShadow(String text, float x, float y, int color) {
//        float l;
//        CustomFontRenderer.drawStringWithMaxWidth();
//            l = this.textFont.renderString(text, x + 1.0F, y + 1.0F, color, true);
//            l = Math.max(l, this.textFont.renderString(text, x, y, color, false));
//
//        return l;
//    }

    public float drawString(String text, float x, float y, int color) {
        TextRenderUtils.drawText(x, y, color, text, this);
        return x;

    }

    public int getScaledSize() {
        return ScaleGui.get(fontSize);
    }

    public String getName() {
        return fontName;
    }

}
