package ru.vizzi.Utils;

import com.google.common.annotations.Beta;
import lombok.SneakyThrows;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Beta
@SuppressWarnings("unused")
public class CompletableFutureBuilder<T> {

    public static class SyncQueueHandler {

        private static final Queue<FutureTask<?>> QUEUE = new LinkedList<>();

        public static void update() {
            synchronized (QUEUE) {
                while (!QUEUE.isEmpty()) {
                    QUEUE.poll().run();
                }
            }
        }

        public static void remove(FutureTask<?> futureTask) {
            synchronized (QUEUE) {
                QUEUE.remove(futureTask);
            }
        }
    }

    private static class Settings {
        private static final long DEFAULT_SYNC_TASK_TIMEOUT = 1000L;
        private long timeout = DEFAULT_SYNC_TASK_TIMEOUT;
    }

    private final CompletableFuture<T> completableFuture;
    private final Settings settings;

    private CompletableFutureBuilder(CompletableFuture<T> completableFuture) {
        this(completableFuture, new Settings());
    }

    private CompletableFutureBuilder(CompletableFuture<T> completableFuture, Settings settings) {
        this.completableFuture = completableFuture;
        this.settings = settings;
    }

    public CompletableFutureBuilder<Void> thenAccept(Consumer<T> consumer) {
        return new CompletableFutureBuilder<>(completableFuture.thenAccept(consumer), settings);
    }

    public CompletableFutureBuilder<Void> thenAcceptSync(Consumer<T> consumer) {
        return new CompletableFutureBuilder<>(completableFuture.thenAccept(t -> acceptSync0(t, consumer)), settings);
    }

    public <R> CompletableFutureBuilder<R> thenApply(Function<T, R> function) {
        return new CompletableFutureBuilder<>(completableFuture.thenApply(function), settings);
    }

    public <R> CompletableFutureBuilder<R> thenApplySync(Function<T, R> function) {
        return new CompletableFutureBuilder<>(completableFuture.thenApply(t -> applySync0(t, function)), settings);
    }

    public CompletableFuture<T> build() {
        return completableFuture;
    }

    public CompletableFutureBuilder<T> syncQueueTimeout(long timeout) {
        this.settings.timeout = timeout;
        return this;
    }

    public static CompletableFutureBuilder<Void> runAsync(Runnable runnable) {
        return new CompletableFutureBuilder<>(CompletableFuture.runAsync(runnable));
    }

    public static CompletableFutureBuilder<Void> runAsync(Runnable runnable, Executor executor) {
        return new CompletableFutureBuilder<>(CompletableFuture.runAsync(runnable, executor));
    }

    public static CompletableFutureBuilder<Void> runSync(Runnable runnable) {
        return runAsync(() -> {}).thenAcceptSync(o -> runnable.run());
    }

    public static <T> CompletableFutureBuilder<T> supplyAsync(Supplier<T> supplier) {
        return new CompletableFutureBuilder<>(CompletableFuture.supplyAsync(supplier));
    }

    public static <T> CompletableFutureBuilder<T> supplyAsync(Supplier<T> supplier, Executor executor) {
        return new CompletableFutureBuilder<>(CompletableFuture.supplyAsync(supplier, executor));
    }

    public static <T> CompletableFutureBuilder<T> supplySync(Supplier<T> supplier) {
        return supplyAsync(() -> null).thenApplySync(o -> supplier.get());
    }

    public static <T> CompletableFutureBuilder<T> builder(CompletableFuture<T> completableFuture) {
        return new CompletableFutureBuilder<>(completableFuture);
    }

    public static <T> CompletableFutureBuilder<T> builder() {
        return new CompletableFutureBuilder<>(CompletableFuture.supplyAsync(() -> null));
    }

    public static <T> CompletableFutureBuilder<T> builder(Executor executor) {
        return new CompletableFutureBuilder<>(CompletableFuture.supplyAsync(() -> null, executor));
    }

    @SneakyThrows
    private <U> U acceptSync0(U object, Consumer<U> consumer) {
        return returnSyncResult(new FutureTask<>(() -> consumer.accept(object), object));
    }

    @SneakyThrows
    private <U, R> R applySync0(U object, Function<U, R> function) {
        return returnSyncResult(new FutureTask<>(() -> function.apply(object)));
    }

    @SneakyThrows
    private <R> R returnSyncResult(FutureTask<R> e) {
        if(e != null) {
            SyncQueueHandler.QUEUE.add(e);
            long l = System.currentTimeMillis();
            while(!e.isDone()) {
                if(settings.timeout != -1) {
                    if (System.currentTimeMillis() - l >= settings.timeout) {
                        System.out.println(e + " is time outed");
                        SyncQueueHandler.remove(e);
                        return null;
                    }
                }
            }
            return e.get();
        }
        return null;
    }

//    static ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
//
//
//    @SneakyThrows
//    public static void main(String[] args) {
//        Thread thread = new Thread(() -> {
//            System.out.println(Thread.currentThread());
//            while (true) {
//                SyncQueueHandler.update();
//            }
//        });
//        thread.setDaemon(true);
//        thread.start();
//
//
//        TestAAA testAAA = new TestAAA();
//        Test test = new Test();
//        System.out.println(test);
//        System.out.println(testAAA.getTest());
//
//        for (int i = 0; i < 10; i++) {
//            System.out.println("step " + i);
//            CompletableFutureBuilder.builder(executorService)
//                    .syncQueueTimeout(-1)
//                    .thenApply(o -> {
//                        System.out.println(Thread.currentThread());
//                        return "";
//                    })
//                    .thenApplySync(s -> {
//                        System.out.println(Thread.currentThread());
//                        return 123;
//                    })
//                    .thenAccept(integer -> {
//                        try {
//                            Thread.sleep(2000);
//                        } catch (Throwable throwable) {}
//                        System.out.println(Thread.currentThread());
//                        System.out.println(integer);
//                    })
//                    .thenAcceptSync(aVoid -> {
//                        System.out.println(Thread.currentThread());
//                        System.out.println("?????");
//                        testAAA.test();
//                    });
//        }
//
//        Thread.sleep(33000);
//
//        System.out.println(test);
//        System.out.println(testAAA.getTest());
//    }
}
