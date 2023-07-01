package ru.vizzi.librariescore.gui;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
		for (Object button : guiButtonsList) {
			if (button instanceof GuiButtonAdvanced) {
				((GuiButtonAdvanced) button).drawButton(mx, my);
			}

		}
	}

	public void addButton(GuiButtonNew button) {
		guiButtonsList.add(button);
	}

	public void removeButton(GuiButtonNew button) {
		guiButtonsList.remove(button);
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
	@SneakyThrows
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
			for (Object o : guiButtonsList) {
				if (o instanceof GuiButtonNew) {
					GuiButtonNew guibutton = (GuiButtonNew) o;
					if (guibutton.mousePressed(this.mc, mouseX, mouseY)) {
						guibutton.playPressSound(this.mc.getSoundHandler());
						this.actionPerformedNew(guibutton);
						return;
					}
				}
			}
		}
	}
	
	protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        if (this.currentButton != null && state == 0)
        {
            this.currentButton.mouseReleased(mouseX, mouseY);
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
