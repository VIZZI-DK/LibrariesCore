package ru.vizzi.librariescore.config.json;

public interface IJsonParser<T> {

    void parse();

    T getResult();
}
