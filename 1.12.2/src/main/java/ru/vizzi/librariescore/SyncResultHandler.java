package ru.vizzi.librariescore;


import lombok.Setter;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SyncResultHandler<T> {

    @Setter
    private static Consumer<Runnable> mainThreadExecutor;
    private final CompletableFuture<T> completableFuture;
    private final boolean checkNonNullResult;

    public SyncResultHandler(CompletableFuture<T> completableFuture) {
        this(completableFuture, true);
    }

    public SyncResultHandler(CompletableFuture<T> completableFuture, boolean checkNonNullResult) {
        this.completableFuture = completableFuture;
        this.checkNonNullResult = checkNonNullResult;
    }

    /**
     * Позволяет обработать асинхронный результат в основном потоке.
     * */
    public void thenAcceptSync(Consumer<T> consumer) {
        if (mainThreadExecutor == null) {
            throw new RuntimeException("The main thread executor wasn't set! I can't work in the main thread.");
        }
        completableFuture.thenAcceptAsync(result -> {
            if(checkNonNullResult && result == null) {
                return;
            }
            mainThreadExecutor.accept(() -> consumer.accept(result));
        });
    }
}
