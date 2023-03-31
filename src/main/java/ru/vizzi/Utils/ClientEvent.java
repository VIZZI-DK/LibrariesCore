package ru.vizzi.Utils;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import ru.vizzi.Utils.eventhandler.RegistryEvent;
import ru.vizzi.Utils.gui.drawmodule.AnimationHelper;

@RegistryEvent
public class ClientEvent {

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

        }
    }

}
