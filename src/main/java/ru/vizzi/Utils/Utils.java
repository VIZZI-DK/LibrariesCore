package ru.vizzi.Utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import ru.vizzi.Utils.obf.IgnoreObf;

import java.util.List;

/**
 * @author Zloy_GreGan
 */
@IgnoreObf
public class Utils {

    public static int getPlayersCount() {
        int count = MinecraftServer.getServer().getEntityWorld().playerEntities.size();
        return count;
    }

    public static int getMaxPlayerCount() {
        return MinecraftServer.getServer().getMaxPlayers();
    }

    public void shutDown() {
        Minecraft.getMinecraft().shutdown();
    }

    public boolean isInGame() {
        return Minecraft.getMinecraft().thePlayer != null && Minecraft.getMinecraft().theWorld != null;
    }

    public boolean isGuiOpen(Class<? extends GuiScreen> guiClass) {
        return Minecraft.getMinecraft().currentScreen == null ? false : Minecraft.getMinecraft().currentScreen.getClass() == guiClass;
    }

    public GuiScreen currentScreen() {
        return Minecraft.getMinecraft().currentScreen;
    }

    public int getWindowID() {
        return Minecraft.getMinecraft().thePlayer.openContainer.windowId;
    }

    public static String getKeyName(KeyBinding key){
        return Keyboard.getKeyName(key.getKeyCode());
    }

    public static String getWorldName(World world) {
        return world.getWorldInfo().getWorldName();
    }

    public static int getDimension(World world) {
        return world.provider.dimensionId;
    }

    public static List<GuiPlayerInfo> getPlayerInfo() {
        List<GuiPlayerInfo> playerInfoList = Minecraft.getMinecraft().getNetHandler().playerInfoList;
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
