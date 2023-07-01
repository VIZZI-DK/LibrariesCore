package ru.vizzi.librariescore;


import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.vizzi.librariescore.eventhandler.RegistryEvent;

import java.util.ArrayList;


@SideOnly(Side.CLIENT)
@RegistryEvent
public class GuiScreenUtils {
	
	private static ArrayList<GuiScreen> arrayList = new ArrayList<>();
	private static Minecraft mc = Minecraft.getMinecraft();
	
	public static void addScreen(GuiScreen screen) {
		LibrariesCore.logger.debug("GuiScreenUtils: Gui %s add to open list ", screen.getClass());
		arrayList.add(screen);
	}

	@SubscribeEvent
	public void guiCloseEvent(TickEvent.ClientTickEvent e) {
		if(e.phase == TickEvent.Phase.START)
		if(mc.world != null && mc.currentScreen == null) {
			if(!arrayList.isEmpty()) {
				GuiScreen screen = arrayList.remove(0);
				mc.displayGuiScreen(screen);
			}
		}
	}

}
