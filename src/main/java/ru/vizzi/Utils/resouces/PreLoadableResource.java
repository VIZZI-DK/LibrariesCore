package ru.vizzi.Utils.resouces;

import ru.vizzi.Utils.obf.IgnoreObf;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
@IgnoreObf
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface PreLoadableResource {

}
