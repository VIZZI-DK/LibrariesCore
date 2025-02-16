package ru.vizzi.Utils.CustomFont;

import org.newdawn.slick.UnicodeFont;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;

import java.util.LinkedHashMap;
import java.util.Map;
@GradleSideOnly(GradleSide.CLIENT)
public class StringWidthCache {
    private final Map<String, Integer> cache;
    private final FontContainer font;

    public StringWidthCache(FontContainer font, int maxSize) {
        this.font = font;
        this.cache = new LinkedHashMap<String, Integer>(maxSize, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
                return size() > maxSize;
            }
        };
    }

    public int getStringWidth(String string, UnicodeFont uf) {
        if (font == null || string == null) {
            return 0;
        }
        if (!string.startsWith("§")) {
            string = "§f" + string;
        }
        if (cache.containsKey(string)) {
            return cache.get(string);
        }

        if (uf == null) {
            return 0;
        }
        StringBuilder result = new StringBuilder();
        String[] parts = string.split("§");

        for (String part : parts) {
            if (part != null && part.length() > 1) {
                result.append(part.substring(1));
            }
        }

        int width = uf.getWidth(result.toString());
        cache.put(string, width);
        return width;
    }

    public int getStringWidthNew(String string, UnicodeFont uf) {
        if (font == null || string == null) {
            return 0;
        }
        if (cache.containsKey(string)) {
            return cache.get(string);
        }
        if (uf == null) {
            return 0;
        }
        int width = uf.getWidth(string);
        cache.put(string, width);
        return width;
    }
}