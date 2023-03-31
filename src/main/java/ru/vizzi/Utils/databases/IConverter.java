package ru.vizzi.Utils.databases;

public interface IConverter<SOURCE, TARGET> {
    TARGET convertTo(SOURCE source);
    SOURCE convertFrom(TARGET target);
}
