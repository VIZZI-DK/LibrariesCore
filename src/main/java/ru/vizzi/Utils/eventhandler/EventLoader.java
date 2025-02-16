package ru.vizzi.Utils.eventhandler;



import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Map;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.discovery.ASMDataTable;
import cpw.mods.fml.common.discovery.asm.ModAnnotation;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.MinecraftForge;
import ru.vizzi.Utils.LibrariesCore;

public class EventLoader {
    public void onPreInit(FMLPreInitializationEvent event) {
        boolean isGradle = false;
        try {
            Class.forName("GradleStart");
            isGradle = true;
        } catch (Exception ignored){
        }
        for (final ASMDataTable.ASMData data : event.getAsmData().getAll(RegistryEvent.class.getName())) {
            try {
//                Map<String, Object> objectMap = data.getAnnotationInfo();
//                Side side = null;
//                if(objectMap.containsKey("side")){
//                    String s =  ReflectionHelper.getPrivateValue(ModAnnotation.EnumHolder.class, (ModAnnotation.EnumHolder) objectMap.get("side"), "value");
//                    side = s.equals("CLIENT") ? Side.CLIENT : s.equals("SERVER") ? Side.SERVER: null;
//                }
//                if(!isGradle){
//                    if(side != FMLCommonHandler.instance().getSide()){
//                        continue;
//                    }
//                }
//                if (data.getAnnotationInfo().containsKey("modid")) {
//                    String modid = (String) data.getAnnotationInfo().get("modid");
//                    if(!Loader.isModLoaded(modid)){
//                        continue;
//                    }
             //   }
                LibrariesCore.logger.debug("Class event: %s", data.getClassName());
                final Object obj = Class.forName(data.getClassName()).newInstance();


                boolean hasOreGenEvent = false;
                boolean hasTerrainGenEvent = false;
                boolean hasMinecraftForgeEvent = false;
                boolean hasFMLEvent = false;
                for (String className : this.getEvents(obj)) {
                    if (className.startsWith("net.minecraftforge.event.terraingen")) {
                        if (className.startsWith("net.minecraftforge.event.terraingen.OreGenEvent")) {
                            hasOreGenEvent = true;
                        }
                        else {
                            if (!className.startsWith("net.minecraftforge.event.terraingen.PopulateChunkEvent")) {
                                continue;
                            }
                            if (className.equals("net.minecraftforge.event.terraingen.PopulateChunkEvent$PopulateChunkEvent.Populate")) {
                                hasTerrainGenEvent = true;
                            }
                            else {
                                hasMinecraftForgeEvent = true;
                            }
                        }
                    }
                    else if (className.startsWith("cpw.mods.fml.common")) {
                        hasFMLEvent = true;
                    }
                    else {
                        hasMinecraftForgeEvent = true;
                    }
                }
                if (hasOreGenEvent) {
                    MinecraftForge.ORE_GEN_BUS.register(obj);
                    LibrariesCore.logger.debug("Class %s successful registered in MinecraftForge.ORE_GEN_BUS event bus", data.getClassName());
                }
                if (hasTerrainGenEvent) {
                    MinecraftForge.TERRAIN_GEN_BUS.register(obj);
                    LibrariesCore.logger.debug("Class %s successful registered in MinecraftForge.TERRAIN_GEN_BUS event bus", data.getClassName());
                }
                if (hasMinecraftForgeEvent) {
                    MinecraftForge.EVENT_BUS.register(obj);
                    LibrariesCore.logger.debug("Class %s successful registered in MinecraftForge.EVENT_BUS event bus", data.getClassName());
                }
                if (!hasFMLEvent) {
                    continue;
                }
                FMLCommonHandler.instance().bus().register(obj);
                LibrariesCore.logger.debug("Class %s successful registered in FMLCommonHandler.instance().bus() event bus", data.getClassName());
            }
            catch (Exception error) {
                LibrariesCore.logger.debug("Cannot register event listener for %s class:\n%s", data.getClassName(), error.getStackTrace());
            }
        }
    }
    
    private ArrayList<String> getEvents(Object handler) {
        ArrayList result = new ArrayList();

        try {
           Class error = handler.getClass();
           Method[] var4 = error.getDeclaredMethods();
           int var5 = var4.length;

           for(int var6 = 0; var6 < var5; ++var6) {
              Method method = var4[var6];
              if(method.isAnnotationPresent(SubscribeEvent.class) && method.getParameterCount() == 1) {
                 Parameter param = method.getParameters()[0];
                 if(Event.class.isAssignableFrom(param.getType())) {
                    result.add(param.getType().getName());
                 }
              }
           }
        } catch (Exception var9) {
           var9.printStackTrace();
        }

        return result;
     }


}
