package ru.vizzi.librariescore;

import lombok.RequiredArgsConstructor;
import ru.vizzi.librariescore.config.Configurable;
import ru.vizzi.librariescore.config.IConfigGson;

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
