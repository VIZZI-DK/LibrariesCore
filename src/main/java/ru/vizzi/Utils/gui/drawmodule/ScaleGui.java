package ru.vizzi.Utils.gui.drawmodule;

import net.minecraft.client.Minecraft;

public class ScaleGui {

    private static final Minecraft mc = Minecraft.getMinecraft();
    public static float FULL_HD = 16 / 9f;
    public static float SXGA = 4 / 3f;

    public static float screenCenterX, screenCenterY;
    public static int screenWidth, screenHeight;
    public static float scaleValue;

    public static final float DEFAULT_WIDTH = 1920f;
    public static final float HALF_WIDTH = DEFAULT_WIDTH / 2f;
    public static final float DEFAULT_HEIGHT = 1080f;
    public static final float HALF_HEIGHT = DEFAULT_HEIGHT / 2f;
    
    public static int costil;

    public static void update() {
        refresh(FULL_HD);
    }

    public static void update(float minAspect) {
        refresh(minAspect);
    }

    private static void refresh(float minAspect) {
        screenWidth = mc.displayWidth;
        screenHeight = mc.displayHeight;
        screenCenterX = screenWidth / 2f;
        screenCenterY = screenHeight / 2f;
        if(mc.isFullScreen()) {
        	costil = 0;
        } else {
        	if(screenWidth>854) {
        		costil = 63;
    		} else {
    			costil = 0;
    		}
        }
        
        float ratio =  screenWidth / (float) screenHeight;
        scaleValue = ratio < minAspect ? screenHeight / (1.0f + (minAspect - ratio)) : screenHeight;
    }

    public static float get(float value) {
        return scaleValue / (DEFAULT_HEIGHT / value);
    }
    
    
    

    public static float get(float value, float size) {
        return scaleValue / (DEFAULT_HEIGHT / value) - get(size) / 2f;
    }

    public static float getCenterX(float value, float size) {
        return screenCenterX + scaleValue / (DEFAULT_HEIGHT / (value - HALF_WIDTH)) - get(size) / 2f;
    }

    public static float getCenterX(float value) {
        return screenCenterX + scaleValue / (DEFAULT_HEIGHT / (value - HALF_WIDTH));
    }

    public static float getCenterY(float value) {
        return screenCenterY + scaleValue / (DEFAULT_HEIGHT / (value - HALF_HEIGHT));
    }

    public static float getCenterY(float value, float size) {
        return screenCenterY + scaleValue / (DEFAULT_HEIGHT / (value - HALF_HEIGHT)) - get(size) / 2f;
    }

    public static float getRight(float value) {
        return screenWidth + scaleValue / (DEFAULT_HEIGHT / (value - DEFAULT_WIDTH));
    }

    public static float getRight(float value, float size) {
        return screenWidth + scaleValue / (DEFAULT_HEIGHT / (value - DEFAULT_WIDTH)) - get(size) / 2f;
    }

    public static float getBot(float value) {
        return screenHeight - scaleValue / (DEFAULT_HEIGHT / (DEFAULT_HEIGHT - value));
    }

    public static float getBot(float y, float size) {
        return screenHeight - scaleValue / (DEFAULT_HEIGHT / (DEFAULT_HEIGHT - y)) - get(size) / 2f;
    }
}
