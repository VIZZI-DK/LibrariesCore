package ru.vizzi.Utils;

import cpw.mods.fml.common.FMLCommonHandler;
import lombok.experimental.UtilityClass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

@UtilityClass
public class WorldUtils {

    public  Entity teleportEntity(Entity entity, double x, double y, double z, int worldId) {
        boolean changeDim = entity.worldObj.provider.dimensionId != worldId;
        if (changeDim) {
            teleportToDimensionNew(entity, x, y, z, worldId);
        } else {
            Entity mount = entity.ridingEntity;
            if (entity.ridingEntity != null) {
                entity.mountEntity(null);
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


    public Entity teleportToDimensionNew(Entity entity, double x, double y, double z, int worldId) {

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
            entity.mountEntity(mount);
            targetServer.updateEntities();
            teleportEntity(entity, x, y, z, worldId);
        }

        return entity;
    }


}
