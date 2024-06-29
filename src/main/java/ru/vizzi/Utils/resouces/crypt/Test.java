package ru.vizzi.Utils.resouces.crypt;

import java.io.*;

public class Test implements Serializable {

    public InputStream getDecryptedInputStream(InputStream inputStream) throws Throwable {
        System.out.println("Мы в тесте");
        return inputStream;
    }
}
