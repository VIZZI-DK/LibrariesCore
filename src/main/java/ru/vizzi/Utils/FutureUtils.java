package ru.vizzi.Utils;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;

public class FutureUtils {

    public static <T> ListenableFuture<T> immediateFailedFuture(Exception exception) {
        SettableFuture<T> future = SettableFuture.create();
        future.setException(exception);
        return future;
    }
}