package ru.vizzi.Utils.toast;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.vizzi.Utils.eventhandler.RegistryEvent;

@RegistryEvent
@GradleSideOnly(GradleSide.CLIENT)
public class ClientEvent {
    @Getter
    private static final GuiToast guiToast = new GuiToast(Minecraft.getMinecraft());

    @Getter
    private static ClientEvent instance;

    public ClientEvent() {
        if(instance!= null) {
            instance = this;
        }
    }

    @SubscribeEvent
    public void onRenderTickEvent(TickEvent.RenderTickEvent event) {
        if (event.phase == TickEvent.Phase.END)
            guiToast.drawToast();
    }


}
