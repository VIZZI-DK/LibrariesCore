package ru.vizzi.librariescore.databases.mariadb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vizzi.librariescore.config.Configurable;

@Setter
@NoArgsConstructor
@Getter
public class ConfigDBType {

    @Configurable
    private String host;
    @Configurable
    private String username;
    @Configurable
    private String password;
    @Configurable
    private String database;
    @Configurable
    private int port;

}
