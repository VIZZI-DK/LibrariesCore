package ru.vizzi.Utils.resouces;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.minecraft.util.ResourceLocation;
import ru.vizzi.Utils.EventLoadResource;
import ru.vizzi.Utils.eventhandler.RegistryEvent;

@RequiredArgsConstructor
@RegistryEvent
public class PreLoadableResourceManager {

    private final ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);

    private final Queue<FutureTask<Object>> asyncQueue = new LinkedList<>();
    private final Queue<FutureTask<Object>> syncQueue = new LinkedList<>();
    private final PreLoadableResourceScanner preLoadableResourceScanner = new PreLoadableResourceScanner();

    public void loadAll() {
        processFields(preLoadableResourceScanner.scanFields());
        for (Class<?> clazz : preLoadableResourceScanner.scanClasses()) {
            Field[] fields = clazz.getFields();
            processFields(Arrays.asList(fields));
        }

        int totalTasks = asyncQueue.size();

        while(!asyncQueue.isEmpty()) {
            FutureTask<Object> poll = asyncQueue.poll();
            if(poll != null) {
                executorService.submit(poll);
            } else {
                if (!asyncQueue.remove(null)) {
                    asyncQueue.clear();
                }
            }
        }
        while(executorService.getCompletedTaskCount() != totalTasks) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        while(!syncQueue.isEmpty()) {
            syncQueue.poll().run();
        }

        executorService.shutdown();
      //  XlvsCoreCommon.EVENT_BUS.post(new PreloadableResourcesLoadPostEvent());
    }

    @SneakyThrows
    private void processFields(Collection<Field> fields) {
        for (Field field : fields) {
            if(field.getType() == ResourceLocation.class && Modifier.isPublic(field.getModifiers()) && Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
                ResourceLocation resourceLocation = (ResourceLocation) field.get(null);
                if(resourceLocation == null) continue;
                for (ResourceType value : ResourceType.values()) {
                    if (resourceLocation.getResourcePath().endsWith(value.getFormatString())) {
                        try {
                            asyncQueue.add(new FutureTask<>(() -> {
                                Object apply = value.getAsyncFunction().apply(field, resourceLocation);
                                synchronized (syncQueue) {
                                    syncQueue.add(new FutureTask<>((ThrowableRunnable) () -> value.getSyncConsumer().accept(resourceLocation, apply), null));
                                }
                            }, null));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void event(EventLoadResource event) {
        loadAll();
    }
}
