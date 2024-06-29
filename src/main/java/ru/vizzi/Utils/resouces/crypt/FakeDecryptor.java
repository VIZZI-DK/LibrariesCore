package ru.vizzi.Utils.resouces.crypt;

import java.io.InputStream;

public class FakeDecryptor implements IDecryptor {
    @Override
    public InputStream getDecryptedInputStream(InputStream inputStream) throws Throwable {
        return inputStream;
    }
}
