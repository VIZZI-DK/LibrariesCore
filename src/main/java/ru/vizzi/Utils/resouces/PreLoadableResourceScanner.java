package ru.vizzi.Utils.resouces;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import java.lang.reflect.Field;
import java.util.Set;

public class PreLoadableResourceScanner {

    public Set<Field> scanFields() {
        Reflections reflections = new Reflections("ru.vizzi", new FieldAnnotationsScanner());
        return reflections.getFieldsAnnotatedWith(PreLoadableResource.class);
    }

    public Set<Class<?>> scanClasses() {
        Reflections reflections = new Reflections("ru.vizzi", new TypeAnnotationsScanner(), new SubTypesScanner());
        return reflections.getTypesAnnotatedWith(PreLoadableResource.class);
    }
}
