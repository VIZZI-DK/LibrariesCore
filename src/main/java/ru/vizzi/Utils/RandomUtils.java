package ru.vizzi.Utils;

import ru.vizzi.Utils.obf.IgnoreObf;

import java.awt.*;
import java.util.Random;

/**
 * @author Zloy_GreGan
 */
@IgnoreObf
public class RandomUtils {

    private static Random rand;

    public static char genChar(String alphabet) {
        return alphabet.charAt(rand.nextInt(alphabet.length()));
    }

    public static char genLetter() {
        return genChar("abcdefghijklmnopqrstuvwxyz");
    }

    public static char genSymbol() {
        return genChar("!@#$%^&*()_+\"в„–;%:?*()=-.,/\\|'");
    }

    public static char genDigit() {
        return genChar("0123456789");
    }

    public static char genChar(boolean symbols) {
        return genChar(symbols ? "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!@#$%^&*()_+\"в„–;%:?*()=-.,/\\|'" : "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
    }

    public static Color nextColor(int maxR, int maxG, int maxB) {
        return new Color(rand.nextInt(maxR), rand.nextInt(maxG), rand.nextInt(maxB));
    }

    public static Color nextColor() {
        return nextColor(256, 256, 256);
    }

    static {
        rand = new Random();
    }

}
