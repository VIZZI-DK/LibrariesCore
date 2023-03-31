package ru.vizzi.Utils.resouces;

import java.io.IOException;
import java.io.InputStream;

public interface IResourceLoader {

    InputStream getResourceInputStream(String path) throws IOException;
}
