package ru.vizzi.Utils.config;



import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import ru.vizzi.Utils.CommonUtils;
import ru.vizzi.Utils.config.json.JsonConfigBuilder;
import ru.vizzi.Utils.config.json.JsonConfigModel;
import ru.vizzi.Utils.config.json.JsonConfigParser;

/**
 * This interface reads and writes config data to json file.
 * He looks for all fields annotated by ru.xlv.core.common.util.config.Configurable
 * in the input ru.xlv.core.common.util.config.IConfig object and create a JSONObject
 * in the set json file. The field's name will be taken as name of the
 * JSONObject, and the field's value as he's value.
 *
 * Supported types: {@link Integer}, {@link Float}, {@link Double}, {@link Long}, {@link String}, {@link Map}.
 *
 * @author Xlv
 * @deprecated Don't support a lot of features unlike {@link IConfigGson}.
 * */
@Deprecated
public interface IConfigJson extends IConfig {

    default void load(IConfigJson config, File file) {
        try {
            if(createConfigIfNull() && !file.exists()) {
                save(config);
            }
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String json = "";
            String line;
            while((line = bufferedReader.readLine()) != null) {
                json = json.concat(line);
            }
            bufferedReader.close();

            JsonConfigParser jsonConfigParser = new JsonConfigParser(json);
            jsonConfigParser.parse();

            JsonConfigModel jsonConfigModel = jsonConfigParser.getResult();
            for (Class<?> clazz : CommonUtils.getClassesHierarchy(config.getClass())) {
                boolean isClassAnnotated = clazz.getAnnotationsByType(Configurable.class).length > 0;
                for(Field field : clazz.getDeclaredFields()) {
                    if((isClassAnnotated || field.getAnnotationsByType(Configurable.class).length > 0) && jsonConfigModel.getConfigMap().get(field.getName()) != null) {
                        field.set(config, jsonConfigModel.getConfigMap().get(field.getName()));
                    }
                }
            }
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    default void load(IConfigJson config) {
        load(config, getConfigFile());
    }

    default void save(IConfigJson config) {
        Map<String, String> comments = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
        try {
            for (Class<?> clazz : CommonUtils.getClassesHierarchy(config.getClass())) {
                boolean isClassAnnotated = clazz.getAnnotationsByType(Configurable.class).length > 0;
                for (Field field : clazz.getDeclaredFields()) {
                    Configurable[] configurable = field.getAnnotationsByType(Configurable.class);
                    if ((isClassAnnotated || configurable.length > 0) && field.get(config) != null) {
                        map.put(field.getName(), field.get(config));
                        if (configurable.length > 0 && !configurable[0].comment().equals("")) {
                            comments.put(field.getName(), configurable[0].comment());
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        JsonConfigBuilder jsonConfigBuilder = new JsonConfigBuilder(getConfigFile(), map, comments, enablePrettySaving());
        jsonConfigBuilder.write();
    }

    /**
     * The file will be written as json.
     *
     * @return a json file
     * */
    File getConfigFile();

    /**
     * Do you want to create a config file if it doesn't exist ?
     *
     * Warning! Not initialized fields, except primitive data typed fields, will not be saved to JSON.
     * */
    default boolean createConfigIfNull() {
        return false;
    }

    /**
     * Do you want to see a pretty styling config ? :)
     * */
    default boolean enablePrettySaving() {
        return false;
    }
}
