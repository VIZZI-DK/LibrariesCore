package ru.vizzi.Utils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import ru.vizzi.Utils.CustomFont.CustomFontRenderer;
import ru.vizzi.Utils.eventhandler.EventResize;
import ru.vizzi.Utils.eventhandler.RegistryEvent;
import ru.vizzi.Utils.gui.drawmodule.AnimationHelper;
import ru.vizzi.Utils.gui.drawmodule.ScaleGui;

@RegistryEvent
public class ClientEvent {

    public static int lastWidth = -1;
    public static int lastHeight = -1;

    @SubscribeEvent
    public void event(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            AnimationHelper.updateAnimationSpeed();

        }
    }

    @SubscribeEvent
    public void event(TickEvent.ClientTickEvent event) {
        if(event.phase == TickEvent.Phase.START) {
            CompletableFutureBuilder.SyncQueueHandler.update();
            resize();
        }
    }

    public static void resize(){
        if(Minecraft.getMinecraft().displayWidth!= lastWidth || Minecraft.getMinecraft().displayHeight!= lastHeight || lastHeight == -1 || lastWidth == -1) {
            lastWidth = Minecraft.getMinecraft().displayWidth;
            lastHeight = Minecraft.getMinecraft().displayHeight;
            ScaleGui.update(ScaleGui.FULL_HD);
           // CustomFontRenderer.resize();
            EventResize eventResize = new EventResize(lastWidth, lastHeight);
            MinecraftForge.EVENT_BUS.post(eventResize);
        }
    }

}
