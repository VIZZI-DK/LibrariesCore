package ru.vizzi.librariescore.gui;

import ru.vizzi.librariescore.gui.drawmodule.GuiUtils;
import ru.vizzi.librariescore.gui.drawmodule.TextUtils;

public class GuiButtonAdvancedImage extends GuiButtonAdvanced {
    private float xImage;
    private float yImage;
    private float wImage;
    private float hImage;
    private float xText;

    public GuiButtonAdvancedImage(int id, float x, float y, float width, float height, String text, float xImage, float yImage, float wImage, float hImage, float xText) {
        super(id, x, y, width, height, text);
        this.xImage = xImage;
        this.yImage = yImage;
        this.wImage = wImage;
        this.hImage = hImage;
        this.xText = xText;
    }

    public void drawButton(int mouseX, int mouseY) {
		if (visible) {
			boolean hovered = isHovered(mouseX, mouseY);
			if (hovered && !this.hovered) {
				// SoundUtils.playGuiSound(SoundType.BUTTON_HOVER);
			}
			this.hovered = hovered;
			updateAnimation(hovered);

			if (colorBackground != -1) {

				if (!enabled && colorDisableBackground != -1) {
					GuiUtils.drawRoundedRect(x, y, width, height, rounted, colorDisableBackground,
							1.0);
				} else if (isHovered() && colorHoverBackground != -1 && !active) {
					GuiUtils.drawRoundedRect(x, y, width, height, rounted, colorHoverBackground,
							1.0);
				} else if (active && colorActiveBackground != -1) {
					GuiUtils.drawRoundedRect(x, y, width, height, rounted, colorActiveBackground,
							1.0);
				} else
					GuiUtils.drawRoundedRect(x, y, width, height, rounted, colorBackground, 1.0);

			}
		}

		if (texture != null) {

			if (!enabled && textureDisable != null) {
				GuiUtils.drawImageNew(textureDisable, x+xImage, y+yImage, wImage, hImage, 1.0);
			} else if (isHovered() && textureHover != null && !active) {
				GuiUtils.drawImageNew(textureHover, x+xImage, y+yImage, wImage, hImage, 1.0);
			} else if (active && textureActive != null) {
				GuiUtils.drawImageNew(textureActive, x+xImage, y+yImage, wImage, hImage, 1.0);
			} else {
				GuiUtils.drawImageNew(texture, x+xImage, y+yImage, wImage, hImage, 1.0);
			}

		}

		drawText();

	}
}
