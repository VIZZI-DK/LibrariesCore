package ru.vizzi.librariescore;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import ru.vizzi.librariescore.eventhandler.RegistryEvent;
import ru.vizzi.librariescore.gui.drawmodule.AnimationHelper;

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
