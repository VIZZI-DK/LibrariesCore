package ru.vizzi.Utils;

import lombok.experimental.UtilityClass;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@UtilityClass
public class ItemStackFactory {

    @Nullable
    public ItemStack create(@Nonnull String unlocalizedName) {
        return create(unlocalizedName, 1);
    }

    @Nullable
    public ItemStack create(@Nonnull String unlocalizedName, int amount) {
        return create(unlocalizedName, amount, 0);
    }

    @Nullable
    public ItemStack create(@Nonnull String unlocalizedName, int amount, int metadata) {
        return create(unlocalizedName, amount, metadata, null);
    }

    @Nullable
    public ItemStack create(@Nonnull String unlocalizedName, int amount, int metadata, @Nullable NBTTagCompound jsonNbt) {
        Item item = getItemByUnlocalizedName(unlocalizedName);
        if (item == null) {
            return null;
        }
        return create(item, amount, metadata, jsonNbt);
    }

    @Nonnull
    public ItemStack create(@Nonnull Item item, int amount, int metadata, @Nullable NBTTagCompound jsonNbt) {
        if(amount <= 0) {
            amount = 1;
        }
        ItemStack itemStack = new ItemStack(item, amount, metadata);
        if (jsonNbt != null && !jsonNbt.equals("")) {
            itemStack.setTagCompound(jsonNbt);
        }
        return itemStack;
    }
    
    public Item getItemByUnlocalizedName(String unlocalizedName) {
        Object object = Item.itemRegistry.getObject(unlocalizedName);
        return object instanceof Item ? (Item) object : null;
    }
}
