package ru.vizzi.Utils.toast;


import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

@GradleSideOnly(GradleSide.CLIENT)
public interface IToast {
    public static final Object NO_TOKEN = new Object();

    Visibility draw(GuiToast paramGuiToast, long paramLong);

     void setDisplayedText(AdvancedToast.Type type, String title, String message);

    default Object getType() {
        return NO_TOKEN;
    }

    public enum Visibility {
        SHOW, HIDE;
    }
}
