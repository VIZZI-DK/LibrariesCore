package ru.vizzi.Utils.model;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ru.vizzi.Utils.eventhandler.RegistryEvent;

@RegistryEvent
@SideOnly(Side.CLIENT)
public class ModelEvents {

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void event(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            ModelLoader.loadModels();
            FMLCommonHandler.instance().bus().unregister(this);
        }
    }


}
