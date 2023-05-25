package ru.vizzi.Utils.gui;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import ru.vizzi.Utils.obf.IgnoreObf;

@Getter
@Setter
@IgnoreObf
public class GuiExtended extends AbstractGuiScreenAdvanced {

	protected List<GuiModule> modules = new ArrayList<>();
	
	private GuiButtonNew currentButton; 

	private GuiScreen parentScreen;

	public GuiExtended() {
		super(16 / 9f);
	}

	public GuiExtended(GuiScreen parent) {
		this();
		this.parentScreen = parent;
	}

	@Override
	public void initGui() {
		super.initGui();
		currentButton = null;
		modules.clear();
		buttonList.clear();
	}

	public void drawButtons(int mx, int my, float ticks) {
		for (Object button : buttonList) {
			if (button instanceof GuiButtonAdvanced) {
				((GuiButtonAdvanced) button).drawButton(mx, my);
			}

		}
	}

	public void addButton(GuiButton button) {
		buttonList.add(button);
	}

	public void removeButton(GuiButton button) {
		buttonList.remove(button);
	}

	public void drawModules(int mx, int my, float ticks) {
		for (GuiModule module : modules) {
			if (module.isActive()) {

				module.drawScreen(mx, my, ticks);

			}
		}
	}

	public void updateModule() {
		for (GuiModule module : modules) {
			if (module.isActive()) {
				module.updateScreen();
			}
		}
	}

	public void addModule(GuiModule guiModule) {
		guiModule.setWorldAndResolution(mc, width, height);
		modules.add(guiModule);
	}

	public void activateModule(int index) {
		if (index == -1) {
			modules.forEach(module -> module.setActive(false));
//            for (Object button : buttonList) {
//                if(button instanceof Button) {
//                    ((Button) button).setCheckHover(true);
//                }
//            }
			return;
		}

		modules.get(index).setActive(true);
		modules.get(index).setWorldAndResolution(mc, width, height);
		modules.get(index).initGui();
//        for (Object button : buttonList) {
//            if(button instanceof Button) {
//                ((Button) button).setCheckHover(false);
//            }
//        }
	}

	public void activateModule(Object object) {
		if (object == null) {
			modules.forEach(module -> module.setActive(false));
//            for (Object button : buttonList) {
//                if(button instanceof Button) {
//                    ((Button) button).setCheckHover(true);
//                }
//            }
			return;
		}

		for (GuiModule module : modules) {
			module.setActive(false);
			if (module.equals(object)) {
				module.setActive(true);
				module.setWorldAndResolution(mc, width, height);
				module.initGui();
			}
		}

//        for (Object button : buttonList) {
//            if(button instanceof Button) {
//                ((Button) button).setCheckHover(false);
//            }
//        }
	}

	@Override
	public void handleMouseInput() {
		for (GuiModule module : modules) {
			if (module.isActive()) {
				module.handleMouseInput();
				return;
			}
		}
		super.handleMouseInput();
	}

	@Override
	public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (mouseButton == 0) {
			for (GuiModule module : modules) {
				if (module.isActive()) {
					module.mouseClicked(mouseX, mouseY, mouseButton);
					return;
				}
			}
			for (Object o : buttonList) {
				if (o instanceof GuiButton) {
					GuiButton guibutton = (GuiButton) o;
					if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
						guibutton.func_146113_a(this.mc.getSoundHandler());
						this.actionPerformed(guibutton);
						return;
					}
				} else {
					GuiButtonNew guibutton = (GuiButtonNew) o;
					if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
						
	                    this.currentButton = guibutton;
						guibutton.func_146113_a(this.mc.getSoundHandler());
						this.actionPerformedNew(guibutton);
						return;
					}
				}
			}
		}
	}

	@Override
	public void mouseClickMove(int mouseX, int mouseY, int lastButtonClicked, long timeSinceMouseClick) {

	}


	public void mouseMovedOrUp(int p_146286_1_, int p_146286_2_, int p_146286_3_)
    {
        super.mouseMovedOrUp(p_146286_1_, p_146286_2_, p_146286_3_);
        if (this.currentButton != null && p_146286_3_ == 0)
        {
            this.currentButton.mouseReleased(p_146286_1_, p_146286_2_);
            this.currentButton = null;
        }
    }

	protected void actionPerformedNew(GuiButtonNew guibutton) {
		// TODO Auto-generated method stub

	}

	public GuiScreen getParentScreen() {
		return parentScreen;
	}
}
