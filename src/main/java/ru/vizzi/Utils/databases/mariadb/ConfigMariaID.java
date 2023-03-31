package ru.vizzi.Utils.databases.mariadb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.vizzi.Utils.config.Configurable;
import ru.vizzi.Utils.config.IConfigGson;

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
