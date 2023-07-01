package ru.vizzi.librariescore.databases.mariadb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.vizzi.librariescore.config.Configurable;
import ru.vizzi.librariescore.config.IConfigGson;

import java.io.File;
import java.util.ArrayList;

@RequiredArgsConstructor
@Setter
@Getter
public class ConfigMariaList implements IConfigGson {

    @Configurable
    boolean enabled = false;

    @Configurable
    ArrayList<ConfigDBType> configMariaDBS;

    private final String filePath;

    @Override
    public File getConfigFile() {
        return new File(filePath);
    }
}
