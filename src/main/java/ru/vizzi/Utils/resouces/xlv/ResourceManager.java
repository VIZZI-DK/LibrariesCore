package ru.vizzi.Utils.resouces.xlv;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.ResourceLocation;
import ru.vizzi.Utils.config.Flex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class ResourceManager {

    private static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(2);

   // private static final List<ResourceLoader<?>> RESOURCE_LOADER_LIST = ImmutableList.of(new ResourceLoaderDDS(), new ResourceLoaderPNG(), new ResourceLoaderOBJ());
   private static final List<ResourceLoader<?>> RESOURCE_LOADER_LIST = ImmutableList.of(new ResourceLoaderOBJ());
    private static final Queue<FutureTask<Object>> SYNC_QUEUE = new LinkedList<>();
    private static final List<AbstractResource> LOADED_RESOURCES = new ArrayList<>();

    private static final int MAX_RESOURCE_LIFE_TIME = 60000;
    private static int loadedResourcesInCurrentFrame;

    public static void loadResource(AbstractResource resource) {
        if (resource.getLoadingState() == ResourceLoadingState.WAIT) {
            if (!checkIfResourceAlreadyLoaded(resource)) {
                resource.setLoadingState(ResourceLoadingState.ASYNC);
                EXECUTOR_SERVICE.submit(() -> loadAsync(resource));
                synchronized (LOADED_RESOURCES) {
                    LOADED_RESOURCES.add(resource);
                }
            }
        }
    }

    public static boolean checkIfResourceAlreadyLoaded(AbstractResource resource) {
        synchronized(LOADED_RESOURCES) {
            int i = LOADED_RESOURCES.indexOf(resource);
            if (i >= 0) {
                AbstractResource otherResource = LOADED_RESOURCES.get(i);
                if (otherResource.getLoadingState() == ResourceLoadingState.DONE) {
                    LOADED_RESOURCES.add(resource);
                    resource.setFrom(otherResource);
                    resource.setLoadingState(ResourceLoadingState.DONE);
                    return true;
                } else if (otherResource.getLoadingState() == ResourceLoadingState.ASYNC || otherResource.getLoadingState() == ResourceLoadingState.SYNC) {
                    LOADED_RESOURCES.add(resource);
                    resource.setLoadingState(ResourceLoadingState.SYNC);
                    return true;
                }
            }

            return false;
        }
    }

    public static void updateSync() {
        if(loadedResourcesInCurrentFrame > 0) {
            loadedResourcesInCurrentFrame--;
        }

        synchronized (SYNC_QUEUE) {
            if(loadedResourcesInCurrentFrame < 1) {
                if (!SYNC_QUEUE.isEmpty()) {
                    FutureTask<Object> poll = SYNC_QUEUE.poll();
                    poll.run();
                    loadedResourcesInCurrentFrame++;
                }
            }
        }

        if(loadedResourcesInCurrentFrame < 1) {
            synchronized (LOADED_RESOURCES) {
                for (int i = 0; i < LOADED_RESOURCES.size(); i++) {
                    AbstractResource resource = LOADED_RESOURCES.get(i);
                    resource.update();

                    if(unloadResource(resource)) {
                        LOADED_RESOURCES.remove(i);
                        unloadSameResources(resource);
                        break;
                    }
                }
            }
        }
    }

    private static void loadAsync(AbstractResource resource) {
        resource.loadFromFile();
        resource.setLoadingState(ResourceLoadingState.SYNC);
        synchronized (SYNC_QUEUE) {
            SYNC_QUEUE.add(new FutureTask<>(() -> {
                resource.loadToMemory();
                resource.setLoadingState(ResourceLoadingState.DONE);
                setDataForSameResourcesWithLoadingState(resource);
                return null;
            }));
        }
    }

    private static void setDataForSameResourcesWithLoadingState(AbstractResource resource) {
        synchronized (LOADED_RESOURCES) {
            for(AbstractResource otherResource : LOADED_RESOURCES) {
                if(otherResource != resource && otherResource.getLoadingState() == ResourceLoadingState.SYNC && otherResource.equals(resource)) {
                    otherResource.setFrom(resource);
                    otherResource.setLoadingState(ResourceLoadingState.DONE);
                }
            }
        }
    }

    private static void loadSync(ResourceLoader<?> resourceLoader, ResourceLocationStateful resourceLocationStateful) {
        resourceLocationStateful.setLoadingState(ResourceLoadingState.SYNC);
        Object o = resourceLoader.loadAsync(resourceLocationStateful);
        resourceLoader.loadSync(resourceLocationStateful, o);
        resourceLocationStateful.setLoadingState(ResourceLoadingState.DONE);
    }

    private static boolean unloadResource(AbstractResource resource) {
        if (resource.getLoadingState() == ResourceLoadingState.DONE && resource.getLifeTime() >= MAX_RESOURCE_LIFE_TIME && !isOtherSameResourcesInUse(resource)) {
            resource.unload();
            resource.setLoadingState(ResourceLoadingState.WAIT);
            return true;
        }

        return  false;
    }

    private static void unloadSameResources(AbstractResource resource) {
        synchronized(LOADED_RESOURCES) {
            for (int i = 0; i < LOADED_RESOURCES.size(); i++) {
                AbstractResource otherResource = LOADED_RESOURCES.get(i);
                if (otherResource != resource && otherResource.getLifeTime() >= MAX_RESOURCE_LIFE_TIME && otherResource.equals(resource)) {
                    otherResource.unload();
                    otherResource.setLoadingState(ResourceLoadingState.WAIT);
                    LOADED_RESOURCES.remove(i);
                    i--;
                }
            }
        }
    }

    private static boolean isOtherSameResourcesInUse(AbstractResource resource) {
        synchronized(LOADED_RESOURCES) {
            for (AbstractResource loadedResource : LOADED_RESOURCES) {
                if(loadedResource != resource && loadedResource.getLifeTime() < MAX_RESOURCE_LIFE_TIME && loadedResource.equals(resource)) {
                    return true;
                }
            }

            return false;
        }
    }

    public static ResourceLoader<?> getResourceLoader(String format) {
        synchronized (RESOURCE_LOADER_LIST) {
            return Flex.getCollectionElement(RESOURCE_LOADER_LIST, resourceLoader -> resourceLoader.getFormat().equals(format));
        }
    }

    private static ResourceLoader<?> getResourceLoader(ResourceLocation resourceLocation) {
        String format = resourceLocation.getResourcePath().substring(resourceLocation.getResourcePath().lastIndexOf(".") + 1);
        synchronized (RESOURCE_LOADER_LIST) {
            return Flex.getCollectionElement(RESOURCE_LOADER_LIST, resourceLoader -> resourceLoader.getFormat().equals(format));
        }
    }

    public static void clear() {
        synchronized(LOADED_RESOURCES) {
            for (AbstractResource loadedResource : LOADED_RESOURCES) {
                loadedResource.unload();
            }

            LOADED_RESOURCES.clear();
        }
    }
}

