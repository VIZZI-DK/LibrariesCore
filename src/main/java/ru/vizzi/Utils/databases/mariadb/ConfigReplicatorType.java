package ru.vizzi.Utils.databases.mariadb;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.vizzi.Utils.config.Configurable;

@Setter
@NoArgsConstructor
@Getter
public class ConfigReplicatorType {

    @Configurable
    private String host = "localhost";
    @Configurable
    private String username = "root";
    @Configurable
    private String password = "root";
    @Configurable
    private int replicationID = 1;
    @Configurable
    private int port = 3306;

}
