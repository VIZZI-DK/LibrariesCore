package ru.vizzi.Utils.CustomFont;

import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import static org.lwjgl.opengl.GL11.*;

@GradleSideOnly(GradleSide.CLIENT)
public class TextRenderUtils {

    public static <T> void drawText(float posX, float posY, int color, T text, FontContainer font) {
        CustomFontRenderer.drawStringWithMaxWidth(String.valueOf(text), posX, posY, -1, color, font, EnumStringRenderType.DEFAULT);
    }

    public static <T> void drawCenteredText(float posX, float posY, int color, T text, FontContainer font) {
        CustomFontRenderer.drawStringWithMaxWidth(String.valueOf(text), posX - (CustomFontRenderer.getStringWidth(font, String.valueOf(text)) / 2), posY, -1, color, font, EnumStringRenderType.DEFAULT);
    }

    public static <T> void drawSplitText(float posX, float posY, float wrapWidth, int color, T text, FontContainer font, EnumStringRenderType type) {
        CustomFontRenderer.drawStringWithMaxWidth(String.valueOf(text), posX, posY, wrapWidth, color, font, type);
    }

    public static <T> void drawCenteredTextWithAlpha(float posX, float posY, float alpha, int color, T text, FontContainer font) {
        TextRenderUtils.drawTextWithAlpha(posX - (CustomFontRenderer.getStringWidth(font, String.valueOf(text)) / 2), posY, alpha, color, text, font);
    }

    public static <T> void drawTextWithAlpha(float posX, float posY, float alpha, int color, T text, FontContainer font) {
        int alphaF = (int) (234 * alpha);
        if (alphaF > 234) alphaF = 234;
        if (alphaF < 0) alphaF = 0;
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        CustomFontRenderer.drawStringWithMaxWidth(String.valueOf(text), posX, posY, -1, color | alphaF + 22 << 24, font, EnumStringRenderType.DEFAULT);
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    public static <T> float getTextWidth(T text, FontContainer font) {
        return CustomFontRenderer.getStringWidth(font, String.valueOf(text));
    }

    public static <T> float getTextHeight(T text, FontContainer font) {
        return CustomFontRenderer.getStringHeight(font, String.valueOf(text), -1);
    }

    public static <T> float getTextHeight(T text, FontContainer font, int wrapWidth) {
        return CustomFontRenderer.getStringHeight(font, String.valueOf(text), wrapWidth);
    }

}
