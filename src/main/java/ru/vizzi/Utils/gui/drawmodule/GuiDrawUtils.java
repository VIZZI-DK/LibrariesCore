package ru.vizzi.Utils.gui.drawmodule;

import static org.lwjgl.opengl.GL11.GL_ONE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_ZERO;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import ru.vizzi.Utils.CustomFont.EnumStringRenderType;
import ru.vizzi.Utils.CustomFont.FontContainer;
import ru.vizzi.Utils.CustomFont.StringCache;
import ru.vizzi.Utils.CustomFont.TextRenderUtils;
import ru.vizzi.Utils.obf.IgnoreObf;
import ru.vizzi.Utils.resouces.CoreAPI;
@IgnoreObf
public class GuiDrawUtils {
    public static final float DPI = (float) (2.0F * Math.PI);

    private static final Tessellator tessellator = Tessellator.instance;
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final RenderItem renderItem = RenderItem.getInstance();




    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color,
                                       double aplha) {

        float radiusY = radius;
        float radiusX = radius;
        GL11.glPushMatrix();
        drawFilledCircle(x + radiusX, y + radiusY, radius, color, aplha);

        drawRect(x + radiusX, y, x + width - radiusX, y + height, color, aplha);
        drawRect(x, y + radiusY, x + radiusX, y + height - radiusY, color, aplha);

        drawFilledCircle(x + radiusX, y + height - radiusY, radius, color, aplha);
        drawFilledCircle(x + width - radiusX, y + radiusY, radius, color, aplha);
        drawRect(x + width - radiusX, y + radiusY, x + radiusX, y + height - radiusY, color, aplha);
        drawRect(x + width - radiusX, y + radiusY, x + width, y + height - radiusY, color, aplha);
        drawFilledCircle(x + width - radiusX, y + height - radiusY, radius, color, aplha);
        GL11.glPopMatrix();

    }

    public static void drawRect(ResourceLocation textureLocation, double x, double y, double width, double height,
                                float r, float g, float b, float a) {
        CoreAPI.bindTexture(textureLocation);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(r, g, b, a);
        tessellator.addVertexWithUV(x, y + height, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV(x + width, y + height, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV(x + width, y, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(x, y, 0.0, 0.0, 0.0);
        tessellator.draw();
    }

    public static void drawRect(ResourceLocation textureLocation, double x, double y, double width, double height) {
        CoreAPI.bindTexture(textureLocation);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV(x + width, y + height, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV(x + width, y, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(x, y, 0.0, 0.0, 0.0);
        tessellator.draw();
    }



    public static void drawFilledCircle(float x, float y, float radius, int color, double alpha) {

        float f2 = (float) (color & 255) / 255.0F;
        int triangleAmount = 35 + 15;

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GuiUtils.renderColor(color, alpha);
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawing(GL11.GL_TRIANGLE_FAN);
        tessellator.addVertex(x, y, 0);
        for (int i = 0; i <= triangleAmount; i++) {
            float angle = (float) ((DPI * i / triangleAmount) + Math.toRadians(180));
            tessellator.addVertex(x + MathHelper.sin(angle) * radius, y + MathHelper.cos(angle) * radius, 0);
        }
        tessellator.draw();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        GL11.glPopMatrix();
    }

    private static void drawRect(double x, double y, double width, double height, int color, double alpha) {
        double j1;

        if (x < width) {
            j1 = x;
            x = width;
            width = j1;
        }

        if (y < height) {
            j1 = y;
            y = height;
            height = j1;
        }

        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GuiUtils.renderColor(color, alpha);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double) x, (double) height, 0.0D);
        tessellator.addVertex((double) width, (double) height, 0.0D);
        tessellator.addVertex((double) width, (double) y, 0.0D);
        tessellator.addVertex((double) x, (double) y, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }


    public static void drawString(FontContainer fontType, String string, float x, float y, float scale, int color) {
        drawStringNoScale(fontType, string, ScaleGui.get(x), ScaleGui.get(y), ScaleGui.get(scale), color);
    }
    public static void drawStringNoScaleGui(FontContainer fontType, String string, float x, float y, float scale, int color) {
        drawStringNoScale(fontType, string, x, y, scale, color);
    }
    public static void drawCenteredString(FontContainer fontType, String string, float x, float y, float scale, int color) {
        FontContainer fontContainer = fontType;
        scale = ScaleGui.get(scale);
        drawStringNoScale(fontContainer, string, ScaleGui.get(x) - ScaleGui.get(fontContainer.width(string)) / 2f, ScaleGui.get(y), scale, color);
    }

    public static void drawStringNoXYScale(FontContainer fontType, String string, float x, float y, float scale, int color) {
        scale = ScaleGui.get(scale);
        drawStringNoScale(fontType, string, x, y, scale, color);
    }

    public static void drawRightStringNoXYScale(FontContainer fontType, String string, float x, float y, float scale, int color) {
        FontContainer fontContainer = fontType;
        scale = ScaleGui.get(scale);
        drawStringNoScale(fontContainer, string, x - ScaleGui.get(fontContainer.width(string)), y, scale, color);
    }

    public static void drawCenteredStringNoXYScale(FontContainer fontType, String string, float x, float y, float scale, int color) {
        FontContainer fontContainer = fontType;
        scale = ScaleGui.get(scale);
        drawStringNoScale(fontContainer, string, x - ScaleGui.get(fontContainer.width(string)) / 2f, y, scale, color);
    }

    public static void drawCenteredStringCenterX(FontContainer fontContainer, String string, float x, float y, float scale, int color) {
        scale = ScaleGui.get(scale);
        drawStringNoScale(fontContainer, string, ScaleGui.getCenterX(x) - ScaleGui.get(fontContainer.width(string))  / 2f, ScaleGui.get(y), scale, color);
    }

    public static void drawCenteredStringCenter(FontContainer fontContainer, String string, float x, float y, float scale, int color) {
        scale = ScaleGui.get(scale);
        drawStringNoScale(fontContainer, string, ScaleGui.getCenterX(x) - ScaleGui.get(fontContainer.width(string)) / 2f, ScaleGui.getCenterY(y), scale, color);
    }

    public static void drawStringCenter(FontContainer fontType, String string, float x, float y, float scale, int color) {
        drawStringNoScale(fontType, string, ScaleGui.getCenterX(x), ScaleGui.getCenterY(y), ScaleGui.get(scale), color);
    }

    public static void drawStringCenterX(FontContainer fontType, String string, float x, float y, float scale, int color) {
        drawStringNoScale(fontType, string, ScaleGui.getCenterX(x), ScaleGui.get(y), ScaleGui.get(scale), color);
    }

    public static void drawStringCenterYRight(FontContainer fontType, String string, float x, float y, float scale, int color) {
        drawStringNoScale(fontType, string, ScaleGui.getRight(x), ScaleGui.getCenterY(y), ScaleGui.get(scale), color);
    }

    public static void drawRightStringCenter(FontContainer fontContainer, String string, float x, float y, float scale, int color) {
        scale = ScaleGui.get(scale);
        drawStringNoScale(fontContainer, string, ScaleGui.getCenterX(x) - ScaleGui.get(fontContainer.width(string)), ScaleGui.getCenterY(y), scale, color);
    }

    public static void drawRightString(FontContainer fontContainer, String string, float x, float y, float scale, int color) {
        scale = ScaleGui.get(scale);
        drawStringNoScale(fontContainer, string, ScaleGui.get(x) - ScaleGui.get(fontContainer.width(string)), ScaleGui.get(y), scale, color);
    }

    public static void drawStringCenterXBot(FontContainer fontType, String string, float x, float y, float scale, int color) {
        drawStringNoScale(fontType, string, ScaleGui.getCenterX(x), ScaleGui.getBot(y), ScaleGui.get(scale), color);
    }

    public static void drawRightStringCenterXBot(FontContainer fontContainer, String string, float x, float y, float scale, int color) {
        scale = ScaleGui.get(scale);
        drawStringNoScale(fontContainer, string, ScaleGui.getCenterX(x) - ScaleGui.get(fontContainer.width(string)), ScaleGui.getBot(y), scale, color);
    }

    public static void drawRightStringRightBot(FontContainer fontContainer, String string, float x, float y, float scale, int color) {
        scale = ScaleGui.get(scale);
        drawStringNoScale(fontContainer, string, ScaleGui.getRight(x) - ScaleGui.get(fontContainer.width(string)), ScaleGui.getBot(y), scale, color);
    }

    public static float drawSplittedStringCenter(FontContainer font, String text, float x, float y, float scale, float width, float heightLimit, int color, EnumStringRenderType renderType) {
        return drawSplittedString(font, text, ScaleGui.getCenterX(x), ScaleGui.getCenterY(y), ScaleGui.get(scale), width, heightLimit, color, renderType);
    }

    public static float drawSplittedStringRightBot(FontContainer font, String text, float x, float y, float scale, float width, float heightLimit, int color, EnumStringRenderType renderType) {
        return drawSplittedString(font, text, ScaleGui.getRight(x), ScaleGui.getBot(y), ScaleGui.get(scale), width, heightLimit, color, renderType);
    }

    public static float drawSplittedStringCenterXBot(FontContainer font, String text, float x, float y, float scale, float width, float heightLimit, int color) {
        return drawSplittedString(font, text, ScaleGui.getCenterX(x), ScaleGui.getBot(y), ScaleGui.get(scale), width, heightLimit, color, EnumStringRenderType.DEFAULT);
    }

    public static float drawSplittedRightStringCenterXBot(FontContainer font, String text, float x, float y, float scale, float width, float heightLimit, int color) {
        return drawSplittedString(font, text, ScaleGui.getCenterX(x), ScaleGui.getBot(y), ScaleGui.get(scale), width, heightLimit, color, EnumStringRenderType.RIGHT);
    }

    public static float drawSplittedStringNoScale(FontContainer font, String text, float x, float y, float scale, float width, float heightLimit, int color, EnumStringRenderType type) {
        return drawSplittedString(font, text, x, y, ScaleGui.get(scale), width, heightLimit, color, type);
    }

    private static final List<String> tempList = new ArrayList<>();
    private static final List<String> tempSplitted = new ArrayList<>();

    public static float drawSplittedString(FontContainer fontContainer, String text, float x, float y, float scale, float width, float heightLimit, int color, EnumStringRenderType type) {
        //TextRenderUtils.drawSplitText(x,y);
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        TextRenderUtils.drawSplitText(0, 0, width, color, text, fontContainer, type);
        GL11.glPopMatrix();
        return 0;
    }


    public static void drawStringNoScale(FontContainer fontContainer, String string, float x, float y, float scale, int color) {
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        // GL11.glScalef(scale, scale, 1.0f);
        fontContainer.drawString(string, 0, 0, color);
        GL11.glPopMatrix();
    }

    public static void clearMaskBuffer(float x, float y, float width, float height) {
        glDisable(GL_TEXTURE_2D);
        glColor4f(1f, 1f, 1f, 1f);
        glBlendFuncSeparate(GL_ZERO, GL_ONE, GL_ZERO, GL_ZERO);
        drawRect(x, y, width, height);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_TEXTURE_2D);
    }



    public static void drawRectXY(double x1, double y1, double x2, double y2) {
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x1, y2, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV(x2, y2, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV(x2, y1, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(x1, y1, 0.0, 0.0, 0.0);
        tessellator.draw();
    }

    public static void drawRect(double x, double y, double width, double height, float r, float g, float b, float a) {
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(r, g, b, a);
        tessellator.addVertexWithUV(x, y + height, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV(x + width, y + height, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV(x + width, y, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(x, y, 0.0, 0.0, 0.0);
        tessellator.draw();
    }

    public static void drawRect(double x, double y, double width, double height) {
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + height, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV(x + width, y + height, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV(x + width, y, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(x, y, 0.0, 0.0, 0.0);
        tessellator.draw();
    }


    public static void drawRect(double x, double y, double x1, double y1, double x2, double y2, double x3, double y3, float r, float g, float b, float a) {
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(r, g, b, a);
        tessellator.addVertexWithUV(x, y, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV(x1, y1, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV(x2, y2, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(x3, y3, 0.0, 0.0, 0.0);
        tessellator.draw();
    }

    public static void drawRecLines(float x, float y, float width, float height) {
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x + width, y);
        GL11.glVertex2f(x + width, y + height);
        GL11.glVertex2f(x, y + height);
        GL11.glVertex2f(x, y);
        GL11.glEnd();
    }


}
