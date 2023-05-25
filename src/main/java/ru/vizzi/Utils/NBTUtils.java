package ru.vizzi.Utils;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.vizzi.Utils.obf.IgnoreObf;

/**
 * @author Zloy_GreGan
 */
@IgnoreObf
public class NBTUtils {

    public static NBTTagCompound getNbt(ItemStack stack) {
        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }
        return stack.stackTagCompound;
    }

}
