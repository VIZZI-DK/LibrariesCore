package ru.vizzi.Utils.gui.drawmodule;

import net.minecraft.client.Minecraft;
import ru.vizzi.Utils.obf.IgnoreObf;

import net.minecraft.client.Minecraft;

public class ScaleGui {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static float FULL_HD = 16 / 9.0f;
    public static float SXGA = 4 / 3.0f;

    public static int screenCenterX, screenCenterY;
    public static int screenWidth, screenHeight;
    public static float scaleValue;

    public static final float DEFAULT_WIDTH = 1920.0f;
    public static final float HALF_WIDTH = DEFAULT_WIDTH / 2.0f;
    public static final float DEFAULT_HEIGHT = 1080.0f;
    public static final float HALF_HEIGHT = DEFAULT_HEIGHT / 2.0f;

    public static void update() {
        refresh(FULL_HD);
    }

    public static void update(float minAspect) {
        refresh(minAspect);
    }

    private static void refresh(float minAspect) {
        screenWidth = mc.displayWidth;
        screenHeight = mc.displayHeight;
        screenCenterX = screenWidth / 2;
        screenCenterY = screenHeight / 2;
        float ratio = screenWidth / (float) screenHeight;
        scaleValue = ratio < minAspect ? screenHeight / (1.0f + (minAspect - ratio)) : screenHeight;
    }

    public static int get(int value) {
        return (int) (scaleValue / (DEFAULT_HEIGHT / value));
    }

    public static float get(float value) {
        return scaleValue / (DEFAULT_HEIGHT / value);
    }

    public static int get(int value, int size) {
        return (int) (scaleValue / (DEFAULT_HEIGHT / value) - get(size) / 2f);
    }

    public static float get(float value, float size) {
        return scaleValue / (DEFAULT_HEIGHT / value) - get(size) / 2f;
    }

    public static int getCenterX(float value, float size) {
        return (int) (screenCenterX + scaleValue / (DEFAULT_HEIGHT / (value - HALF_WIDTH)) - get(size) / 2f);
    }

    public static int getCenterX(float value) {
        return (int) (screenCenterX + scaleValue / (DEFAULT_HEIGHT / (value - HALF_WIDTH)));
    }

    public static float getCenterXFloat(float value) {
        return (screenCenterX + scaleValue / (DEFAULT_HEIGHT / (value - HALF_WIDTH)));
    }

    public static int getCenterY(float value) {
        return (int) (screenCenterY + scaleValue / (DEFAULT_HEIGHT / (value - HALF_HEIGHT)));
    }

    public static float getCenterYFloat(float value) {
        return (screenCenterY + scaleValue / (DEFAULT_HEIGHT / (value - HALF_HEIGHT)));
    }

    public static int getCenterY(float value, float size) {
        return (int) (screenCenterY + scaleValue / (DEFAULT_HEIGHT / (value - HALF_HEIGHT)) - get(size) / 2f);
    }

    public static int getRight(float value) {
        return (int) (screenWidth + scaleValue / (DEFAULT_HEIGHT / (value - DEFAULT_WIDTH)));
    }

    public static int getRight(float value, float size) {
        return (int) (screenWidth + scaleValue / (DEFAULT_HEIGHT / (value - DEFAULT_WIDTH)) - get(size) / 2f);
    }

    public static int getBot(float value) {
        return (int) (screenHeight - scaleValue / (DEFAULT_HEIGHT / (DEFAULT_HEIGHT - value)));
    }

    public static int getBot(int value) {
        return (int) (screenHeight - scaleValue / (DEFAULT_HEIGHT / (DEFAULT_HEIGHT - value)));
    }

    public static int getBot(float y, float size) {
        return (int) (screenHeight - scaleValue / (DEFAULT_HEIGHT / (DEFAULT_HEIGHT - y)) - get(size) / 2f);
    }
}
