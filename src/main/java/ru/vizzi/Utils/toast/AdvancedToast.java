package ru.vizzi.Utils.toast;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.vizzi.Utils.LibrariesCore;


@GradleSideOnly(GradleSide.CLIENT)
public class AdvancedToast implements IToast {
    public static final ResourceLocation TEXTURE_TOASTS = new ResourceLocation(LibrariesCore.MODID, "textures/gui/toasts.png");
    public static RenderItem renderitem = new RenderItem();
    private static Minecraft mc = Minecraft.getMinecraft();

    private final Icon icon;
    private final ItemStack stack;

    private final long timeoutDelay;

    private String title;

    private String message;

    private Type type;

    private long firstDrawTime;

    private boolean newDisplay;

    private AdvancedToast(Icon icon, Type type, String title, String message, int timeoutDelay) {
        this.icon = icon;
        this.stack = null;
        this.type = type;
        this.title = title;
        this.message = message;
        this.timeoutDelay = timeoutDelay;
    }
    private AdvancedToast(Icon icon,ItemStack stack, Type type, String title, String message, int timeoutDelay) {
        this.icon = icon;
        this.stack = stack;
        this.type = type;
        this.title = title;
        this.message = message;
        this.timeoutDelay = timeoutDelay;
    }

    private AdvancedToast(Icon icon, Type type, IChatComponent title, IChatComponent message, int timeout) {
        this(icon, type, title.getUnformattedText(), (message == null) ? null : message.getUnformattedText(), timeout);
    }

    public Icon getType() {
        return this.icon;
    }

    public void setDisplayedText(Type type, String title, String message) {
        this.type = type;
        this.title = title;
        this.message = message;
        this.newDisplay = true;
    }

    public void setDisplayedText(Type type, IChatComponent title, IChatComponent message) {
        setDisplayedText(type, title.getUnformattedText(), (message == null) ? null : message.getUnformattedText());
    }

    public Visibility draw(GuiToast gui, long delta) {
        if (this.newDisplay) {
            this.firstDrawTime = delta;
            this.newDisplay = false;
        }
        Minecraft minecraft = gui.getMinecraft();
        FontRenderer fontRenderer = minecraft.fontRenderer;
        minecraft.getTextureManager().bindTexture(TEXTURE_TOASTS);
        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        gui.drawTexturedModalRect(0, 0, 0, this.type

                .texturePosY, 180, 32);
        if(stack != null) {
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glEnable(GL11.GL_COLOR_MATERIAL);
            GL11.glEnable(GL11.GL_LIGHTING);
            renderitem.renderItemAndEffectIntoGUI(mc.fontRenderer, mc.getTextureManager(), stack, 8, 8);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        } else {
            gui.drawTexturedModalRect(6, 6, this.icon

                    .iconPosX, this.icon
                    .iconPosY, 20, 20);
        }
        int titleColor = -256;
        int subTitleColor = 0;
        if (this.type == Type.SUCCESS) {
            titleColor = this.icon.titleColor;
            subTitleColor = this.icon.messageColor;
        }
        if (this.message == null) {
            fontRenderer.drawString(this.title, 30, 12, titleColor);
        } else {
            fontRenderer.drawString(this.title, 30, 7, titleColor);
            fontRenderer.drawString(this.message, 30, 18, subTitleColor);
        }
        return (delta - this.firstDrawTime < this.timeoutDelay) ? Visibility.SHOW : Visibility.HIDE;
    }

    public static void addOrUpdate(Icon icon, Type type, String title, String message, int timeout) {
        GuiToast gui = ClientEvent.getGuiToast();
        IToast toast = gui.getToast(AdvancedToast.class, icon);
        if (toast == null) {
            gui.add(new AdvancedToast(icon, type, title, message, timeout));
        } else {
            toast.setDisplayedText(type, title, message);
        }
    }

    public static void addOrUpdate(Icon icon, ItemStack itemStack, Type type, String title, String message, int timeout) {
        GuiToast gui = ClientEvent.getGuiToast();
        if(itemStack != null){
            gui.add(new AdvancedToast(icon,itemStack, type, title, message, timeout));
        }
    }

    public static void addOrUpdate(Icon icon, Type type, IChatComponent title, IChatComponent message, int timeout) {
        addOrUpdate(icon, type, title

                .getUnformattedText(), (message == null) ? null : message
                .getUnformattedText(), timeout);
    }

    public static void addOrUpdate(Icon icon, Type type, IChatComponent title, IChatComponent message) {
        addOrUpdate(icon, type, title, message, 2000);
    }

    public enum Icon {
        RADIO(9437439, 3421236, 176, 0),
        INFO(9437439, 3421236, 196, 0),
        CLEAN(9437439, 3421236, 216, 0);

        private final int titleColor;

        private final int messageColor;

        private final int iconPosX;

        private final int iconPosY;

        Icon(int titleColor, int messageColor, int iconPosX, int iconPosY) {
            this.titleColor = titleColor;
            this.messageColor = messageColor;
            this.iconPosX = iconPosX;
            this.iconPosY = iconPosY;
        }
    }

    public enum Type {
        ERROR(0),
        WARNING(32),
        SUCCESS(64);

        private final int texturePosY;

        Type(int texturePosY) {
            this.texturePosY = texturePosY;
        }
    }
}
