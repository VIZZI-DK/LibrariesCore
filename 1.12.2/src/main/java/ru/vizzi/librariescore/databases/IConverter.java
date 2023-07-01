package ru.vizzi.librariescore.databases;

public interface IConverter<SOURCE, TARGET> {
    TARGET convertTo(SOURCE source);
    SOURCE convertFrom(TARGET target);
}
