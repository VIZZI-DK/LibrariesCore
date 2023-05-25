package ru.vizzi.Utils.config;

import ru.vizzi.Utils.obf.IgnoreObf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@IgnoreObf
public @interface ConfigComment {
    String[] value();
}
