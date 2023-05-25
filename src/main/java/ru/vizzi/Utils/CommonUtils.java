package ru.vizzi.Utils;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import ru.vizzi.Utils.obf.IgnoreObf;

@IgnoreObf
public class CommonUtils {
	
    public static boolean isServerSide() {
        return FMLCommonHandler.instance().getSide() == Side.SERVER;
    }
    
    public static <T> void unregisterFMLEvents(Object object) {
        FMLCommonHandler.instance().bus().unregister(object);
    }

    public static <T> void unregisterEvents(Object object) {
        MinecraftForge.EVENT_BUS.unregister(object);
    }
    
    public static <T> T registerEvents(Class<T> clazz) {
        try {
            T object = clazz.newInstance();
            MinecraftForge.EVENT_BUS.register(object);
            return object;
        } catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        return null;
    }

    public static <T> T registerFMLEvents(Class<T> clazz) {
        try {
            T object = clazz.newInstance();
            FMLCommonHandler.instance().bus().register(object);
            return object;
        } catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        return null;
    }

    public static void registerFMLEvents(Object object) {
        FMLCommonHandler.instance().bus().register(object);
    }

    public static void registerEvents(Object object) {
        MinecraftForge.EVENT_BUS.register(object);
    }

    public static <T> T registerAllEvents(Class<T> clazz) {
        try {
            T object = clazz.newInstance();
            FMLCommonHandler.instance().bus().register(object);
            MinecraftForge.EVENT_BUS.register(object);
            return object;
        } catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        return null;
    }
    
    public static List<Class<?>> getClassesHierarchy(Class<?> clazz) {
        List<Class<?>> list = new ArrayList<>();
        Class<?> c = clazz;
        while(c != null) {
            list.add(c);
            c = c.getSuperclass();
        }
        return list;
    }
}
