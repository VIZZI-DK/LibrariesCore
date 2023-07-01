package ru.vizzi.librariescore.gui;

import lombok.Getter;
import net.minecraft.client.gui.GuiScreen;

public class GuiModule extends GuiExtended {

	@Getter
    private int widthTemp, heightTemp, x, y;

    private boolean isActive;

    public GuiModule(GuiScreen parent, int x, int y, int width, int height) {
        super(parent);
        this.widthTemp = width;
        this.heightTemp = height;
        this.x = x;
        this.y = y;
    }

    public GuiModule(int width, int height) {
        this.widthTemp = width;
        this.heightTemp = height;
    }

    @Override
    public void initGui() {
        super.initGui();
        width = widthTemp;
        height = heightTemp;
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
}
