package ru.vizzi.librariescore.obf;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IgnoreObf
@Retention(RetentionPolicy.RUNTIME)
public @interface IgnoreObf {
}
