package ru.vizzi.Utils.resouces.crypt;

import java.io.InputStream;

public interface IDecryptor {

    InputStream getDecryptedInputStream(InputStream inputStream) throws Throwable;

}
