package ru.vizzi.Utils.config.json;

public interface IJsonParser<T> {

    void parse();

    T getResult();
}
