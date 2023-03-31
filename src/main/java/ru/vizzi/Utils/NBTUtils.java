package ru.vizzi.Utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Zloy_GreGan
 */

public class NBTUtils {

    public static NBTTagCompound getNbt(ItemStack stack) {
        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }
        return stack.stackTagCompound;
    }

}
