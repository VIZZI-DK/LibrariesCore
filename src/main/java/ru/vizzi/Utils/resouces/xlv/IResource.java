package ru.vizzi.Utils.resouces.xlv;

public interface IResource {
    void loadFromFile();
    void setFrom(AbstractResource resource);
    void loadToMemory();
    void unload();
}
