package ru.vizzi.Utils.CustomFont;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Font;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import ru.justagod.cutter.GradleSide;
import ru.justagod.cutter.GradleSideOnly;
import ru.vizzi.Utils.LibrariesCore;
import ru.vizzi.Utils.eventhandler.EventResize;
import ru.vizzi.Utils.eventhandler.RegistryEvent;
import ru.vizzi.Utils.gui.drawmodule.ScaleGui;
import ru.vizzi.Utils.resouces.CoreAPI;

/*
    ScaleGui заменить на su.metalabs.lib.api.gui.utils.ScaleManager
    LibrariesCore.instance.runUsingMainClientThread() заменить на Minecraft.getMinecraft().func_152343_a()
 */



@GradleSideOnly(GradleSide.CLIENT)
public class CustomFontRenderer {
    private static final Map<String, UnicodeFont> cache = new HashMap();
    private static final Map<String, Color> colors = new HashMap();

    private static HashSet<String> loading = new HashSet<>();

    @Getter
    private static HashMap<FontContainer, StringWidthCache> widthCache = new HashMap();

    private static ExecutorService executorService;
    private static String symbols = " +=0123456789абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!-_()|?,./\"'[]{}*&^:%$₽;№@#~`><•і";
    public static String symbolsNew = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz" +
            "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя" +
            "ҐЄІЇґєії" +
            "0123456789" +
            "!\"#$%&'()*+,-—./:;<=>?@[\\]^_`{|}~" +
            " ";


    public CustomFontRenderer() {

        if(executorService == null){
            executorService = Executors.newFixedThreadPool(4);
        }

    }

    public static UnicodeFont getNoSyncFont(FontContainer font){
        String name = font.fontName+(int)ScaleGui.get(font.fontSize);
        if(cache.containsKey(name)){
            return cache.get(name);
        } else {
            try {
                InputStream in = CoreAPI.getInputStreamFromZip(font.rs); // Тут придеться не много подумать :(
                Font fontBase = Font.createFont(0, in);
                in.close();
                boolean bold = font.rs.getResourcePath().toLowerCase().contains("bold");
                UnicodeFont unicodeFont = new UnicodeFont(fontBase, (int) ScaleGui.get(font.fontSize), bold, false);
                unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
                unicodeFont.addGlyphs(symbolsNew);
                unicodeFont.setDisplayListCaching(true);
                unicodeFont.loadGlyphs();
                cache.put(name, unicodeFont);
                return unicodeFont;
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }


    private static CompletableFuture<UnicodeFont> getAsync(FontContainer font){
        String name = font.fontName+(int)ScaleGui.get(font.fontSize);
        if(loading.contains(name)){
            return null;
        } else {
            loading.add(name);
            return CompletableFuture.supplyAsync(()->{
                try{
                    InputStream in = CoreAPI.getInputStreamFromZip(font.rs); // Тут придеться не много подумать :(
                    Font fontBase = Font.createFont(0, in);
                    in.close();
                    boolean bold = font.rs.getResourcePath().toLowerCase().contains("bold");
                    UnicodeFont unicodeFont = new UnicodeFont(fontBase, (int) ScaleGui.get(font.fontSize), bold, false);
                    unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
                    unicodeFont.addGlyphs(symbolsNew);
                    unicodeFont.setDisplayListCaching(true);
                    return unicodeFont;
                } catch (Exception e){
                    e.printStackTrace();

                }
                return null;
            }, executorService);
        }
    }
    @SubscribeEvent
    public void resize(EventResize e){
        widthCache.clear();
    }

    public static UnicodeFont get(FontContainer font) {
        String name = font.fontName+(int)ScaleGui.get(font.fontSize);
        if(cache.containsKey(name)){
            return cache.get(name);
        } else {
            CompletableFuture<UnicodeFont> completableFuture = getAsync(font);
            if(completableFuture != null){
                completableFuture.handle((data, error) ->{
                    if(error != null){
                        error.printStackTrace();
                    } else if(data != null){
                        LibrariesCore.instance.runUsingMainClientThread(()->{ //Minecraft.getMinecraft().func_152343_a()
                            try{
                                data.loadGlyphs();
                                loading.remove(name);
                                cache.put(name, data);
                            } catch (Exception e){
                                e.printStackTrace();
                            }
                        });
                    }
                    return null;
                });
            }
        }
        return null;
    }


    public static StringWidthCache getCache(FontContainer font){
        return widthCache.computeIfAbsent(font, f -> new StringWidthCache(f, 200));
    }

    public static float getStringWidthNew(FontContainer font, String string) {
        if(font == null || string == null){
            return  0;
        }
        StringWidthCache stringWidthCache = getCache(font);
        UnicodeFont unicodeFont = get(font);
        return stringWidthCache.getStringWidthNew(string, unicodeFont);
        //  return (float)width;
    }

    public static float getStringWidth(FontContainer font, String string) {
        if(font == null || string == null){
            return  0;
        }
        if (!string.startsWith("§")) {
            string = "§f" + string;
        }

        UnicodeFont uf = get(font);
        if(uf == null){
            return 0;
        }
        StringBuilder result = new StringBuilder();
        String[] var4 = string.split("§");
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String s = var4[var6];
            if (s != null && s.length() > 1) {
                result.append(s.substring(1));
            }
        }

        int width = uf.getWidth(result.toString());
        return (float)width;
    }

    public static float getStringHeight(FontContainer font, String string, float w) {
        if(font == null || string == null){
            return  0;
        }
        if (!string.startsWith("§")) {
            string = "§f" + string;
        }

        if (w != -1.0F) {
            w *= 1 * 2.0F;
        }

        float y = 0.0F;
        float width = 0.0F;
        UnicodeFont uf = get(font);
        if(uf == null){
            return 0;
        }
        String[] var6 = string.split("\n");
        int var7 = var6.length;

        for(int var8 = 0; var8 < var7; ++var8) {
            String s1 = var6[var8];
            if (s1 != null && s1.length() != 0) {
                String source = "";
                String[] var11 = s1.split("§");
                int var12 = var11.length;

                for(int var13 = 0; var13 < var12; ++var13) {
                    String s = var11[var13];
                    if (s != null && s.length() > 1) {
                        String[] var15 = s.substring(1).split(" ");
                        int var16 = var15.length;

                        for(int var17 = 0; var17 < var16; ++var17) {
                            String s2 = var15[var17];
                            String t = s2 + " ";
                            source = source + t;
                            if (w != -1.0F && (float)(ScaleGui.get(width) + ScaleGui.get(uf.getWidth(t))) > w) {
                                y += (float)(uf.getHeight(source) + 2);
                                width = 0.0F;
                            }

                            width += (float)uf.getWidth(t);
                        }
                    }
                }

                y += (float)(uf.getHeight(source) + 2);
                width = 0.0F;
            }
        }

        return y / (2.0F);
    }



    public static void drawStringWithMaxWidth(String string, double x, double y, float width, int color, FontContainer font, EnumStringRenderType type) {
        if(font == null || string == null){
            return;
        }

        UnicodeFont uf = CustomFontRenderer.get(font);
        if(uf == null){
            return;
        }
        //GL11.glScalef((float)1, (float)1, (float)1.0f);
        GL11.glScaled(1.0001, 1.0001, 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        float xCurrent = 0;
        StringWidthCache stringWidthCache = getCache(font);

        if(width == -1){
            ArrayList<FontElement> fontElements = validateMinecraftColor(string, color);

            for(FontElement fontElement : fontElements){
                uf.drawString((float)x+xCurrent, (float)y, fontElement.string, fontElement.color);
                xCurrent+=stringWidthCache.getStringWidth(fontElement.string, uf);
            }
        } else {
            ArrayList<String> strings = splitString(string, width, uf);
            float yCurrent = 0;

            for(String s : strings){
                ArrayList<FontElement> fontElements = validateMinecraftColor(s, color);
                xCurrent = 0;
                for(FontElement fontElement : fontElements){
                    float textWidth = font.width(s);

                    uf.drawString((float)(x - (type == EnumStringRenderType.DEFAULT ? 0 : type == EnumStringRenderType.RIGHT ?
                            textWidth : textWidth/2))+xCurrent, (float)y+yCurrent, fontElement.string, fontElement.color);
                    xCurrent+=stringWidthCache.getStringWidth(fontElement.string, uf);
                }
                yCurrent+=uf.getHeight(s) + 2;

            }

        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glScalef((float)1, (float)1, (float)1.0f);
    }

    public static void drawStringWithMaxWidthNoValid(String string, double x, double y, float width, int color, FontContainer font, EnumStringRenderType type) {
        if(font == null || string == null){
            return;
        }

        UnicodeFont uf = CustomFontRenderer.get(font);
        if(uf == null){
            return;
        }
        //GL11.glScalef((float)1, (float)1, (float)1.0f);
        GL11.glScaled(1.0001, 1.0001, 0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        // float xCurrent = 0;
        StringWidthCache stringWidthCache = getCache(font);

        if(width == -1){
            uf.drawString((float)x, (float)y, string, new Color(color));
        } else {
            ArrayList<String> strings = splitString(string, width, uf);
            float yCurrent = 0;

            for(String s : strings){

                //  xCurrent = 0;
                //   for(FontElement fontElement : fontElements){
                float textWidth = font.width(s);

                uf.drawString((float)(x - (type == EnumStringRenderType.DEFAULT ? 0 : type == EnumStringRenderType.RIGHT ?
                        textWidth : textWidth/2)), (float)y+yCurrent, s, new Color(color));
                //xCurrent+=stringWidthCache.getStringWidth(s, uf);
                //   }
                yCurrent+=uf.getHeight(s) + 2;

            }

        }
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glScalef((float)1, (float)1, (float)1.0f);
    }

    public static ArrayList<FontElement> validateMinecraftColor(String s, int defaultColor){
        Color color1 = new Color(defaultColor);
        String drawString = "";
        ArrayList<FontElement> drawStringMass = new ArrayList<>();
        boolean nextColor = false;
        for(int i = 0; i<s.length(); i++){
            char c = s.charAt(i);
            if(c != '§'){
                if(nextColor){
                    color1 = getColor(c, defaultColor);
                    nextColor = false;
                    continue;
                }
                drawString+=c;
            } else {
                nextColor = true;
                drawStringMass.add(new FontElement(color1, drawString));
                drawString="";
            }
        }
        if(!drawString.isEmpty()){
            drawStringMass.add(new FontElement(color1, drawString));
        }
        return drawStringMass;
    }



    public static ArrayList<String> splitString(String input, float maxWidth, UnicodeFont uf) {
        ArrayList<String> splitStrings = new ArrayList<>();
        float wCurrent = 0;
        StringBuilder element = new StringBuilder();

        String[] lines = input.split("\\R"); // Разбиваем текст по \n
        for (String line : lines) {
            if(line.equals(" ")){
                splitStrings.add(line);
                continue;
            }
            String[] words = line.split(" ");

            for (int i = 0; i < words.length; i++) {
                String word = words[i];
                String s = (i + 1 == words.length) ? word : word + " ";
                float widthM = uf.getWidth(s);

                if (widthM > maxWidth) {
                    if (element.length() > 0) {
                        splitStrings.add(element.toString());
                        element.setLength(0);
                        wCurrent = 0;
                    }
                    splitStrings.addAll(splitLongWord(word, maxWidth, uf));
                } else {
                    if (wCurrent + widthM <= maxWidth) {
                        wCurrent += widthM;
                        element.append(s);
                    } else {
                        splitStrings.add(element.toString());
                        element.setLength(0);
                        wCurrent = widthM;
                        element.append(s);
                    }
                }
            }
            if (element.length() > 0) {
                splitStrings.add(element.toString());
                element.setLength(0);
                wCurrent = 0;
            }
        }

        return splitStrings;
    }


    private static ArrayList<String> splitLongWord(String word, float maxWidth, UnicodeFont uf) {
        ArrayList<String> parts = new ArrayList<>();
        StringBuilder part = new StringBuilder();
        float wCurrent = 0;

        for (char c : word.toCharArray()) {
            String s = String.valueOf(c);
            float widthM = uf.getWidth(s);
            if (wCurrent + widthM > maxWidth) {
                parts.add(part.toString());
                part.setLength(0);
                wCurrent = 0;
            }
            wCurrent += widthM;
            part.append(c);
        }
        if (part.length() > 0) {
            parts.add(part.toString());
        }

        return parts;
    }




    private static Color getColor(char s, int color) {
        Color c = colors.get(String.valueOf(s));
        return c != null ? c : new Color(color);
    }
//    @SubscribeEvent
//    public void preRender(TickEvent.RenderTickEvent ev) {
//        if (ev.phase == Phase.START) {
//            Minecraft mc = Minecraft.getMinecraft();
//            guiScale = (new ScaledResolution(mc, mc.displayWidth, mc.displayHeight)).getScaleFactor();
//            fontScale = 1.0F + (float)(guiScale - 2) / 2.0F;
//        }
//
//    }

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
        registerColors();
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class FontElement{

        Color color;
        String string;

    }
}
