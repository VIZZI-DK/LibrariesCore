package ru.vizzi.Utils.gui;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import ru.vizzi.Utils.CustomFont.FontContainer;
import ru.vizzi.Utils.gui.drawmodule.AnimationHelper;
import ru.vizzi.Utils.gui.drawmodule.GuiDrawUtils;
import ru.vizzi.Utils.gui.drawmodule.GuiUtils;
import ru.vizzi.Utils.obf.IgnoreObf;

@Getter
@IgnoreObf
@Setter
public class GuiButtonAdvanced extends GuiButtonNew {

	protected static final Minecraft mc = Minecraft.getMinecraft();

	protected ResourceLocation texture, textureHover, textureActive, textureDisable;

	public float xBase, yBase;
	public float widthBase, heightBase;
	protected float textBlending;
	protected float textScale;
	protected boolean hovered;
	protected boolean active;
	public float rounted = 0;
	public int colorBackground = -1;
	public int colorHoverBackground = -1;
	public int colorActiveBackground = -1;
	public int colorDisableBackground = -1;
	public int colorTextActive = -1;
	public int colorText = -1;
	public int colorTextHover = -1;
	public int colorTextDisable = -1;
	public FontContainer font;

	public GuiButtonAdvanced(int id, float x, float y, float width, float height, String text) {
		super(id, x, y, width, height, text);
		this.xBase = x;
		this.yBase = y;
		this.widthBase = width;
		this.heightBase = height;
		this.textScale = height / 36f;

	}

	public GuiButtonAdvanced(int id, float x, float y, float width, float height, String text, float textScale) {
		super(id, x, y, width, height, text);
		this.xBase = x;
		this.yBase = y;
		this.widthBase = width;
		this.heightBase = height;
		this.textScale = textScale;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
		this.drawButton(mouseX, mouseY);
	}

	protected void updateAnimation(boolean hovered) {
		if (hovered) {
			textBlending += AnimationHelper.getAnimationSpeed() * 0.1f;
			if (textBlending > 1)
				textBlending = 1;
		} else {
			textBlending -= AnimationHelper.getAnimationSpeed() * 0.1f;
			if (textBlending < 0)
				textBlending = 0f;
		}
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
				GuiUtils.drawImageNew(textureDisable, xPosition, this.yPosition, this.width, this.height, 1.0);
			} else if (isHovered() && textureHover != null && !active) {
				GuiUtils.drawImageNew(textureHover, xPosition, this.yPosition, this.width, this.height, 1.0);
			} else if (active && textureActive != null) {
				GuiUtils.drawImageNew(textureActive, xPosition, this.yPosition, this.width, this.height, 1.0);
			} else {
				GuiUtils.drawImageNew(texture, xPosition, this.yPosition, this.width, this.height, 1.0);
			}

		}

		drawText();

	}

	public boolean mousePressed(Minecraft p_146116_1_, int p_146116_2_, int p_146116_3_) {

		if (this.enabled && this.visible && p_146116_2_ >= this.xPosition && p_146116_3_ >= this.yPosition
				&& p_146116_2_ < this.xPosition + this.width && p_146116_3_ < this.yPosition + this.height) {
			return true;
		}
		return false;
	}

	protected boolean isHovered(int mouseX, int mouseY) {

		return this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition
				&& mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
	}

	protected void drawText() {
		int color = -1;
		if (colorText != -1) {

			if (!enabled) {
				color = colorTextDisable;
			} else if (isHovered() && !active) {
				color = colorTextHover;
			} else if (active) {
				color = colorTextActive;
			} else {
				color = colorText;
			}
			
			float paddingY = textScale * 4f;
			GuiDrawUtils.drawStringNoScale(font, displayString,
					this.xPosition + this.width / 2.0f - font.width(displayString) * textScale / 2f,
					this.yPosition + this.height / 2.0f - paddingY, textScale, color);

		}
	}

	public void addXPosition(float x) {
		this.xPosition = (int) (xBase + x);
	}

	public void addYPosition(float y) {
		this.yPosition = (int) (yBase + y);
	}

	public void scaleXPosition(float x) {
		this.xPosition = (int) (xBase * x);
	}

	public void scaleYPosition(float y) {
		this.yPosition = (int) (yBase * y);
	}

	public void setXPosition(float x) {
		this.xBase = this.xPosition = (int) x;
	}

	public void setYPosition(float y) {
		this.yBase = this.yPosition = (int) y;
	}

	public void setHeight(float height) {
		this.heightBase = this.height = (int) height;
	}

	public void setWidth(float width) {
		this.widthBase = this.width = (int) width;
	}

	public void scaleWidth(float x) {
		this.width = (int) (widthBase * x);
	}

	public void scaleHeight(float y) {
		this.height = (int) (heightBase * y);
	}
}
