package ru.vizzi.Utils;

import lombok.experimental.UtilityClass;
import org.lwjgl.input.Mouse;

@UtilityClass
public class MouseUtils {



    public int getEventDWheel(){
        int wheel = Mouse.getEventDWheel();
        if(wheel>0){
            return 120;
        } else if(wheel<0){
            return -120;
        }
        return wheel;



    }
}
