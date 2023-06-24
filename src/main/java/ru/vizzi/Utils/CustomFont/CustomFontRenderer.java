package ru.vizzi.Utils.CustomFont;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import ru.vizzi.Utils.eventhandler.RegistryEvent;
import ru.vizzi.Utils.gui.drawmodule.ScaleGui;
import ru.vizzi.Utils.resouces.CoreAPI;

@SideOnly(Side.CLIENT)
@RegistryEvent
public class CustomFontRenderer {
    private static final Map<String, UnicodeFont> cache = new HashMap();
    private static final Map<String, Color> colors = new HashMap();
    //    public static int guiScale;
//    static float fontScale;
    private static String symbols = " +=0123456789абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!-_()?,./\"'[]{}*&^:%$;№@#~`><•";

    public CustomFontRenderer() {
    }

    private static UnicodeFont get(FontContainer font) {
        int size = (int)((float)font.fontSize * 1);
        return (UnicodeFont)cache.computeIfAbsent(font.fontName + size, (o) -> {
            Font s;
            try {
                InputStream in = CoreAPI.getInputStreamFromZip(font.rs);
                Throwable var5 = null;

                try {
                    s = Font.createFont(0, in);
                } catch (Throwable var17) {
                    var5 = var17;
                    throw var17;
                } finally {
                    if (in != null) {
                        if (var5 != null) {
                            try {
                                in.close();
                            } catch (Throwable var15) {
                                var5.addSuppressed(var15);
                            }
                        } else {
                            in.close();
                        }
                    }

                }
            } catch (IOException | FontFormatException var19) {
                throw new RuntimeException(font.fontName, var19);
            }

            UnicodeFont uf = new UnicodeFont(s, size, false, false);
            uf.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
            uf.addGlyphs(symbols);

            try {
                uf.loadGlyphs();
            } catch (SlickException var16) {
                throw new RuntimeException(font.fontName, var16);
            }

            uf.setDisplayListCaching(true);
            return uf;
        });
    }

    public static float getStringWidth(FontContainer font, String string) {
        if (!string.startsWith("§")) {
            string = "§f" + string;
        }

        UnicodeFont uf = get(font);
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
        if (!string.startsWith("§")) {
            string = "§f" + string;
        }

        if (w != -1.0F) {
            w *= 1 * 2.0F;
        }

        float y = 0.0F;
        float width = 0.0F;
        UnicodeFont uf = get(font);
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
        float guiScale = ScaleGui.get(1.0f);
        x = ((float)x * guiScale);
        y = ((float)y * guiScale);
//        if (w != -1) {
//            w = ((float)w * guiScale);
//        }
        GL11.glScalef((float)guiScale, (float)guiScale, (float)1.0f);
        GL11.glEnable((int)3042);
        GL11.glDisable((int)3553);
        GL11.glBlendFunc((int)770, (int)771);
//        if (!string.startsWith("§")) {
//            string = "§v" + string;
//        }
//        double sx = x;
//        int width = 0;
//        UnicodeFont uf = CustomFontRenderer.get(font);
//        for (String s1 : string.split("\n")) {
//            if (s1 == null || s1.length() == 0) continue;
//            String source = "";
//            for (String s : s1.split("§")) {
//                if (s == null || s.length() <= 1) continue;
//                char col = s.charAt(0);
//                for (String s2 : s.substring(1).split(" ")) {
//                    String t = s2 + " ";
//                    source = source + t;
//                    if (w != -1 && ScaleGui.get(width) + ScaleGui.get(uf.getWidth(t)) > w) {
//                        x = sx;
//                        y += uf.getHeight(source) + 2;
//                        width = 0;
//                    }
//                    uf.drawString((float) (x - (type == EnumStringRenderType.DEFAULT ? 0 : type == EnumStringRenderType.RIGHT ?
//                            font.width(s) * ScaleGui.get(2) : font.width(s)  / 2f)), (float)y, t, CustomFontRenderer.getColor(col, color));
//                    width += uf.getWidth(t);
//                    x += uf.getWidth(t);
//                }
//            }
//            x = sx;
//            y += uf.getHeight(source) + 2;
//            width = 0;
//        }
        UnicodeFont uf = CustomFontRenderer.get(font);
        float xCurrent = 0;

        if(width == -1){
            ArrayList<FontElement> fontElements = validateMinecraftColor(string, color);

            for(FontElement fontElement : fontElements){
                uf.drawString((float)x+xCurrent, (float)y, fontElement.string, fontElement.color);
                xCurrent+=getStringWidth(font, fontElement.string);
            }
        } else {
            ArrayList<String> strings = splitString(string, width, ScaleGui.get(111), font);
            float yCurrent = 0;

            for(String s : strings){
                ArrayList<FontElement> fontElements = validateMinecraftColor(s, color);
                xCurrent = 0;
                for(FontElement fontElement : fontElements){
                    uf.drawString((float)(x - (type == EnumStringRenderType.DEFAULT ? 0 : type == EnumStringRenderType.RIGHT ?
                            font.width(s) : font.width(s)/2))+xCurrent, (float)y+yCurrent, fontElement.string, fontElement.color);
                    xCurrent+=getStringWidth(font, fontElement.string);
                }
                yCurrent+=uf.getHeight(s) + 2;

            }

        }

        GL11.glEnable((int)3553);
        GL11.glDisable((int)3042);
        GL11.glScalef((float)guiScale, (float)guiScale, (float)1.0f);
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

    public static ArrayList<String> splitString(String input, float maxWidth, float maxHeight, FontContainer font) {
        ArrayList<String> splitStrings = new ArrayList<>();
        float yCurrent = 0;
        float wCurrent = 0;

        String[] inputMas = input.split(" ");
        UnicodeFont uf = CustomFontRenderer.get(font);
        String element = "";

        for(int i = 0; i < inputMas.length; i++){
            String s;
            if(i+1==inputMas.length){
                s = inputMas[i]+"";
            } else {
                s = inputMas[i]+" ";
            }
            float widthM = ScaleGui.get(uf.getWidth(s));
            if(wCurrent + widthM <= maxWidth){
                wCurrent+=widthM;
                element+=s;
            } else {
                splitStrings.add(element);
                element = "";
                wCurrent = 0;
                wCurrent+=widthM;
                element+=s;
            }
        }
        if(wCurrent!=0){
            splitStrings.add(element);
        }

        return splitStrings;
    }

    private static Color getColor(char s, int color) {
        Color c = (Color)colors.get(String.valueOf(s));
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
