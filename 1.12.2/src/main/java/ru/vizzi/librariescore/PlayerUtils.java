package ru.vizzi.librariescore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * @author Zloy_GreGan
 */

public class PlayerUtils {

    public static boolean isPlayerOp(EntityPlayer entityPlayer) {
        for(String name : FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayerNames()){
            if(name.equals(entityPlayer.getName())){
                return true;
            }
        }
        return false;
    }

    public static boolean isInCreative(EntityPlayer entityPlayer) {
        return entityPlayer.capabilities.isCreativeMode;
    }

    public void playerDisconnect() {
        NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getConnection();
        if (netHandler != null) {
            netHandler.getNetworkManager().closeChannel(new TextComponentString("Disconnect"));
        }
    }

    public static EntityPlayer getOnlinePlayer(String playerName) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(playerName);
    }

}
