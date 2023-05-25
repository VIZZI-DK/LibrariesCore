package ru.vizzi.Utils;

import ru.vizzi.Utils.obf.IgnoreObf;

import java.util.Comparator;
import java.util.stream.Stream;

/**
 * @author Zloy_GreGan
 */
@IgnoreObf
public class MathUtils {

    public static int getMaxNumber(Integer... number) {
        return Stream.of(number).max(Comparator.comparing(Integer::intValue)).get();
    }

    public static int getMinNumber(Integer... number) {
        return Stream.of(number).min(Comparator.comparing(Integer::intValue)).get();
    }

    public static float getMaxNumber(Float... number) {
        return Stream.of(number).max(Comparator.comparing(Float::floatValue)).get();
    }

    public static float getMinNumber(Float... number) {
        return Stream.of(number).min(Comparator.comparing(Float::floatValue)).get();
    }

    private float clamp(float value, float min, float max) {
        if (value < min) { return min; }
        if (value > max) { return max; }
        return value;
    }

}
