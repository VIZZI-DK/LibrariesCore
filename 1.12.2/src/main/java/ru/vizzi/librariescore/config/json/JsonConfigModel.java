package ru.vizzi.librariescore.config.json;

import java.util.HashMap;
import java.util.Map;

public class JsonConfigModel {

    private final Map<String, Object> configMap = new HashMap<>();

    public Map<String, Object> getConfigMap() {
        return configMap;
    }
}
