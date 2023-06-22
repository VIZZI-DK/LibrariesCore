package ru.vizzi.Utils.gui.drawmodule;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import ru.vizzi.Utils.obf.IgnoreObf;
import ru.vizzi.Utils.resouces.CoreAPI;
import ru.vizzi.Utils.resouces.PreLoadableResource;

import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;


@IgnoreObf
public class GuiUtils {
    private static Tessellator tessellator;

    private static RenderItem itemRenderer;
    private static RenderItemNew itemRendererNew;
    private static FontRenderer fontRenderer;


    public static <T> void drawCenteredText(double posX, double posY, double scale, int color, T text) {
        GuiUtils.drawText(posX - (GuiUtils.getTextWidth(String.valueOf(text), scale) / 2), posY, scale, color, text);
    }

    public static <T> void drawCenteredTextWithAlpha(double posX, double posY, double scale, int color, double alpha, T text) {
        GuiUtils.drawTextWithAlpha(posX - (GuiUtils.getTextWidth(String.valueOf(text), scale) / 2), posY, scale, color, alpha, text);
    }

    public static <T> void drawText(double posX, double posY, double scale, int color, T text) {
        glPushMatrix();
        glScaled(scale, scale, scale);
        fontRenderer.drawString(String.valueOf(text), (int) (posX / scale), (int) (posY / scale), color);
        glPopMatrix();
    }

    public static <T> void drawTextWithShadow(double posX, double posY, double scale, int color, T text) {
        glPushMatrix();
        glScaled(scale, scale, scale);
        fontRenderer.drawStringWithShadow(String.valueOf(text), (int) (posX / scale), (int) (posY / scale), color);
        glPopMatrix();
    }

    public static <T> void drawSplitText(double posX, double posY, int wrapWidth, double scale, int color, T text) {
        glPushMatrix();
        glScaled(scale, scale, scale);
        fontRenderer.drawSplitString(String.valueOf(text), (int) (posX / scale), (int) (posY / scale), wrapWidth, color);
        glPopMatrix();
    }

    public static <T> void drawTextWithAlpha(double posX, double posY, double scale, int color, double alpha, T text) {
        int alphaF = (int) (234 * alpha);
        if (alphaF > 234) alphaF = 234;
        if (alphaF < 0) alphaF = 0;
        glPushMatrix();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glScaled(scale, scale, scale);
        fontRenderer.drawString(String.valueOf(text), (int) (posX / scale), (int) (posY / scale), color | alphaF + 25 << 24);
        glDisable(GL_BLEND);
        glPopMatrix();
    }

    public static <T> double getTextWidth(T text, double textScale) {
        return fontRenderer.getStringWidth(String.valueOf(text)) * (float)textScale;
    }

    public static double getTextHeight(double textScale) {
        return fontRenderer.FONT_HEIGHT * (float)textScale;
    }

    public static void drawLine(double startX, double startY, double endX, double endY, double lineWidth, int zLevel, int color, double alpha) {
        glPushMatrix();
        glDisable(3553);
        glEnable(3042);
        glLineWidth((float) lineWidth);
        GuiUtils.renderColor(color, alpha);
        tessellator.startDrawing(1);
        tessellator.addVertex(startX , startY, zLevel);
        tessellator.addVertex(endX, endY, zLevel);
        tessellator.draw();
        GuiUtils.renderColor(0xFFFFFF, 1);
        glDisable(3042);
        glEnable(3553);
        glPopMatrix();
    }

    public static void drawGradientLine(double startX, double startY, double endX, double endY, double lineWidth, int zLevel, int color1, int color2, double alpha, double alpha2) {
        float f1 = (float)(color1 >> 16 & 255) / 255.0f;
        float f2 = (float)(color1 >> 8 & 255) / 255.0f;
        float f3 = (float)(color1 & 255) / 255.0f;
        float f4 = (float)(color2 >> 16 & 255) / 255.0f;
        float f5 = (float)(color2 >> 8 & 255) / 255.0f;
        float f6 = (float)(color2 & 255) / 255.0f;
        glPushMatrix();
        glDisable(3553);
        glEnable(3042);
        glLineWidth((float) lineWidth);
        tessellator.startDrawing(1);
        tessellator.setColorRGBA_F(f1, f2, f3, (float)alpha);
        tessellator.addVertex(startX, startY, zLevel);
        tessellator.setColorRGBA_F(f4, f5, f6, (float)alpha2);
        tessellator.addVertex(endX, endY, zLevel);
        tessellator.draw();
        glDisable(3042);
        glEnable(3553);
        glPopMatrix();
    }
    
    

    public static void drawRectS(double posX, double posY, double endX, double endY, int color, double alpha) {
        glPushMatrix();
        glDisable(3553);
        glEnable(3042);
        glDisable(3008);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        glShadeModel(7425);
        GuiUtils.renderColor(color, alpha);
        tessellator.startDrawingQuads();
        tessellator.addVertex(posX, posY + endY, 0.0);
        tessellator.addVertex(posX + endX, posY + endY, 0.0);
        tessellator.addVertex(posX + endX, posY, 0.0);
        tessellator.addVertex(posX, posY, 0.0);
        tessellator.draw();
        GuiUtils.renderColor(0xFFFFFF, 1);
        glShadeModel(7424);
        glDisable(3042);
        glEnable(3008);
        glEnable(3553);
        glPopMatrix();
    }
    
    
    public static void drawRectS(double posX, double posY, double posX1, double posX2, double posY2, double posX3, int color, double alpha) {
        glPushMatrix();
        glDisable(3553);
        glEnable(3042);
        glDisable(3008);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        glShadeModel(7425);
        GuiUtils.renderColor(color, alpha);
        tessellator.startDrawingQuads();
        tessellator.addVertex(posX2, posY2, 0.0);
        tessellator.addVertex(posX3, posY2, 0.0);
        tessellator.addVertex(posX1, posY, 0.0);
        tessellator.addVertex(posX, posY, 0.0);


        tessellator.draw();
        GuiUtils.renderColor(0xFFFFFF, 1);
        glShadeModel(7424);
        glDisable(3042);
        glEnable(3008);
        glEnable(3553);
        glPopMatrix();
    }
    


    public static void drawGradientRectS(double posX, double posY, double endX, double endY, int color1, int color2, double alpha, double alpha2) {
        float f1 = (float)(color1 >> 16 & 255) / 255.0f;
        float f2 = (float)(color1 >> 8 & 255) / 255.0f;
        float f3 = (float)(color1 & 255) / 255.0f;
        float f4 = (float)(color2 >> 16 & 255) / 255.0f;
        float f5 = (float)(color2 >> 8 & 255) / 255.0f;
        float f6 = (float)(color2 & 255) / 255.0f;
        glPushMatrix();
        glDisable(3553);
        glEnable(3042);
        glDisable(3008);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        glShadeModel(7425);
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, (float)alpha);
        tessellator.addVertex(posX, posY + endY, 0.0);
        tessellator.addVertex(posX + endX, posY + endY, 0.0);
        tessellator.setColorRGBA_F(f4, f5, f6, (float) alpha2);
        tessellator.addVertex(posX + endX, posY, 0.0);
        tessellator.addVertex(posX, posY, 0.0);
        tessellator.draw();
        glShadeModel(7424);
        glDisable(3042);
        glEnable(3008);
        glEnable(3553);
        glPopMatrix();
    }

    public static void drawCircle(double posX, double posY, double radius, int color, double alpha) {
        glEnable(3042);
        glDisable(3553);
        glEnable(2881);
        glBlendFunc(770, 771);
        glHint(3155, 4354);
        GuiUtils.renderColor(color, alpha);
        tessellator.startDrawing(9);

        for(int i = 0; i <= 360; ++i) {
            double x = Math.sin((double)i * 3.141526D / 180.0D) * radius;
            double y = Math.cos((double)i * 3.141526D / 180.0D) * radius;
            tessellator.addVertex(posX + x, posY + y, 0.0D);
        }

        tessellator.draw();
        glDisable(2881);
        glEnable(3553);
        glDisable(3042);
    }

    public static void drawGradientCircle(double posX, double posY, double radius, int color1, int color2, double alpha) {
        float f = (float)(color1 >> 24 & 255) / 255.0F;
        float f1 = (float)(color1 >> 16 & 255) / 255.0F;
        float f2 = (float)(color1 >> 8 & 255) / 255.0F;
        float f3 = (float)(color2 >> 24 & 255) / 255.0F;
        float f4 = (float)(color2 >> 16 & 255) / 255.0F;
        float f5 = (float)(color2 >> 8 & 255) / 255.0F;
        glEnable(3042);
        glDisable(3553);
        glEnable(2881);
        glBlendFunc(770, 771);
        glHint(3155, 4354);
        glShadeModel(7425);
        tessellator.startDrawing(9);
        tessellator.setColorRGBA_F(f, f1, f2, (float) alpha);
        tessellator.addVertex(posX, posY, 0.0D);
        tessellator.setColorRGBA_F(f3, f4, f5, (float) alpha);

        for(int i = 1; i <= 361; ++i) {
            double x = Math.sin((double)i * 3.141526D / 180.0D) * radius;
            double y = Math.cos((double)i * 3.141526D / 180.0D) * radius;
            tessellator.addVertex(posX + x, posY + y, 0.0D);
        }

        tessellator.draw();
        glShadeModel(7424);
        glDisable(2881);
        glEnable(3553);
        glDisable(3042);
    }

    public static void drawCircleLine(double posX, double posY, double radius, int color, double alpha) {
        glEnable(3042);
        glDisable(3553);
        glEnable(2881);
        glBlendFunc(770, 771);
        glHint(3155, 4354);
        GuiUtils.renderColor(color, alpha);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(2);

        for(int i = 0; i <= 360; ++i) {
            double x = Math.sin(i * Math.PI / 180.0D) * radius;
            double y = Math.cos(i * Math.PI / 180.0D) * radius;
            tessellator.addVertex(posX + x, posY + y, 0.0D);
        }

        tessellator.draw();
        glDisable(2881);
        glEnable(3553);
        glDisable(3042);
    }

    public static void drawWedge(double posX, double posY, double rotation, double radius, double size, int color, double alpha) {
        glEnable(3042);
        glDisable(3553);
        glEnable(2881);
        glBlendFunc(770, 771);
        glHint(3155, 4354);
        glTranslated(posX, posY, 0.0D);
        glRotated(rotation, 0.0D, 0.0D, 1.0D);
        GuiUtils.renderColor(color, alpha);
        tessellator.startDrawing(9);
        tessellator.addVertex(0.0D, 0.0D, 0.0D);

        for(int i = 0; (double)i <= size; ++i) {
            double x = Math.sin((double)i * 3.141526D / 180.0D) * radius;
            double y = Math.cos((double)i * 3.141526D / 180.0D) * radius;
            tessellator.addVertex(x, y, 0.0D);
        }

        tessellator.draw();
        glRotated(-rotation, 0.0D, 0.0D, 1.0D);
        glTranslated(-posX, -posY, 0.0D);
        glDisable(2881);
        glEnable(3553);
        glDisable(3042);
    }

    public static void glScissor(int posX, int posY, int endX, int endY, boolean test){
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution resolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int scale = resolution.getScaleFactor();



        int scissorWidth = endX * scale;
        int scissorHeight = endY * scale;
        int scissorX = posX * scale;
        int scissorY = mc.displayHeight - scissorHeight - (posY * scale);
        if (test) GuiUtils.drawRectS(scissorX, scissorY, scissorWidth, scissorHeight, 0xFFFFFF, 0.3);
        glEnable(3089);
        GL11.glScissor(scissorX, scissorY, scissorWidth, scissorHeight);
    }
    public static void glScissorNoScale(int posX, int posY, int endX, int endY, boolean test){
        if (test) GuiUtils.drawRectS(posX, posY, endX, endY, 0xFFFFFF, 0.3);
        glEnable(3089);
        GL11.glScissor(posX, posY, endX, endY);
    }

    public static void drawItemStackIntoGUI(ItemStack itemstack, int posX, int posY, double scale) {
        if (itemstack != null) {
            glPushMatrix();
            glEnable(2896);
            glScaled(scale, scale, 0);
            itemRenderer.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemstack, (int) (posX / scale), (int) (posY / scale));
            glDisable(2896);
            glPopMatrix();
            
        }
    }
    public static void drawItemStackIntoGUINew(ItemStack itemstack, float posX, float posY, double scale) {
        if (itemstack != null) {
            GL11.glPushMatrix();
            GL11.glEnable((int) 3042);
            OpenGlHelper.glBlendFunc((int) 770, (int) 771, (int) 1, (int) 0);
            GL11.glDisable((int) 3008);
            GL11.glPushMatrix();
            GL11.glEnable((int) 2929);
            GL11.glTranslated(0, 0, (double) 20.0);
            //GL11.glScaled((double) 0.0625, (double) 0.0625, (double) 1.0);

            GL11.glScaled((double) scale, scale, (double) 2.0);
            GL11.glDisable((int) 2896);
            itemRendererNew.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer,
                    Minecraft.getMinecraft().getTextureManager(), itemstack, (float) (posX / scale),
                    (float) (posY / scale), false);
            GL11.glEnable((int) 2896);
            GL11.glDisable((int) 2929);
            GL11.glEnable((int) 3042);
            GL11.glAlphaFunc((int) 516, (float) 0.0f);
            GL11.glPopMatrix();
            GL11.glDisable((int) 2896);
            GL11.glEnable((int) 3042);
            GL11.glColor4f((float) 1.0f, (float) 1.0f, (float) 1.0f, (float) 1.0f);
            GL11.glDisable((int) 2896);
            GL11.glBlendFunc((int) 770, (int) 771);
            GL11.glEnable((int) 3008);
            GL11.glPopMatrix();
            
        }
    }

    public static boolean isInBox(double posX, double posY, double endX, double endY, double checkX, double checkY) {
        return checkX >= posX && checkY >= posY && checkX <= posX + endX && checkY <= posY + endY;
    }

    public static void renderColor(int color, double alpha) {
        Color color1 = Color.decode((new StringBuilder()).append("").append(color).toString());
        float red = (float)color1.getRed() / 255F;
        float green = (float)color1.getGreen() / 255F;
        float blue = (float)color1.getBlue() / 255F;
        glColor4f(red, green, blue, (float)alpha);
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
        return Math.sqrt((x * x) + (y * y));
    }

    public static void disableGlScissor() {
        GL11.glDisable(3089);
    }

    public static void drawImage(ResourceLocation image, double posX, double posY, double endX, double endY, double scale) {
        Minecraft.getMinecraft().renderEngine.bindTexture(image);
        glPushMatrix();
       
        glEnable(GL11.GL_BLEND);
        glDisable(GL11.GL_ALPHA_TEST);
        glEnable(2832);
        glHint(3153, 4353);
        glScaled(scale, scale, scale);
        GuiUtils.renderColor(0xFFFFFF, 1);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(posX / scale, (posY / scale) + endY, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV((posX / scale) + endX, (posY / scale) + endY, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV((posX / scale) + endX, posY / scale, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(posX / scale, posY / scale, 0.0, 0.0, 0.0);
        tessellator.draw();
        glDisable(GL11.GL_BLEND);
        glDisable(2832);
       glEnable(GL11.GL_ALPHA_TEST);
        glPopMatrix();
    }
    
    public static void drawImageNew(ResourceLocation image, double posX, double posY, double endX, double endY, double scale) {
        CoreAPI.bindTexture(image);
        glPushMatrix();
       
        glEnable(GL11.GL_BLEND);
        glDisable(GL11.GL_ALPHA_TEST);
        glEnable(2832);
        glHint(3153, 4353);
        glScaled(scale, scale, scale);
        GuiUtils.renderColor(0xFFFFFF, 1);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(posX / scale, (posY / scale) + endY, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV((posX / scale) + endX, (posY / scale) + endY, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV((posX / scale) + endX, posY / scale, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(posX / scale, posY / scale, 0.0, 0.0, 0.0);
        tessellator.draw();
        glDisable(GL11.GL_BLEND);
        glDisable(2832);
       glEnable(GL11.GL_ALPHA_TEST);
        glPopMatrix();
    }
    
    
    public static void drawImageNew(ResourceLocation textureLocation, double x, double y, double width, double height) {
    	CoreAPI.bindTexture(textureLocation);
        tessellator.startDrawingQuads();
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
    
    

    public static void drawImageColor(ResourceLocation image, double posX, double posY, double endX, double endY, int color, double scale) {
        Minecraft.getMinecraft().renderEngine.bindTexture(image);
        glPushMatrix();
        glEnable(3042);
        glEnable(2832);
        glHint(3153, 4353);
        glScaled(scale, scale, scale);
        GuiUtils.renderColor(color, 1);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(posX / scale, (posY / scale) + endY, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV((posX / scale) + endX, (posY / scale) + endY, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV((posX / scale) + endX, posY / scale, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(posX / scale, posY / scale, 0.0, 0.0, 0.0);
        tessellator.draw();
        glDisable(3042);
        glDisable(2832);
        glPopMatrix();
    }

    public static void drawImageColorWithAlpha(ResourceLocation image, double posX, double posY, double endX, double endY, int color, double alpha, double scale) {
        Minecraft.getMinecraft().renderEngine.bindTexture(image);
        glPushMatrix();
        glEnable(3042);
        glEnable(2832);
        glHint(3153, 4353);
        glScaled(scale, scale, scale);
        renderColor(color, alpha);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(posX / scale, (posY / scale) + endY, 0.0, 0.0, 1.0);
        tessellator.addVertexWithUV((posX / scale) + endX, (posY / scale) + endY, 0.0, 1.0, 1.0);
        tessellator.addVertexWithUV((posX / scale) + endX, posY / scale, 0.0, 1.0, 0.0);
        tessellator.addVertexWithUV(posX / scale, posY / scale, 0.0, 0.0, 0.0);
        tessellator.draw();
        glDisable(3042);
        glDisable(2832);
        glPopMatrix();
    }

    private Dimension getTextureSize(ResourceLocation texture){
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
        int width = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
        int height = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        return new Dimension(width, height);
    }

    static {
        tessellator = Tessellator.instance;
        itemRenderer = new RenderItem();
        itemRendererNew = new RenderItemNew();
        fontRenderer = Minecraft.getMinecraft().fontRenderer;
    }

}