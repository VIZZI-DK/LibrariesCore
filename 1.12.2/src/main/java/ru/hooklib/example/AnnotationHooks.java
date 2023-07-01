package ru.hooklib.example;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import ru.hooklib.asm.Hook;
import ru.hooklib.asm.ReturnCondition;
import ru.vizzi.librariescore.resouces.CoreAPI;

public class AnnotationHooks {


	@Hook(targetMethod = "<init>", returnCondition = ReturnCondition.ALWAYS)
	public static void ScaledResolution(ScaledResolution obj, Minecraft minecraftClient)
    {
        obj.scaledWidth = minecraftClient.displayWidth;
        obj.scaledHeight = minecraftClient.displayHeight;
        obj.scaleFactor = 1;
        boolean flag = minecraftClient.isUnicode();
        int i = minecraftClient.gameSettings.guiScale;

        if (i == 0)
        {
            i = 1000;
        }
        if(CoreAPI.isDefaultScale) {
            while (obj.scaleFactor < i && obj.scaledWidth / (obj.scaleFactor + 1) >= 320 && obj.scaledHeight / (obj.scaleFactor + 1) >= 240) {
                ++obj.scaleFactor;
            }

            if (flag && obj.scaleFactor % 2 != 0 && obj.scaleFactor != 1) {
                --obj.scaleFactor;
            }
        }

        obj.scaledWidthD = (double)obj.scaledWidth / (double)obj.scaleFactor;
        obj.scaledHeightD = (double)obj.scaledHeight / (double)obj.scaleFactor;
        obj.scaledWidth = MathHelper.ceil(obj.scaledWidthD);
        obj.scaledHeight = MathHelper.ceil(obj.scaledHeightD);
    }
      

}
