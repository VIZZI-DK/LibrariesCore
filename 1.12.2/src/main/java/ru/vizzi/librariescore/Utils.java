package ru.vizzi.librariescore;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zloy_GreGan
 */

public class Utils {

    public static int getPlayersCount() {
        int count = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().playerEntities.size();
        return count;
    }

    public static int getMaxPlayerCount() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getMaxPlayers();
    }

    public void shutDown() {
        Minecraft.getMinecraft().shutdown();
    }

    public boolean isInGame() {
        return Minecraft.getMinecraft().player != null && Minecraft.getMinecraft().world != null;
    }

    public boolean isGuiOpen(Class<? extends GuiScreen> guiClass) {
        return Minecraft.getMinecraft().currentScreen == null ? false : Minecraft.getMinecraft().currentScreen.getClass() == guiClass;
    }

    public GuiScreen currentScreen() {
        return Minecraft.getMinecraft().currentScreen;
    }

    public int getWindowID() {
        return Minecraft.getMinecraft().player.openContainer.windowId;
    }

    public static String getKeyName(KeyBinding key){
        return Keyboard.getKeyName(key.getKeyCode());
    }

    public static String getWorldName(World world) {
        return world.getWorldInfo().getWorldName();
    }

    public static int getDimension(World world) {
        return world.provider.getDimension();
    }

    public static List<NetworkPlayerInfo> getPlayerInfo() {
        List<NetworkPlayerInfo> playerInfoList = new ArrayList<>();
        playerInfoList.addAll(Minecraft.getMinecraft().getConnection().getPlayerInfoMap());
        return playerInfoList;
    }
    
	private static boolean[] KeyStates = new boolean[256];

	public static boolean checkKey(int key) {
		return (Keyboard.isKeyDown(key) != KeyStates[key] ? (KeyStates[key] = !KeyStates[key]) : false);
	}

	public static boolean checkKeyMouse(int key) {
		return (Mouse.isButtonDown(key) != KeyStates[key] ? (KeyStates[key] = !KeyStates[key]) : false);
	}
}
