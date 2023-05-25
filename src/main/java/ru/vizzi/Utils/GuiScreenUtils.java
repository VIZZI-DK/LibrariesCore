package ru.vizzi.Utils;

import java.util.ArrayList;

import lombok.experimental.UtilityClass;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import ru.vizzi.Utils.eventhandler.RegistryEvent;
import ru.vizzi.Utils.obf.IgnoreObf;


@SideOnly(Side.CLIENT)
@RegistryEvent
public class GuiScreenUtils {
	
	private static ArrayList<GuiScreen> arrayList = new ArrayList<>();
	private static Minecraft mc = Minecraft.getMinecraft();
	@IgnoreObf
	public static void addScreen(GuiScreen screen) {
		LibrariesCore.logger.debug("GuiScreenUtils: Gui %s add to open list ", screen.getClass());
		arrayList.add(screen);
	}

	@SubscribeEvent
	public void guiCloseEvent(ClientTickEvent e) {
		if(e.phase == Phase.START)
		if(mc.theWorld != null && mc.currentScreen == null) {
			if(!arrayList.isEmpty()) {
				GuiScreen screen = arrayList.remove(0);
				mc.displayGuiScreen(screen);
			}
		}
	}

}
