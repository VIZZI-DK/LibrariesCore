package ru.hooklib.example;

import ru.hooklib.asm.Hook;
import ru.hooklib.asm.ReturnCondition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import ru.vizzi.Utils.resouces.CoreAPI;

public class AnnotationHooks {


	@Hook(targetMethod = "<init>", returnCondition = ReturnCondition.ALWAYS)
	public static void ScaledResolution(ScaledResolution obj, Minecraft p_i1094_1_, int p_i1094_2_, int p_i1094_3_)
    {
        obj.scaledWidth = p_i1094_2_;
        obj.scaledHeight = p_i1094_3_;
        obj.scaleFactor = 1;
        boolean flag = p_i1094_1_.func_152349_b();
        int k = p_i1094_1_.gameSettings.guiScale;

        if (k == 0)
        {
            k = 1000;
        }

        if(CoreAPI.isDefaultScale) {
            while (obj.scaleFactor < k && obj.scaledWidth / (obj.scaleFactor + 1) >= 320 && obj.scaledHeight / (obj.scaleFactor + 1) >= 240)
            {
                ++obj.scaleFactor;
            }

            if (flag && obj.scaleFactor % 2 != 0 && obj.scaleFactor != 1)
            {
                --obj.scaleFactor;
            }
        }

        obj.scaledWidthD = (double)obj.scaledWidth / (double)obj.scaleFactor;
        obj.scaledHeightD = (double)obj.scaledHeight / (double)obj.scaleFactor;
        obj.scaledWidth = MathHelper.ceiling_double_int(obj.scaledWidthD);
        obj.scaledHeight = MathHelper.ceiling_double_int(obj.scaledHeightD);
    }
      

}
