package ru.vizzi.Utils.resouces;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import lombok.Getter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class MinecraftResourcesLoader implements IResourceLoader{
    @Getter
    private final ArrayList<File> files = new ArrayList<>();
    private final ArrayList<ZipFile> zipFiles = new ArrayList<>();

    private final ArrayList<File> gradleFiles = new ArrayList<>();

    public MinecraftResourcesLoader() {


        for(ModContainer modContainer : Loader.instance().getModList()){
            files.add(modContainer.getSource());
        }

        for (File file : files) {
            if(file.exists()) {
                try {
                    zipFiles.add(new ZipFile(file));
                } catch (IOException e) {
                    e.printStackTrace();
                    if(file.getName().endsWith("main")){
                        gradleFiles.add(file);
                    }
                }
            }
        }
    }

    @Override
    public InputStream getResourceInputStream(String path) throws IOException {
        for (ZipFile zipFile : zipFiles) {
            if(zipFile != null) {
                ZipEntry zipEntry = zipFile.getEntry("assets/" + path);
                if (zipEntry != null) {
                    return zipFile.getInputStream(zipEntry);
                }
            }
        }
        for (File fileGradle : gradleFiles) {
            if(fileGradle != null) {
                File file = new File(fileGradle, "assets/" + path);
                if (file != null) {
                    return new FileInputStream(file);
                }
            }
        }
        return null;
    }
}