package ru.vizzi.Utils.databases;

public interface IDatabaseEventListener<T> {
    void onInsert(T object);
    void onDelete(T object);
    void onUpdate(T object);
}
