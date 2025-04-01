package ru.vizzi.Utils.gui;

import ru.vizzi.Utils.gui.drawmodule.GuiDrawUtils;
import ru.vizzi.Utils.gui.drawmodule.GuiUtils;
import ru.vizzi.Utils.gui.drawmodule.ScaleGui;
import ru.vizzi.Utils.obf.IgnoreObf;

@IgnoreObf
public class GuiButtonAdvancedImage extends GuiButtonAdvanced {
    private float xImage;
    private float yImage;
    private float wImage;
    private float hImage;
    private float xText;

    public GuiButtonAdvancedImage(int id, float x, float y, float width, float height, String text, float textScale, float xImage, float yImage, float wImage, float hImage, float xText) {
        super(id, x, y, width, height, text, textScale);
        this.xImage = xImage;
        this.yImage = yImage;
        this.wImage = wImage;
        this.hImage = hImage;
        this.xText = xText;
    }

    public void drawButton(int mouseX, int mouseY) {
		if (visible) {
			updateAnimation(hovered);

			if (colorBackground != -1) {

				if (!enabled && colorDisableBackground != -1) {
					GuiDrawUtils.drawRoundedRect(xPosition, yPosition, width, height, rounted, colorDisableBackground,
							1.0);
				} else if (isHovered() && colorHoverBackground != -1 && !active) {
					GuiDrawUtils.drawRoundedRect(xPosition, yPosition, width, height, rounted, colorHoverBackground,
							1.0);
				} else if (active && colorActiveBackground != -1) {
					GuiDrawUtils.drawRoundedRect(xPosition, yPosition, width, height, rounted, colorActiveBackground,
							1.0);
				} else
					GuiDrawUtils.drawRoundedRect(xPosition, yPosition, width, height, rounted, colorBackground, 1.0);

			}
		}

		if (texture != null) {

			if (!enabled && textureDisable != null) {
				GuiUtils.drawImageNew(textureDisable, xPosition+xImage, yPosition+yImage, wImage, hImage, 1.0);
			} else if (isHovered() && textureHover != null && !active) {
				GuiUtils.drawImageNew(textureHover, xPosition+xImage, yPosition+yImage, wImage, hImage, 1.0);
			} else if (active && textureActive != null) {
				GuiUtils.drawImageNew(textureActive, xPosition+xImage, yPosition+yImage, wImage, hImage, 1.0);
			} else {
				GuiUtils.drawImageNew(texture, xPosition+xImage, yPosition+yImage, wImage, hImage, 1.0);
			}

		}

		drawText();

	}

}
