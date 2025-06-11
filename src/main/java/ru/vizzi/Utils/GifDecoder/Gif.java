package ru.vizzi.Utils.GifDecoder;

import net.minecraft.client.renderer.Tessellator;
import org.lwjgl.opengl.GL11;
import ru.vizzi.Utils.gui.drawmodule.GuiUtils;
import ru.vizzi.Utils.resouces.DynamicTextureNew;

import java.awt.image.BufferedImage;
import java.util.HashMap;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.glPopMatrix;

public class Gif {

    String name;
    GifDecoder gifDecoder;
    public DynamicTextureNew[] textures;
    public int currentFrame = 0;
    public long lastFrameTime = System.currentTimeMillis();
    public HashMap<Integer, Integer> delay = new HashMap<>();
    private boolean load;

    public Gif(String name) {
        this.name = name;
    }


    public void updateAnimation() {
        if (gifDecoder != null && load) {
            long currentTime = System.currentTimeMillis();
            int frameDelay = delay.get(currentFrame); // Получаем задержку текущего кадра

            if (currentTime - lastFrameTime >= frameDelay) {
                currentFrame = (currentFrame + 1) % gifDecoder.getFrameCount(); // Переход на следующий кадр
                lastFrameTime = currentTime; // Сброс времени
            }
        }
    }
    public void bind(){
        if(load){
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textures[currentFrame].getGlTextureId());
        }
    }

    public void drawGif(double posX, double posY, double endX, double endY, double scale){
        if(load){
            updateAnimation();
            drawImage(textures[currentFrame], posX, posY, endX, endY, scale);
        }
    }

    public void drawImage(DynamicTextureNew image, double posX, double posY, double endX, double endY, double scale) {

        glPushMatrix();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, image.getGlTextureId());
        glEnable(GL11.GL_BLEND);
        glDisable(GL11.GL_ALPHA_TEST);
        glEnable(2832);
        glHint(3153, 4353);
        glScaled(scale, scale, scale);
        GuiUtils.renderColor(0xFFFFFF, 1);
        Tessellator tessellator = Tessellator.instance;
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

    public void load() {
        if (load) return;

        GifDecoder decoderGif = new GifDecoder();
        decoderGif.read(name);

        int frameCount = decoderGif.getFrameCount();

        BufferedImage[] frames = new BufferedImage[frameCount];

        for (int i = 0; i < frameCount; i++) {
            frames[i] = decoderGif.getFrame(i);
        }

        textures = new DynamicTextureNew[frameCount];

        for (int i = 0; i < frameCount; i++) {
            textures[i] = new DynamicTextureNew(frames[i]);
            delay.put(i, decoderGif.getDelay(i));
        }
        gifDecoder = decoderGif;
        load = true;


    }


}
