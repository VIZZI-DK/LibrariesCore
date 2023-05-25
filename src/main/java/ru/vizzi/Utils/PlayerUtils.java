package ru.vizzi.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import ru.vizzi.Utils.obf.IgnoreObf;

/**
 * @author Zloy_GreGan
 */
@IgnoreObf
public class PlayerUtils {

    public static boolean isPlayerOp(EntityPlayer entityPlayer) {
        return MinecraftServer.getServer().getConfigurationManager().func_152596_g(entityPlayer.getGameProfile());
    }

    public static boolean isInCreative(EntityPlayer entityPlayer) {
        return entityPlayer.capabilities.isCreativeMode;
    }

    public void playerDisconnect() {
        NetHandlerPlayClient netHandler = Minecraft.getMinecraft().getNetHandler();
        if (netHandler != null) {
            netHandler.getNetworkManager().closeChannel(new ChatComponentText("Disconnect"));
        }
    }

    public static EntityPlayer getOnlinePlayer(String playerName) {
        return MinecraftServer.getServer().getConfigurationManager().func_152612_a(playerName);
    }

}
