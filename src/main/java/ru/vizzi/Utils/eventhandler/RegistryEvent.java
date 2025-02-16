package ru.vizzi.Utils.eventhandler;

import cpw.mods.fml.relauncher.Side;
import ru.vizzi.Utils.obf.IgnoreObf;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(CLASS)
@Target(TYPE)
@IgnoreObf
public @interface RegistryEvent {


    String modid() default "";

    Side side() default Side.CLIENT;

}
