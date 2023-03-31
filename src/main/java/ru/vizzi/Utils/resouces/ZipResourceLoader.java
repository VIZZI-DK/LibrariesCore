package ru.vizzi.Utils.resouces;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import lombok.Getter;

public class ZipResourceLoader implements IResourceLoader {

    public static final String TARGET_DIR = "..\\resources";

    @Getter
    private final File[] files;
    private final ZipFile[] zipFiles;

    public ZipResourceLoader() {
        String[] zipNames = {"resources0.zip"};
        ZipFile[] zipFiles = new ZipFile[0];
        File[] files = new File[0];
        for (int i = 0; i < zipNames.length; i++) {
            File file;
            file = new File(zipNames[i]);

            if(file.exists()) {
                files = Arrays.copyOf(files, files.length + 1);
                zipFiles = Arrays.copyOf(zipFiles, zipFiles.length + 1);
                files[i] = file;
                try {
                    zipFiles[i] = new ZipFile(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.files = files;
        this.zipFiles = zipFiles;
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
        return null;
    }
}
