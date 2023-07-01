//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package ru.vizzi.librariescore.gui.drawmodule;

import java.awt.Color;
import java.awt.Dimension;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import ru.vizzi.librariescore.resouces.CoreAPI;

public class GuiUtils {
    private static Tessellator tessellator = Tessellator.getInstance();
    private static RenderItem itemRenderer = Minecraft.getMinecraft().getRenderItem();

    private static RenderItemNew renderItemNew = new RenderItemNew(itemRenderer);
    private static FontRenderer fontRenderer;
    public static final float DPI = 6.2831855F;

    public GuiUtils() {
    }

    public static void drawRoundedRect(float x, float y, float width, float height, float radius, int color, double aplha) {
        GL11.glPushMatrix();
        drawFilledCircle(x + radius, y + radius, radius, color, aplha);
        drawRect((double)(x + radius), (double)y, (double)(x + width - radius), (double)(y + height), color, aplha);
        drawRect((double)x, (double)(y + radius), (double)(x + radius), (double)(y + height - radius), color, aplha);
        drawFilledCircle(x + radius, y + height - radius, radius, color, aplha);
        drawFilledCircle(x + width - radius, y + radius, radius, color, aplha);
        drawRect((double)(x + width - radius), (double)(y + radius), (double)(x + radius), (double)(y + height - radius), color, aplha);
        drawRect((double)(x + width - radius), (double)(y + radius), (double)(x + width), (double)(y + height - radius), color, aplha);
        drawFilledCircle(x + width - radius, y + height - radius, radius, color, aplha);
        GL11.glPopMatrix();
    }

    public static void drawFilledCircle(float x, float y, float radius, int color, double alpha) {
        float f2 = (float)(color & 255) / 255.0F;
        int triangleAmount = 50;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glBlendFunc(1, 771);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        renderColor(color, alpha);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos((double)x, (double)y, 0.0).endVertex();

        for(int i = 0; i <= triangleAmount; ++i) {
            float angle = (float)((double)(6.2831855F * (float)i / (float)triangleAmount) + Math.toRadians(180.0));
            builder.pos((double)(x + MathHelper.sin(angle) * radius), (double)(y + MathHelper.cos(angle) * radius), 0.0).endVertex();
        }

        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
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

        BufferBuilder builder = tessellator.getBuffer();
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        renderColor(color, alpha);
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(x, height, 0.0).endVertex();
        builder.pos(width, height, 0.0).endVertex();
        builder.pos(width, y, 0.0).endVertex();
        builder.pos(x, y, 0.0).endVertex();
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static <T> void drawCenteredText(double posX, double posY, double scale, int color, T text) {
        drawText(posX - getTextWidth(String.valueOf(text), scale) / 2.0, posY, scale, color, text);
    }

    public static <T> void drawCenteredTextWithAlpha(double posX, double posY, double scale, int color, double alpha, T text) {
        drawTextWithAlpha(posX - getTextWidth(String.valueOf(text), scale) / 2.0, posY, scale, color, alpha, text);
    }

    public static <T> void drawText(double posX, double posY, double scale, int color, T text) {
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        fontRenderer.drawString(String.valueOf(text), (int)(posX / scale), (int)(posY / scale), color);
        GL11.glPopMatrix();
    }

    public static <T> void drawTextWithShadow(double posX, double posY, double scale, int color, T text) {
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        fontRenderer.drawStringWithShadow(String.valueOf(text), (float)((int)(posX / scale)), (float)((int)(posY / scale)), color);
        GL11.glPopMatrix();
    }

    public static <T> void drawSplitText(double posX, double posY, int wrapWidth, double scale, int color, T text) {
        GL11.glPushMatrix();
        GL11.glScaled(scale, scale, scale);
        fontRenderer.drawSplitString(String.valueOf(text), (int)(posX / scale), (int)(posY / scale), wrapWidth, color);
        GL11.glPopMatrix();
    }

    public static <T> void drawTextWithAlpha(double posX, double posY, double scale, int color, double alpha, T text) {
        int alphaF = (int)(234.0 * alpha);
        if (alphaF > 234) {
            alphaF = 234;
        }

        if (alphaF < 0) {
            alphaF = 0;
        }

        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glScaled(scale, scale, scale);
        fontRenderer.drawString(String.valueOf(text), (int)(posX / scale), (int)(posY / scale), color | alphaF + 25 << 24);
        GL11.glDisable(3042);
        GL11.glPopMatrix();
    }

    public static <T> double getTextWidth(T text, double textScale) {
        return (double)((float)fontRenderer.getStringWidth(String.valueOf(text)) * (float)textScale);
    }

    public static double getTextHeight(double textScale) {
        return (double)((float)fontRenderer.FONT_HEIGHT * (float)textScale);
    }

    public static void drawLine(double startX, double startY, double endX, double endY, double lineWidth, int zLevel, int color, double alpha) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glLineWidth((float)lineWidth);
        renderColor(color, alpha);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(1, DefaultVertexFormats.POSITION);
        builder.pos(startX, startY, (double)zLevel).endVertex();
        builder.pos(endX, endY, (double)zLevel).endVertex();
        tessellator.draw();
        renderColor(16777215, 1.0);
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void drawGradientLine(double startX, double startY, double endX, double endY, double lineWidth, int zLevel, int color1, int color2, double alpha, double alpha2) {
        float f1 = (float)(color1 >> 16 & 255) / 255.0F;
        float f2 = (float)(color1 >> 8 & 255) / 255.0F;
        float f3 = (float)(color1 & 255) / 255.0F;
        float f4 = (float)(color2 >> 16 & 255) / 255.0F;
        float f5 = (float)(color2 >> 8 & 255) / 255.0F;
        float f6 = (float)(color2 & 255) / 255.0F;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glLineWidth((float)lineWidth);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(1, DefaultVertexFormats.POSITION);
        builder.color(f1, f2, f3, (float)alpha);
        builder.pos(startX, startY, (double)zLevel).endVertex();
        builder.color(f4, f5, f6, (float)alpha2);
        builder.pos(endX, endY, (double)zLevel).endVertex();
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void drawRectS(double posX, double posY, double endX, double endY, int color, double alpha) {
        GL11.glPushMatrix();
        BufferBuilder builder = tessellator.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        renderColor(color, alpha);
        //GlStateManager.color(1,1,1,100);

        builder.begin(7, DefaultVertexFormats.POSITION);
        builder.pos(posX, posY + endY, 0.0).endVertex();
        builder.pos(posX + endX, posY + endY, 0.0).endVertex();
        builder.pos(posX + endX, posY, 0.0).endVertex();
        builder.pos(posX, posY, 0.0).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
        GL11.glPopMatrix();
    }

    public static void drawRectS(double posX, double posY, double posX1, double posX2, double posY2, double posX3, int color, double alpha) {
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(7425);
        renderColor(color, alpha);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION);
        builder.pos(posX2, posY2, 0.0).endVertex();
        builder.pos(posX3, posY2, 0.0).endVertex();
        builder.pos(posX1, posY, 0.0).endVertex();
        builder.pos(posX, posY, 0.0).endVertex();
        tessellator.draw();
        renderColor(16777215, 1.0);
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void drawGradientRectS(double posX, double posY, double endX, double endY, int color1, int color2, double alpha, double alpha2) {
        float f1 = (float)(color1 >> 16 & 255) / 255.0F;
        float f2 = (float)(color1 >> 8 & 255) / 255.0F;
        float f3 = (float)(color1 & 255) / 255.0F;
        float f4 = (float)(color2 >> 16 & 255) / 255.0F;
        float f5 = (float)(color2 >> 8 & 255) / 255.0F;
        float f6 = (float)(color2 & 255) / 255.0F;
        GL11.glPushMatrix();
        GL11.glDisable(3553);
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(7425);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION);
        builder.color(f1, f2, f3, (float)alpha).endVertex();
        builder.pos(posX, posY + endY, 0.0).endVertex();
        builder.pos(posX + endX, posY + endY, 0.0).endVertex();
        builder.color(f4, f5, f6, (float)alpha2).endVertex();
        builder.pos(posX + endX, posY, 0.0);
        builder.pos(posX, posY, 0.0);
        tessellator.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(3042);
        GL11.glEnable(3008);
        GL11.glEnable(3553);
        GL11.glPopMatrix();
    }

    public static void drawCircle(double posX, double posY, double radius, int color, double alpha) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2881);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3155, 4354);
        renderColor(color, alpha);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(9, DefaultVertexFormats.POSITION);

        for(int i = 0; i <= 360; ++i) {
            double x = Math.sin((double)i * 3.141526 / 180.0) * radius;
            double y = Math.cos((double)i * 3.141526 / 180.0) * radius;
            builder.pos(posX + x, posY + y, 0.0).endVertex();
        }

        tessellator.draw();
        GL11.glDisable(2881);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void drawGradientCircle(double posX, double posY, double radius, int color1, int color2, double alpha) {
        float f = (float)(color1 >> 24 & 255) / 255.0F;
        float f1 = (float)(color1 >> 16 & 255) / 255.0F;
        float f2 = (float)(color1 >> 8 & 255) / 255.0F;
        float f3 = (float)(color2 >> 24 & 255) / 255.0F;
        float f4 = (float)(color2 >> 16 & 255) / 255.0F;
        float f5 = (float)(color2 >> 8 & 255) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2881);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3155, 4354);
        GL11.glShadeModel(7425);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(9, DefaultVertexFormats.POSITION);
        builder.color(f, f1, f2, (float)alpha);
        builder.pos(posX, posY, 0.0);
        builder.color(f3, f4, f5, (float)alpha);

        for(int i = 1; i <= 361; ++i) {
            double x = Math.sin((double)i * 3.141526 / 180.0) * radius;
            double y = Math.cos((double)i * 3.141526 / 180.0) * radius;
            builder.pos(posX + x, posY + y, 0.0).endVertex();
        }

        tessellator.draw();
        GL11.glShadeModel(7424);
        GL11.glDisable(2881);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void drawCircleLine(double posX, double posY, double radius, int color, double alpha) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2881);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3155, 4354);
        renderColor(color, alpha);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(2, DefaultVertexFormats.POSITION);

        for(int i = 0; i <= 360; ++i) {
            double x = Math.sin((double)i * Math.PI / 180.0) * radius;
            double y = Math.cos((double)i * Math.PI / 180.0) * radius;
            builder.pos(posX + x, posY + y, 0.0).endVertex();
        }

        tessellator.draw();
        GL11.glDisable(2881);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void drawWedge(double posX, double posY, double rotation, double radius, double size, int color, double alpha) {
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glEnable(2881);
        GL11.glBlendFunc(770, 771);
        GL11.glHint(3155, 4354);
        GL11.glTranslated(posX, posY, 0.0);
        GL11.glRotated(rotation, 0.0, 0.0, 1.0);
        renderColor(color, alpha);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(9, DefaultVertexFormats.POSITION);
        builder.pos(0.0, 0.0, 0.0).endVertex();

        for(int i = 0; (double)i <= size; ++i) {
            double x = Math.sin((double)i * 3.141526 / 180.0) * radius;
            double y = Math.cos((double)i * 3.141526 / 180.0) * radius;
            builder.pos(x, y, 0.0).endVertex();
        }

        tessellator.draw();
        GL11.glRotated(-rotation, 0.0, 0.0, 1.0);
        GL11.glTranslated(-posX, -posY, 0.0);
        GL11.glDisable(2881);
        GL11.glEnable(3553);
        GL11.glDisable(3042);
    }

    public static void glScissor(int posX, int posY, int endX, int endY, boolean test) {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc);
        int scale = resolution.getScaleFactor();
        int scissorWidth = endX * scale;
        int scissorHeight = endY * scale;
        int scissorX = posX * scale;
        int scissorY = mc.displayHeight - scissorHeight - posY * scale;
        if (test) {
            drawRectS((double)scissorX, (double)scissorY, (double)scissorWidth, (double)scissorHeight, 16777215, 0.3);
        }

        GL11.glEnable(3089);
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }

    public static void glScissorNoScale(int posX, int posY, int endX, int endY, boolean test) {
        if (test) {
            drawRectS((double)posX, (double)posY, (double)endX, (double)endY, 16777215, 0.3);
        }

        GL11.glEnable(3089);
        GL11.glScissor(posX, posY, endX, endY);
    }

    public static void drawItemStackIntoGUI(ItemStack itemstack, int posX, int posY, double scale) {
        if (itemstack != null) {
            GL11.glPushMatrix();
            GL11.glEnable(2896);
            GL11.glScaled(scale, scale, 0.0);
            itemRenderer.renderItemAndEffectIntoGUI(itemstack, (int)((double)posX / scale), (int)((double)posY / scale));
            GL11.glDisable(2896);
            GL11.glPopMatrix();
        }

    }

    public static void drawItemStackIntoGUINew(ItemStack itemstack, float posX, float posY, float scale) {
        if (itemstack != null) {
            GL11.glPushMatrix();
            RenderHelper.enableGUIStandardItemLighting();
            //  GlStateManager.enableRescaleNormal();
            GL11.glTranslated(0, 0, 20.0);
            GL11.glScaled(scale, scale, 2.0);

            renderItemNew.renderItemAndEffectIntoGUI(itemstack, (posX / scale), posY / scale);
            RenderHelper.disableStandardItemLighting();
            //  GlStateManager.disableRescaleNormal();
            GL11.glPopMatrix();

        }

    }

    public static boolean isInBox(double posX, double posY, double endX, double endY, double checkX, double checkY) {
        return checkX >= posX && checkY >= posY && checkX <= posX + endX && checkY <= posY + endY;
    }

    public static void renderColor(int color, double alpha) {
        Color color1 = Color.decode("" + color);
        float red = (float)color1.getRed() / 255.0F;
        float green = (float)color1.getGreen() / 255.0F;
        float blue = (float)color1.getBlue() / 255.0F;
        GL11.glColor4f(red, green, blue, (float)alpha);
    }

    public static int getRGBA(int r, int g, int b, int a) {
        return (r & 255) << 24 | (g & 255) << 16 | (b & 255) << 8 | a & 255;
    }

    public static int getRGB(int r, int g, int b) {
        return (r & 255) << 16 | (g & 255) << 8 | b & 255;
    }

    public static int toHex(int r, int g, int b) {
        int h = 0;
        int hex = h | r << 16;
        hex |= g << 8;
        hex |= b;
        return hex;
    }

    public static double getDistanceAtoB(double startX, double startY, double endX, double endY) {
        double x = startX - endX;
        double y = startY - endY;
        return Math.sqrt(x * x + y * y);
    }

    public static void disableGlScissor() {
        GL11.glDisable(3089);
    }

    public static void drawImage(ResourceLocation image, double posX, double posY, double endX, double endY, double scale) {
        Minecraft.getMinecraft().renderEngine.bindTexture(image);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glEnable(2832);
        GL11.glHint(3153, 4353);
        GL11.glScaled(scale, scale, scale);
        renderColor(16777215, 1.0);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(posX / scale, posY / scale + endY, 0.0).tex(0.0, 1.0).endVertex();
        builder.pos(posX / scale + endX, posY / scale + endY, 0.0).tex(1.0, 1.0).endVertex();
        builder.pos(posX / scale + endX, posY / scale, 0.0).tex(1.0, 0.0).endVertex();
        builder.pos(posX / scale, posY / scale, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDisable(2832);
        GL11.glPopMatrix();
    }

    public static void drawImageNew(ResourceLocation image, double posX, double posY, double endX, double endY, double scale) {
        CoreAPI.bindTexture(image);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glDisable(3008);
        GL11.glEnable(2832);
        GL11.glHint(3153, 4353);
        GL11.glScaled(scale, scale, scale);
        renderColor(16777215, 1.0);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(posX / scale, posY / scale + endY, 0.0).tex(0.0, 1.0).endVertex();
        builder.pos(posX / scale + endX, posY / scale + endY, 0.0).tex(1.0, 1.0).endVertex();
        builder.pos(posX / scale + endX, posY / scale, 0.0).tex(1.0, 0.0).endVertex();
        builder.pos(posX / scale, posY / scale, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDisable(2832);
        GL11.glPopMatrix();
    }

    public static void drawImageColor(ResourceLocation image, double posX, double posY, double endX, double endY, int color, double scale) {
        Minecraft.getMinecraft().renderEngine.bindTexture(image);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2832);
        GL11.glHint(3153, 4353);
        GL11.glScaled(scale, scale, scale);
        renderColor(color, 1.0);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(posX / scale, posY / scale + endY, 0.0).tex(0.0, 1.0).endVertex();
        builder.pos(posX / scale + endX, posY / scale + endY, 0.0).tex(1.0, 1.0).endVertex();
        builder.pos(posX / scale + endX, posY / scale, 0.0).tex(1.0, 0.0).endVertex();
        builder.pos(posX / scale, posY / scale, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDisable(2832);
        GL11.glPopMatrix();
    }

    public static void drawImageColorWithAlpha(ResourceLocation image, double posX, double posY, double endX, double endY, int color, double alpha, double scale) {
        Minecraft.getMinecraft().renderEngine.bindTexture(image);
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glEnable(2832);
        GL11.glHint(3153, 4353);
        GL11.glScaled(scale, scale, scale);
        renderColor(color, alpha);
        BufferBuilder builder = tessellator.getBuffer();
        builder.begin(7, DefaultVertexFormats.POSITION_TEX);
        builder.pos(posX / scale, posY / scale + endY, 0.0).tex(0.0, 1.0).endVertex();
        builder.pos(posX / scale + endX, posY / scale + endY, 0.0).tex(1.0, 1.0).endVertex();
        builder.pos(posX / scale + endX, posY / scale, 0.0).tex(1.0, 0.0).endVertex();
        builder.pos(posX / scale, posY / scale, 0.0).tex(0.0, 0.0).endVertex();
        tessellator.draw();
        GL11.glDisable(3042);
        GL11.glDisable(2832);
        GL11.glPopMatrix();
    }

    private Dimension getTextureSize(ResourceLocation texture) {
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        int width = GL11.glGetTexLevelParameteri(3553, 0, 4096);
        int height = GL11.glGetTexLevelParameteri(3553, 0, 4097);
        GL11.glBindTexture(3553, 0);
        return new Dimension(width, height);
    }

    static {
        fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }
}
