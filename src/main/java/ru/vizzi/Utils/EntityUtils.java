package ru.vizzi.Utils;

import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.vizzi.Utils.obf.IgnoreObf;

import javax.vecmath.Vector3d;
import java.util.List;

/**
 * @author Zloy_GreGan
 */
@IgnoreObf
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


    public static Entity teleportEntity(Entity entity, double x, double y, double z, int worldId) {
        boolean changeDim = entity.worldObj.provider.dimensionId != worldId;
        if (changeDim) {
            teleportToDimensionNew(entity, x, y, z, worldId);
        } else {
            Entity mount = entity.ridingEntity;
            if (entity.ridingEntity != null) {
                entity.mountEntity((Entity) null);
                mount = teleportEntity(mount, x, y, z, worldId);
            }

            if (entity instanceof EntityPlayerMP) {
                EntityPlayerMP player = (EntityPlayerMP) entity;
                player.setPositionAndUpdate(x, y, z);
            } else {
                entity.setPosition(x, y, z);
            }

            entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
            if (mount != null) {
                entity.mountEntity(mount);
            }
        }

        return entity;
    }


    private static Entity teleportToDimensionNew(Entity entity, double x, double y, double z, int worldId) {

        Entity mount = entity.ridingEntity;
        if (entity.ridingEntity != null) {
            entity.mountEntity((Entity) null);
            mount = teleportToDimensionNew(mount, x, y, z, worldId);
        }

        // System.out.println("Teleport entity: " + entity.toString());
        y += 0.5D;
        int currentDim = entity.worldObj.provider.dimensionId;
        MinecraftServer minecraftserver = MinecraftServer.getServer();
        WorldServer currentServer = minecraftserver.worldServerForDimension(currentDim);
        WorldServer targetServer = minecraftserver.worldServerForDimension(worldId);
        currentServer.updateEntityWithOptionalForce(entity, false);
        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            player.dimension = worldId;
            player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension,
                    player.worldObj.difficultySetting, player.worldObj.getWorldInfo().getTerrainType(),
                    player.theItemInWorldManager.getGameType()));
            currentServer.removePlayerEntityDangerously(player);
            player.isDead = false;
        } else {
            entity.dimension = worldId;
            entity.isDead = false;
        }

        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
        targetServer.theChunkProviderServer.loadChunk((int) x >> 4, (int) z >> 4);
        targetServer.spawnEntityInWorld(entity);
        targetServer.updateEntityWithOptionalForce(entity, false);
        entity.setWorld(targetServer);
        if (!(entity instanceof EntityPlayerMP)) {
            NBTTagCompound entityNBT = new NBTTagCompound();
            entity.isDead = false;
            entity.writeToNBTOptional(entityNBT);
            entity.isDead = true;
            entity = EntityList.createEntityFromNBT(entityNBT, targetServer);
            if (entity == null) {
                return null;
            }

            entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
            targetServer.spawnEntityInWorld(entity);
            entity.setWorld(targetServer);
            entity.dimension = worldId;
        }

        if (entity instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) entity;
            if (currentServer != null) {
                currentServer.getPlayerManager().removePlayer(player);
            }

            targetServer.getPlayerManager().addPlayer(player);
            targetServer.theChunkProviderServer.loadChunk((int) player.posX >> 4, (int) player.posZ >> 4);
            targetServer.updateEntityWithOptionalForce(entity, false);
            player.playerNetServerHandler.setPlayerLocation(x, y, z, entity.rotationYaw, entity.rotationPitch);
            player.theItemInWorldManager.setWorld(targetServer);
            player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, targetServer);
            player.mcServer.getConfigurationManager().syncPlayerInventory(player);
            FMLCommonHandler.instance().firePlayerChangedDimensionEvent(player, currentDim, worldId);
            player.setPositionAndUpdate(x, y, z);
            player.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
        }

        entity.setLocationAndAngles(x, y, z, entity.rotationYaw, entity.rotationPitch);
        if (mount != null) {
            if (entity instanceof EntityPlayerMP) {
                targetServer.updateEntityWithOptionalForce(entity, true);
            }

            // System.out.println("Mount entity");
            entity.mountEntity(mount);
            targetServer.updateEntities();
            teleportEntity(entity, x, y, z, worldId);
        }

        return entity;
    }

}
