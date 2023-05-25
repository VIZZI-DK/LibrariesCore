package ru.vizzi.Utils.CustomFont;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventBus;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import ru.vizzi.Utils.resouces.CoreAPI;

@SideOnly(Side.CLIENT)
public class CustomFontRenderer {
    private static final Map<String, UnicodeFont> cache = new HashMap<String, UnicodeFont>();
    private static final Map<String, Color> colors = new HashMap<String, Color>();
    public static int guiScale;
    static float fontScale;

    private static String symbols = " +=0123456789абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!-_()?,./\"'[]{}*&^:%$;№@#~`><•";

    private static UnicodeFont get(CustomFont font) {
        int size = (int)((float)font.size * fontScale);
        return cache.computeIfAbsent(font.font + size, o -> {
            Font s;
            try {
                try (InputStream in = CoreAPI.getInputStreamFromZip(font.rs);){
                    s = Font.createFont(0, in);
                }
            }
            catch (FontFormatException | IOException e) {
                throw new RuntimeException(font.font, e);
            }
            UnicodeFont uf = new UnicodeFont(s, size, false, false);
            uf.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
            uf.addGlyphs(symbols);
            try {
                uf.loadGlyphs();
            }
            catch (SlickException e) {
                throw new RuntimeException(font.font, e);
            }
            uf.setDisplayListCaching(true);
            return uf;
        });
    }

    public static float getStringWidth(CustomFont font, String string) {
        if (!string.startsWith("\u00a7")) {
            string = "\u00a7f" + string;
        }
        UnicodeFont uf = CustomFontRenderer.get(font);
        StringBuilder result = new StringBuilder();
        for (String s : string.split("\u00a7")) {
            if (s == null || s.length() <= 1) continue;
            result.append(s.substring(1));
        }
        int width = uf.getWidth(result.toString());
        return ((float)width / (fontScale * 2.0f));
    }

    public static float getStringHeight(CustomFont font, String string, float w) {
        if (!string.startsWith("\u00a7")) {
            string = "\u00a7f" + string;
        }
        if (w != -1) {
            w = ((float)w * (fontScale * 2.0f));
        }
        float y = 0;
        float width = 0;
        UnicodeFont uf = CustomFontRenderer.get(font);
        for (String s1 : string.split("\n")) {
            if (s1 == null || s1.length() == 0) continue;
            String source = "";
            for (String s : s1.split("§")) {
                if (s == null || s.length() <= 1) continue;
                for (String s2 : s.substring(1).split(" ")) {
                    String t = s2 + " ";
                    source = source + t;
                    if (w != -1 && width + uf.getWidth(t) > w) {
                        y += uf.getHeight(source) + 2;
                        width = 0;
                    }
                    width += uf.getWidth(t);
                }
            }
            y += uf.getHeight(source) + 2;
            width = 0;
        }
        return ((float)y / (fontScale * 2.0f));
    }

    public static void drawString(String string, float x, float y, CustomFont font) {
        CustomFontRenderer.drawStringWithMaxWidth(string, x, y, -1, font);
    }

    public static void drawStringWithMaxWidth(String string, float x, float y, float w, CustomFont font) {
        float guiScale = fontScale * 2.0f;
        float rscale = 1.0f / guiScale;
        x = ((float)x * guiScale);
        y = ((float)y * guiScale);
        if (w != -1) {
            w = ((float)w * guiScale);
        }
        GL11.glScalef((float)rscale, (float)rscale, (float)1.0f);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
        if (!string.startsWith("\u00a7")) {
            string = "\u00a7f" + string;
        }
        float sx = x;
        float width = 0;
        UnicodeFont uf = CustomFontRenderer.get(font);
        for (String s1 : string.split("\n")) {
            if (s1 == null || s1.length() == 0) continue;
            String source = "";
            for (String s : s1.split("§")) {
                if (s == null || s.length() <= 1) continue;
                char col = s.charAt(0);
                for (String s2 : s.substring(1).split(" ")) {
                    String t = s2 + " ";
                    source = source + t;
                    if (w != -1 && width + uf.getWidth(t) > w) {
                        x = sx;
                        y += uf.getHeight(source) + 2;
                        width = 0;
                    }
                    uf.drawString((float)x, (float)y, t, CustomFontRenderer.getColor(col));
                    width += uf.getWidth(t);
                    x += uf.getWidth(t);
                }
            }
            x = sx;
            y += uf.getHeight(source) + 2;
            width = 0;
        }
        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glScalef((float)guiScale, (float)guiScale, (float)1.0f);
    }

    private static Color getColor(char s) {
        Color c = colors.get(String.valueOf(s));
        if (c != null) {
            return c;
        }
        return Color.white;
    }

    @SubscribeEvent
    public void preRender(TickEvent.RenderTickEvent ev) {
        if (ev.phase == TickEvent.Phase.START) {
            Minecraft mc = Minecraft.getMinecraft();
            guiScale = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight).getScaleFactor();
            fontScale = 1.0f + (float)(guiScale - 2) / 2.0f;
        }
    }

    private static void registerColors() {
        colors.put("0", new Color(0));
        colors.put("1", new Color(170));
        colors.put("2", new Color(43520));
        colors.put("3", new Color(43690));
        colors.put("4", new Color(11141120));
        colors.put("5", new Color(11141290));
        colors.put("6", new Color(16755200));
        colors.put("7", new Color(11184810));
        colors.put("8", new Color(5592405));
        colors.put("9", new Color(5592575));
        colors.put("a", new Color(5635925));
        colors.put("b", new Color(5636095));
        colors.put("c", new Color(16733525));
        colors.put("d", new Color(16733695));
        colors.put("e", new Color(16777045));
        colors.put("f", new Color(16777215));
        colors.put("g", new Color(12632256));
        colors.put("h", new Color(13467442));
        colors.put("y", new Color(3178751));
    }

    static {
        FMLCommonHandler.instance().bus().register((Object)new CustomFontRenderer());
        CustomFontRenderer.registerColors();
    }
}

