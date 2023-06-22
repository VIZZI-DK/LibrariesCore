package ru.vizzi.Utils.resouces.xlv;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.vizzi.Utils.eventhandler.RegistryEvent;

@RegistryEvent
@SideOnly(Side.CLIENT)
public class ResourceEvent{

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void event(TickEvent.RenderTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            ResourceManager.updateSync();
        }
    }

}
