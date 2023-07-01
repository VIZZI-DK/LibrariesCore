package ru.vizzi.librariescore.databases.mariadb;

import lombok.SneakyThrows;
import ru.vizzi.librariescore.LibrariesCore;
import ru.vizzi.librariescore.databases.BiHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;

public class MariaDBProvider {

    private ConfigDBType configDBType;

    public MariaDBProvider(ConfigDBType config) {
        this.configDBType = config;
    }

    @SneakyThrows
    public Connection getConnection() {
        return DriverManager.getConnection(
                String.format("jdbc:mysql://%s:%s/%s?user=%s&password=%s&autoReconnect=true&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&connectTimeout=0&socketTimeout=0", configDBType.getHost(), configDBType.getPort(), configDBType.getDatabase(), configDBType.getUsername(), configDBType.getPassword()));
    }


    @Nullable
    public BiHolder<Statement, ResultSet> doRequestAsync(@Nonnull String sql) {
        return doRequestAsync(sql, 0);
    }

    @SneakyThrows
    private BiHolder<Statement, ResultSet> doRequestAsync(@Nonnull String sql, int deep) {
        if(deep < 3) {

            Statement statement = getConnection().createStatement();

            try {
                ResultSet resultSet = statement.executeQuery(sql);
                LibrariesCore.logger.debug("SQL: %s successful ", sql);
                return new BiHolder<>(statement, resultSet);
            } catch (SQLNonTransientConnectionException e) {
                LibrariesCore.logger.debug("SQL: %s error ", sql);
                e.printStackTrace();
                doRequestAsync(sql, deep + 1);
            }
        }
        return null;
    }
    @SneakyThrows
    private BiHolder<Statement, ResultSet> doRequestAsyncUpdate(@Nonnull String sql, int deep) {
        if(deep < 3) {

            Statement statement = getConnection().createStatement();

            try {
                statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
                ResultSet resultSet = statement.getGeneratedKeys();
                LibrariesCore.logger.debug("SQL: %s successful ", sql);
                return new BiHolder<>(statement, resultSet);
            } catch (SQLNonTransientConnectionException e) {
                doRequestAsync(sql, deep + 1);
            }
        }
        return null;
    }
    @Nullable
    public BiHolder<Statement, ResultSet> doRequestAsyncUpdate(@Nonnull String sql) {
        return doRequestAsyncUpdate(sql, 0);
    }

}
