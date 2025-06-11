package ru.vizzi.Utils.toast;

import com.google.common.collect.Queues;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Deque;
@GradleSideOnly(GradleSide.CLIENT)
public class GuiToast extends Gui {
    private final Minecraft mc;
    private final ToastInstance<?>[] visible = new ToastInstance<?>[5];
    private final Deque<IToast> toastsQueue = Queues.newArrayDeque();

    public GuiToast(Minecraft mcIn) {
        this.mc = mcIn;
    }

    public void drawToast() {
        if (!this.mc.gameSettings.hideGUI) {
            ScaledResolution resolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
            RenderHelper.disableStandardItemLighting();
            for (int i = 0; i < this.visible.length; i++) {
                ToastInstance<?> toastInstance = this.visible[i];
                if (toastInstance != null && toastInstance.render(resolution.getScaledWidth(), i))
                    this.visible[i] = null;
                if (this.visible[i] == null && !this.toastsQueue.isEmpty())
                    this.visible[i] = new ToastInstance<>(this.toastsQueue.removeFirst());
            }
        }
    }

    @Nullable
    public IToast getToast(Class<? extends IToast> toastClass, Object toastObject) {
        for (ToastInstance<?> toastInstance : this.visible) {
            if (toastInstance != null && toastClass.isAssignableFrom(toastInstance.getToast().getClass()) && toastInstance.getToast().getType().equals(toastObject))
                return  toastInstance.getToast();
        }
        for (IToast toast : this.toastsQueue) {
            if (toastClass.isAssignableFrom(toast.getClass()) && toast.getType().equals(toastObject))
                return toast;
        }
        return null;
    }

    public void clear() {
        Arrays.fill(this.visible, null);
        this.toastsQueue.clear();
    }

    public void add(IToast toastIn) {
        this.toastsQueue.add(toastIn);
    }

    public Minecraft getMinecraft() {
        return this.mc;
    }

    @SideOnly(Side.CLIENT)
    class ToastInstance<T extends IToast> {
        @Getter
        private final IToast toast;
        private long animationTime = -1L;
        private long visibleTime = -1L;
        private IToast.Visibility visibility = IToast.Visibility.SHOW;

        private ToastInstance(T toastIn) {
            this.toast = toastIn;
        }

        private float getVisibility(long flag) {
            float alpha = MathHelper.clamp_float((float) (flag - this.animationTime) / 600.0F, 0.0F, 1.0F);
            alpha *= alpha;
            return (this.visibility == IToast.Visibility.HIDE) ? (1.0F - alpha) : alpha;
        }

        public boolean render(int posX, int posY) {
            long systemTime = Minecraft.getSystemTime();
            if (this.animationTime == -1L)
                this.animationTime = systemTime;
            if (this.visibility == IToast.Visibility.SHOW && systemTime - this.animationTime <= 600L)
                this.visibleTime = systemTime;
            GL11.glPushMatrix();
            GL11.glTranslatef(posX - 160.0F * getVisibility(systemTime), (posY * 32), (500 + posY));
            IToast.Visibility toast$visibility = this.toast.draw(GuiToast.this, systemTime - this.visibleTime);
            GL11.glPopMatrix();
            if (toast$visibility != this.visibility) {
                this.animationTime = systemTime - (int) ((1.0F - getVisibility(systemTime)) * 600.0F);
                this.visibility = toast$visibility;
            }
            return (this.visibility == IToast.Visibility.HIDE && systemTime - this.animationTime > 600L);
        }
    }
}
