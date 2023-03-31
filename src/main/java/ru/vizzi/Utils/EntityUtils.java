package ru.vizzi.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;
import java.util.List;

/**
 * @author Zloy_GreGan
 */

public class EntityUtils {

    public static int getDistanceToTheEntity(Entity start, Entity end) {
        return getDistanceEntityToThePlace(start, new Vector3d((float) end.posX, (float) end.posY, (float) end.posZ));
    }

    public static int getDistanceEntityToThePlace(Entity start, Vector3d end) {
        return (int) getDistance(new Vector3d((float) start.posX, (float) start.posY, (float) start.posZ), new Vector3d(end.x, end.y, end.z));
    }

    public static int getDistancePlaceToTheEntity(Vector3d start, Entity end) {
        return (int) getDistance(new Vector3d(start.x, start.y, start.z), new Vector3d((float) end.posX, (float) end.posY, (float) end.posZ));
    }

    public static double getDistance(Vector3d start, Vector3d end) {
        double x = start.x - end.x;
        double y = (start.y - end.y) - 2;
        double z = start.z - end.z;
        return MathHelper.sqrt_double((x * x) + (y * y) + (z * z));
    }

    public static boolean isEntityInside(Entity entity, int dim, double x, double y, double z, double radius) {
        return entity.dimension == dim
                && entity.posX >= x - radius
                && entity.posY >= y - radius
                && entity.posZ >= z - radius
                && entity.posX < x + radius
                && entity.posY < y + radius
                && entity.posZ < z + radius;
    }

    public static String getUUID(Entity entity) {
        return entity.getUniqueID().toString();
    }

    public static int getEntityID(Entity entity) {
        return entity.getEntityId();
    }

    public static String getEntityName(Entity entity) {
        return entity.getCommandSenderName();
    }

    public Entity getEntityByID(World world, int id) {
        return world.getEntityByID(id);
    }

    public Entity getPointedEntity() {
        return Minecraft.getMinecraft().objectMouseOver.entityHit;
    }

    public static Vector3d getEntityPosition(Entity ent) {
        return new Vector3d(ent.posX, ent.posY, ent.posZ);
    }

    public static EntityItem createEntityItem(World world, ItemStack stack, double x, double y, double z, boolean doRandomSpread) {
        EntityItem entityitem;
        if (doRandomSpread) {
            float f1 = 0.7F;
            double d = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
            double d1 = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
            double d2 = (double)(world.rand.nextFloat() * f1) + (double)(1.0F - f1) * 0.5D;
            entityitem = new EntityItem(world, x + d, y + d1, z + d2, stack);
            entityitem.delayBeforeCanPickup = 10;
        } else {
            entityitem = new EntityItem(world, x, y, z, stack);
            entityitem.motionX = 0.0D;
            entityitem.motionY = 0.0D;
            entityitem.motionZ = 0.0D;
            entityitem.delayBeforeCanPickup = 0;
        }

        return entityitem;
    }

    public static void dropItems(World world, ItemStack stack, double x, double y, double z, boolean doRandomSpread) {
        if (stack != null && stack.stackSize > 0) {
            EntityItem entityitem = createEntityItem(world, stack, x, y, z, doRandomSpread);
            world.spawnEntityInWorld(entityitem);
        }
    }

    public static void dropItemsInventory(World world, ItemStack[] inventory, int x, int y, int z, boolean doRandomSpread) {
        if (inventory != null) {
            ItemStack[] var6 = inventory;
            int var7 = inventory.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                ItemStack stack = var6[var8];
                if (stack != null && stack.stackSize > 0) {
                    dropItems(world, stack.copy(), x, y, z, doRandomSpread);
                }
            }
        }
    }

    public static void dropItemsInventory(World world, IInventory inventory, int x, int y, int z, boolean doRandomSpread) {
        for(int l = 0; l < inventory.getSizeInventory(); ++l) {
            ItemStack items = inventory.getStackInSlot(l);
            if (items != null && items.stackSize > 0) {
                dropItems(world, inventory.getStackInSlot(l).copy(), x, y, z, doRandomSpread);
            }
        }

    }

    public static List getNearEntityFromEntity(Class cls, Entity entity, int dis) {
        AxisAlignedBB range = entity.boundingBox.expand(dis, dis, dis);
        List list = entity.worldObj.getEntitiesWithinAABB(cls, range);
        return list;
    }

}
