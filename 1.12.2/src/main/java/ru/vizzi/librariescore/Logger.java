package ru.vizzi.librariescore;

import lombok.Setter;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public class Logger {

    private static final String TAG_INFO = " [INFO] ";
    private static final String TAG_WARN = " [WARNING] ";
    private static final String TAG_ERROR = " [ERROR] ";
    private static final String TAG_DEBUG = " [DEBUG] ";

    private final String prefix;
    @Setter
    private boolean debug;
    private Consumer<String> outConsumer = System.out::println;
    private BiFunction<String, Object[], String> formatConsumer = String::format;

    public Logger(String prefix) {
        this.prefix = "[" + prefix + "]";
    }

    public Logger setFormatConsumer(BiFunction<String, Object[], String> formatConsumer) {
        this.formatConsumer = formatConsumer;
        return this;
    }

    public Logger setOutConsumer(Consumer<String> outConsumer) {
        this.outConsumer = outConsumer;
        return this;
    }

    public void info(String s, Object... params) {
        info(formatConsumer.apply(s, params));
    }

    public void warn(String s, Object... params) {
        warn(formatConsumer.apply(s, params));
    }

    public void error(String s, Object... params) {
        error(formatConsumer.apply(s, params));
    }

    public void debug(String s, Object... params) {
        debug(formatConsumer.apply(s, params));
    }

    public void info(String s) {
        log(TAG_INFO, s);
    }

    public void warn(String s) {
        log(TAG_WARN, s);
    }

    public void error(String s) {
        log(TAG_ERROR, s);
    }

    public void debug(String s) {
        if(debug)
        log(TAG_DEBUG, s);
    }

    private void log(String tag, String message) {
        outConsumer.accept(prefix + tag + message);
    }
}
