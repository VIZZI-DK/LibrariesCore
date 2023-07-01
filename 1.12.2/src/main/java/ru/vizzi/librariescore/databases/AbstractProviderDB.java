package ru.vizzi.librariescore.databases;

import lombok.Getter;
import ru.vizzi.librariescore.config.IConfigGson;

public abstract class AbstractProviderDB<T extends IConfigGson> {

	@Getter
    protected T config;

    public AbstractProviderDB(T config) {
        this.config = config;
        config.load();
    }

    public abstract void init();

    public abstract void shutdown();
}
