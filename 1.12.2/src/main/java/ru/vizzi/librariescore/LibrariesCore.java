package ru.vizzi.librariescore;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.Validate;

import ru.vizzi.librariescore.databases.DatabaseManager;
import ru.vizzi.librariescore.eventhandler.EventLoader;
import ru.vizzi.librariescore.obf.IgnoreObf;
import ru.vizzi.librariescore.resouces.CoreAPI;
import ru.vizzi.librariescore.resouces.TextureLoader;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

@IgnoreObf
@Mod(modid = LibrariesCore.MODID, name = LibrariesCore.MODNAME, version = LibrariesCore.VERSION)
public class LibrariesCore {

	@Mod.Instance(LibrariesCore.MODID)
	public static LibrariesCore instance;

	public TextureLoader textureLoader;
	
	public static final String MODID = "librariescore";
	public static final String MODNAME = "LibrariesCore";
	public static final String VERSION = "1.0.0";
	private boolean isClient = FMLCommonHandler.instance().getSide().isClient();
	public static final Logger logger = new Logger(MODID);

	private final Thread currentThread = Thread.currentThread();
	private final Queue queue = Queues.newArrayDeque();
	public static LibrariesConfig librariesConfig;
	

	@Mod.EventHandler
	   public void preInit(FMLPreInitializationEvent event) {
		librariesConfig = LibrariesConfig.getInstance();
		librariesConfig.load();
		if(librariesConfig.DEBUG){
			logger.setDebug(true);
		}

		SyncResultHandler.setMainThreadExecutor(this::runUsingMainThread);
	      if(isClient) {
	    	  textureLoader = new TextureLoader();
	    	  CoreAPI.init();
	      };
		EventLoader eventLoader = new EventLoader();
		eventLoader.onPreInit(event);
		FMLCommonHandler.instance().bus().register(this);
	      
	   }
	@Mod.EventHandler
	   public void Init(FMLInitializationEvent event) {
	
	   }
	@Mod.EventHandler
	   public void postInit(FMLPostInitializationEvent event) {
		 if(this.isClient) {
			 MinecraftForge.EVENT_BUS.post(new EventLoadResource());
		 }
		 try {

			 Class.forName("com.mysql.jdbc.Driver");
			 DatabaseManager.init();
		 } catch (Exception e){

		 }
	    
	      
	   }

	@SubscribeEvent
	public void serverUpdate(TickEvent.ServerTickEvent e){
		if(e.phase == TickEvent.Phase.START) {
			synchronized (this.queue)
			{
				while (!this.queue.isEmpty())
				{
					((FutureTask)this.queue.poll()).run();
				}
			}
		}
	}


	   @Mod.EventHandler
	   public void serverStart(FMLServerStartingEvent e){

	   }

	   @Mod.EventHandler
	   public void stopServer(FMLServerStoppingEvent e){
		if(DatabaseManager.isInit()) {
			DatabaseManager.shutdown();
		}
	   }

	@SuppressWarnings("unchecked")
    private <T> ListenableFuture<T> runUsingMainThread(Runnable runnable) {
		if(isClient) {
			return (ListenableFuture<T>) Minecraft.getMinecraft().addScheduledTask(runnable);
		} else {
			return runUsingMainThreadServer(runnable);
		}
    }

    @SuppressWarnings("unchecked")
	private <T> ListenableFuture<T> runUsingMainThread(Callable<T> runnable) {
		if(isClient) {
			return (ListenableFuture<T>) Minecraft.getMinecraft().addScheduledTask(runnable);
		} else {
			return runUsingMainThreadServer(runnable);
		}
    }

	public <T> ListenableFuture<T> runUsingMainClientThread(Runnable runnable) {
		return (ListenableFuture<T>) Minecraft.getMinecraft().addScheduledTask(runnable);
	}

	public <T> ListenableFuture<T> runUsingMainServerThread(Runnable runnable) {
		return runUsingMainThreadServer(runnable);
	}

	@SuppressWarnings("unchecked")
	public <T> ListenableFuture<T> runUsingMainClientThread(Callable<T> runnable) {
		return (ListenableFuture<T>) Minecraft.getMinecraft().addScheduledTask(runnable);

	}

	public <T> ListenableFuture<T> runUsingMainServerThread(Callable<T> runnable) {
		return runUsingMainThreadServer(runnable);
	}



	public boolean getCurrentThread()
	{
		return Thread.currentThread() == this.currentThread;
	}


	private ListenableFuture runUsingMainThreadServer(Callable callable)
	{
		Validate.notNull(callable);

		if (!this.getCurrentThread())
		{
			ListenableFutureTask listenablefuturetask = ListenableFutureTask.create(callable);
			synchronized (this.queue)
			{
				this.queue.add(listenablefuturetask);
				return listenablefuturetask;
			}
		}
		else
		{
			try
			{
				return Futures.immediateFuture(callable.call());
			}
			catch (Exception exception)
			{
				return Futures.immediateFailedCheckedFuture(exception);
			}
		}
	}

	private ListenableFuture runUsingMainThreadServer(Runnable runnable)
	{
		Validate.notNull(runnable);
		return this.runUsingMainThreadServer(Executors.callable(runnable));
	}
}
