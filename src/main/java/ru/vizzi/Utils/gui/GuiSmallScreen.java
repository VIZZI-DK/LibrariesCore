package ru.vizzi.Utils.gui;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.util.ArrayList;
import java.util.List;

@GradleSideOnly(GradleSide.CLIENT)
@Getter
public abstract class GuiSmallScreen {


    public Minecraft mc = Minecraft.getMinecraft();

    private float xBase, yBase;
    public float xPosition, yPosition, width, height;

    public List<GuiButtonNew> buttonList = new ArrayList();
    public GuiScreen parentScreen;


    public GuiSmallScreen(float x, float y, float w, float h) {
        this.xBase = x;
        this.yBase = y;
        this.xPosition = x;
        this.yPosition = y;
        this.width = w;
        this.height = h;

    }


    public void initGui() {
        buttonList.clear();
    }


    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if(mouseX >= this.xPosition &&  mouseY >= this.yPosition && mouseX < this.xPosition + this.width &&  mouseY < this.yPosition + this.height) {
            for (GuiButtonNew o : buttonList) {
                GuiButtonNew guibutton = (GuiButtonNew) o;
                if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
                    if (guibutton instanceof GuiButtonAdvanced) {
                        GuiButtonAdvanced advanced = (GuiButtonAdvanced) guibutton;
                        if (advanced.onPress != null) {
                            advanced.onPress.onPress(advanced);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void updateScreen() {

    }


    public void drawScreen(int x, int y, float r) {

    }

    public void drawButtons(int mx, int my, float ticks) {
        for (GuiButtonNew button : buttonList) {
            button.drawButton(mc, mx, my);
        }
    }

    protected void actionPerformed(GuiButtonNew guibutton) {
        // TODO Auto-generated method stub

    }

}
