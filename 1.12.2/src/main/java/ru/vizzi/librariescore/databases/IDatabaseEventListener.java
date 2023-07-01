package ru.vizzi.librariescore.databases;

public interface IDatabaseEventListener<T> {
    void onInsert(T object);
    void onDelete(T object);
    void onUpdate(T object);
}
