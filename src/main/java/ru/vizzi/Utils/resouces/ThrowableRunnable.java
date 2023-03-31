package ru.vizzi.Utils.resouces;

import lombok.SneakyThrows;

public interface ThrowableRunnable extends Runnable {

    @SneakyThrows
    @Override
    default void run() {
        execute();
    }

    void execute() throws Throwable;
}
