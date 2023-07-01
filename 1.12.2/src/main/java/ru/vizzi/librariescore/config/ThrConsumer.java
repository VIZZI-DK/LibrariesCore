package ru.vizzi.librariescore.config;

import java.io.IOException;

public interface ThrConsumer<T> {

    void accept(T t) throws IOException;
}
