package ru.vizzi.Utils.gui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityZombie;
import ru.vizzi.Utils.obf.IgnoreObf;

@IgnoreObf
public class GuiModule extends GuiExtended {

    @Getter
    @Setter
    private float widthTemp, heightTemp, x, y;


    private boolean isActive;
    @Setter
    @Getter
    private boolean focused;

    public GuiModule(GuiScreen parent, float x, float y, float width, float height) {
        super(parent);
        this.widthTemp = width;
        this.heightTemp = height;
        this.x = x;
        this.y = y;
    }

    public GuiModule(float width, float height) {
        this.widthTemp = width;
        this.heightTemp = height;
    }

    @Override
    public void initGui() {
        super.initGui();
        width = mc.displayWidth;
        height = mc.displayHeight;
    }

    public boolean isGuiFocused() {
        boolean foc = focused;
        if (!foc) {
            for (GuiModule guiModule : modules) {
                if (guiModule.isActive() && guiModule.focused) {
                    foc = true;
                    break;
                }
            }
        }
        return foc;
    }

    public boolean isSubModuleActive() {
        for (GuiModule guiModule : modules) {
            if (guiModule.isActive()) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void actionPerformed(GuiButton guiButton) {
        super.actionPerformed(guiButton);
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTick) {


        super.drawScreen(mouseX, mouseY, partialTick);

    }


    @Override
    public void keyTyped(char c, int id) {
        super.keyTyped(c, id);
    }
}
