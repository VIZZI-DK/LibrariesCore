package ru.vizzi.Utils.gui.drawmodule;

public class AnimationHelper {
    private static float animSpeed;
    private static double oldTime = 0;

    public static void updateAnimationSpeed() {
        double newTime = System.nanoTime() / 1000000000d;
        double delta = newTime - oldTime;

        if(oldTime != 0) {
            animSpeed = (float) (delta * 40.0);
        }

        oldTime = newTime;
    }

    public static float getAnimationSpeed() {
        return animSpeed;
    }

    public static float updateAnim(float value, float maxValue, float speed) {
        if(value < maxValue) {
            value += animSpeed * speed;
            if(value > maxValue) value = maxValue;
        } else if(value > maxValue) {
            value += animSpeed * speed;
            if(value < maxValue) value = maxValue;
        }

        return value;
    }

    public static float updateSlowEndAnim(float value, float maxValue, float speed, float minSpeed) {
        if(speed > 0) {
            if(value < maxValue) {
                float speed1 = maxValue - value;
                if(speed1 < minSpeed) speed1 = minSpeed;
                value += animSpeed * speed * speed1;
                if(value > maxValue) value = maxValue;
            }
        } else {
            if(value > maxValue) {
                float speed1 = value - maxValue;
                if(speed1 < minSpeed) speed1 = minSpeed;
                value += animSpeed * speed * speed1;
                if(value < maxValue) value = maxValue;
            }
        }

        return value;
    }

    public static float updateSlowStartAnim(float value, float maxValue, float speed, float minSpeed) {
        if(speed > 0) {
            if(value < maxValue) {
                float speed1 = maxValue - (1.0f - value);
                if(speed1 < minSpeed) speed1 = minSpeed;
                value += animSpeed * speed * speed1;
                if(value > maxValue) value = maxValue;
            }
        } else {
            if(value > maxValue) {
                float speed1 = -(1.0f - value);
                if(speed1 < -minSpeed) speed1 = minSpeed;
                value -= animSpeed * speed * speed1;
                if(value < maxValue) value = maxValue;
            }
        }


        return value;
    }

    public static float getSlowAnim(float value, float minValue, float maxValue, float speed, float minSpeed) {
        float halfAnim = (maxValue + minValue) / 2f;
        float percentage = (value - minValue) / (maxValue - minValue);
        float baseSpeed = speed;

        if(value > halfAnim) {
            speed *= (1f - percentage);
        } else {
            speed *= percentage;
        }

        if (baseSpeed > 0) {
            if (speed < minSpeed)
                speed = minSpeed;
        } else {
            if (speed > minSpeed)
                speed = minSpeed;
        }

        value += speed;

        if(value > maxValue)
            value = maxValue;
        else if(value < minValue)
            value = minValue;

        return value;
    }
}