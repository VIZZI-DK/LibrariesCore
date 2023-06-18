package ru.vizzi.Utils;

import lombok.RequiredArgsConstructor;
import ru.vizzi.Utils.config.Configurable;
import ru.vizzi.Utils.config.IConfigGson;

import java.io.File;


@RequiredArgsConstructor
public class LibrariesConfig implements IConfigGson {

    private static LibrariesConfig config;

    public static LibrariesConfig getInstance(){
        if(config == null){
            return config = new LibrariesConfig("config/"+LibrariesCore.MODID+"/core.json");
        }
        return config;
    }

    @Configurable
    public boolean DEBUG = false;
    @Configurable
    public boolean mcTextureLoc = false;



    public final String file;

    @Override
    public File getConfigFile() {
        return new File(file);
    }
}
