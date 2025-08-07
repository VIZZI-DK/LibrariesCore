package ru.vizzi.Utils.databases.mariadb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.vizzi.Utils.config.Configurable;
import ru.vizzi.Utils.config.IConfigGson;

import java.io.File;
import java.util.ArrayList;

@Setter
@Getter
public class ConfigMariaList implements IConfigGson {

    @Configurable
    boolean enabled = false;

    @Configurable
    ArrayList<ConfigDBType> configMariaDBS;

    @Configurable
    ArrayList<ConfigReplicatorType> replicationDatabase = new ArrayList<>();

    private final String filePath;


    public ConfigMariaList(String filePath) {
        this.filePath = filePath;
        replicationDatabase.add(new ConfigReplicatorType());
    }

    @Override
    public File getConfigFile() {
        return new File(filePath);
    }
}
