package ru.vizzi.Utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Zloy_GreGan
 **/

public class InventoryUtils {

    public int countItems(Item p_146029_1_, InventoryPlayer inventoryPlayer) {
        int c = 0;
        for (int i = 0; i < inventoryPlayer.mainInventory.length; ++i) {
            if (inventoryPlayer.mainInventory[i] != null && inventoryPlayer.mainInventory[i].getItem() == p_146029_1_) {
                c += inventoryPlayer.mainInventory[i].stackSize;
            }
        }
        return c;
    }
    
    public static ItemStack isPlayerHave(EntityPlayer entityPlayer, String itemUnlocalizedName) {
        for (ItemStack stack : entityPlayer.inventory.mainInventory) {
            if (stack != null && stack.getItem().getUnlocalizedName().equals(itemUnlocalizedName)) {
                return stack;
            }
        }
        return null;
    }

    public static int getInventorySlots(InventoryPlayer inv) {
        int slots = 0;
        for (ItemStack stack : inv.mainInventory) {
            if (stack == null) {
                slots++;
            }
        }
        return slots;
    }
    
    public static boolean removeItemStackFromInventory(IInventory inventory, String targetItemUnlocalizedName, int amount) {
        int count = 0;
        Map<ItemStack, Integer> relevantStacks = new HashMap<ItemStack, Integer>();
        for (int slotIndex = 0; slotIndex < inventory.getSizeInventory(); slotIndex++) {
            ItemStack itemStack = inventory.getStackInSlot(slotIndex);
            if (itemStack != null && itemStack.getUnlocalizedName().equals(targetItemUnlocalizedName)) {
                count += itemStack.stackSize;
                relevantStacks.put(itemStack, slotIndex);
            }
        }

        if (count < amount) {
            return false;
        }

        for (ItemStack itemStack : relevantStacks.keySet()) {
            if (itemStack.stackSize > amount) {
                itemStack.stackSize -= amount;
                return true;
            } else if (itemStack.stackSize == amount) {
                inventory.setInventorySlotContents(relevantStacks.get(itemStack), null);
                return true;
            } else {
                amount -= itemStack.stackSize;
                inventory.setInventorySlotContents(relevantStacks.get(itemStack), null);
            }
        }

        throw new RuntimeException("Unplanned case. This is probably our bug.");
    }
	
}
