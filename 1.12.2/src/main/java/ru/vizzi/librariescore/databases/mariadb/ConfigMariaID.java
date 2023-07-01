package ru.vizzi.librariescore.databases.mariadb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.vizzi.librariescore.config.Configurable;
import ru.vizzi.librariescore.config.IConfigGson;

import java.io.File;

@Getter
@RequiredArgsConstructor

public class ConfigMariaID implements IConfigGson {

    @Configurable
    private int databasesID;

    private final String filePath;

    @Override
    public File getConfigFile() {
        return new File(filePath);
    }

}
