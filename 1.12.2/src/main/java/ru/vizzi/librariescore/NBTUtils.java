package ru.vizzi.librariescore;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

/**
 * @author Zloy_GreGan
 */

public class NBTUtils {

    public static NBTTagCompound getNbt(ItemStack stack) {
        if (stack.getTagCompound() == null) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

}
