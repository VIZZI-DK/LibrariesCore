package ru.vizzi.Utils.player;

import java.util.HashMap;
import java.util.Set;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lombok.SneakyThrows;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.Clone;
import ru.vizzi.Utils.eventhandler.RegistryEvent;

@RegistryEvent
public class ExtendedManager {
	
	private static ExtendedManager instance;
	
	private static HashMap<String, AbstractExtendedPlayer> hashExtended = new HashMap<>();
	
	private Set<Class<?>> scanClasses() {
        Reflections reflections = new Reflections("ru.vizzi", new TypeAnnotationsScanner(), new SubTypesScanner());
        return reflections.getTypesAnnotatedWith(RegistryExtended.class);
    }

	
	@SubscribeEvent
	public void onClonePlayer(Clone event) {
		for(AbstractExtendedPlayer extended : hashExtended.values()) {
			extended.get(event.entityPlayer).copyNBT(extended.get(event.original));
		}
	}

	@SubscribeEvent
	@SneakyThrows
	public void onPlayerConstructing(EntityEvent.EntityConstructing event) {
		if(event.entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.entity;
			for(AbstractExtendedPlayer extended : hashExtended.values()) {
				if(player.getExtendedProperties(extended.getExtendedName()) == null) {
					player.registerExtendedProperties(extended.getExtendedName(), extended.getClass().getConstructor(EntityPlayer.class).newInstance(player));
				}
			}
		}
	}
	
	
	
	
	public static ExtendedManager getInstances() {
		if(instance == null) {
			instance = new ExtendedManager();
		}
		return instance;
	}

}
