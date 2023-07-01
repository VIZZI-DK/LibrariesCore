package ru.vizzi.librariescore.gui.drawmodule;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;

import ru.vizzi.librariescore.customfont.EnumStringRenderType;
import ru.vizzi.librariescore.customfont.FontContainer;
import ru.vizzi.librariescore.customfont.TextRenderUtils;
import ru.vizzi.librariescore.obf.IgnoreObf;

@IgnoreObf
public class TextUtils {

    public static void drawString(FontContainer fontType, String string, float x, float y, int color) {
        drawStringNoScale(fontType, string, ScaleGui.get(x), ScaleGui.get(y), color);
    }
    public static void drawStringNoScaleGui(FontContainer fontType, String string, float x, float y, int color) {
        drawStringNoScale(fontType, string, x, y, color);
    }
    public static void drawCenteredString(FontContainer fontType, String string, float x, float y, int color) {
        FontContainer fontContainer = fontType;
        drawStringNoScale(fontContainer, string, ScaleGui.get(x) - ScaleGui.get(fontContainer.width(string)) / 2f, ScaleGui.get(y), color);
    }

    public static void drawStringNoXYScale(FontContainer fontType, String string, float x, float y, int color) {
        drawStringNoScale(fontType, string, x, y, color);
    }

    public static void drawRightStringNoXYScale(FontContainer fontType, String string, float x, float y, int color) {
        FontContainer fontContainer = fontType;
        drawStringNoScale(fontContainer, string, x - ScaleGui.get(fontContainer.width(string)), y, color);
    }

    public static void drawCenteredStringNoXYScale(FontContainer fontType, String string, float x, float y, int color) {
        FontContainer fontContainer = fontType;
        drawStringNoScale(fontContainer, string, x - ScaleGui.get(fontContainer.width(string)) / 2f, y, color);
    }

    public static void drawCenteredStringCenterX(FontContainer fontContainer, String string, float x, float y, int color) {
        drawStringNoScale(fontContainer, string, ScaleGui.getCenterX(x) - ScaleGui.get(fontContainer.width(string))  / 2f, ScaleGui.get(y), color);
    }

    public static void drawCenteredStringCenter(FontContainer fontContainer, String string, float x, float y, int color) {
        drawStringNoScale(fontContainer, string, ScaleGui.getCenterX(x) - ScaleGui.get(fontContainer.width(string)) / 2f, ScaleGui.getCenterY(y), color);
    }

    public static void drawStringCenter(FontContainer fontType, String string, float x, float y, int color) {
        drawStringNoScale(fontType, string, ScaleGui.getCenterX(x), ScaleGui.getCenterY(y), color);
    }

    public static void drawStringCenterX(FontContainer fontType, String string, float x, float y, float scale, int color) {
        drawStringNoScale(fontType, string, ScaleGui.getCenterX(x), ScaleGui.get(y), color);
    }

    public static void drawStringCenterYRight(FontContainer fontType, String string, float x, float y,int color) {
        drawStringNoScale(fontType, string, ScaleGui.getRight(x), ScaleGui.getCenterY(y), color);
    }

    public static void drawRightStringCenter(FontContainer fontContainer, String string, float x, float y, int color) {
        drawStringNoScale(fontContainer, string, ScaleGui.getCenterX(x) - ScaleGui.get(fontContainer.width(string)), ScaleGui.getCenterY(y), color);
    }

    public static void drawRightString(FontContainer fontContainer, String string, float x, float y, int color) {
        drawStringNoScale(fontContainer, string, ScaleGui.get(x) - ScaleGui.get(fontContainer.width(string)), ScaleGui.get(y), color);
    }

    public static void drawStringCenterXBot(FontContainer fontType, String string, float x, float y, int color) {
        drawStringNoScale(fontType, string, ScaleGui.getCenterX(x), ScaleGui.getBot(y), color);
    }

    public static void drawRightStringCenterXBot(FontContainer fontContainer, String string, float x, float y, int color) {
        drawStringNoScale(fontContainer, string, ScaleGui.getCenterX(x) - ScaleGui.get(fontContainer.width(string)), ScaleGui.getBot(y), color);
    }

    public static void drawRightStringRightBot(FontContainer fontContainer, String string, float x, float y, int color) {
        drawStringNoScale(fontContainer, string, ScaleGui.getRight(x) - ScaleGui.get(fontContainer.width(string)), ScaleGui.getBot(y), color);
    }

    public static float drawSplittedStringCenter(FontContainer font, String text, float x, float y, float width, float heightLimit, int color, EnumStringRenderType renderType) {
        return drawSplittedString(font, text, ScaleGui.getCenterX(x), ScaleGui.getCenterY(y), width, heightLimit, color, renderType);
    }

    public static float drawSplittedStringRightBot(FontContainer font, String text, float x, float y, float width, float heightLimit, int color, EnumStringRenderType renderType) {
        return drawSplittedString(font, text, ScaleGui.getRight(x), ScaleGui.getBot(y), width, heightLimit, color, renderType);
    }

    public static float drawSplittedStringCenterXBot(FontContainer font, String text, float x, float y, float width, float heightLimit, int color) {
        return drawSplittedString(font, text, ScaleGui.getCenterX(x), ScaleGui.getBot(y), width, heightLimit, color, EnumStringRenderType.DEFAULT);
    }

    public static float drawSplittedRightStringCenterXBot(FontContainer font, String text, float x, float y, float width, float heightLimit, int color) {
        return drawSplittedString(font, text, ScaleGui.getCenterX(x), ScaleGui.getBot(y), width, heightLimit, color, EnumStringRenderType.RIGHT);
    }

    public static float drawSplittedStringNoScale(FontContainer font, String text, float x, float y, float width, float heightLimit, int color, EnumStringRenderType type) {
        return drawSplittedString(font, text, x, y, width, heightLimit, color, type);
    }

    private static final List<String> tempList = new ArrayList<>();
    private static final List<String> tempSplitted = new ArrayList<>();

    public static float drawSplittedString(FontContainer fontContainer, String text, float x, float y, float width, float heightLimit, int color, EnumStringRenderType type) {
        //TextRenderUtils.drawSplitText(x,y);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        TextRenderUtils.drawSplitText(0, 0, width, color, text, fontContainer, type);
        GL11.glPopMatrix();
        return 0;
    }


    public static void drawStringNoScale(FontContainer fontContainer, String string, float x, float y, int color) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        // GL11.glScalef(scale, scale, 1.0f);
        fontContainer.drawString(string, 0, 0, color);
        GL11.glPopMatrix();
    }




}
