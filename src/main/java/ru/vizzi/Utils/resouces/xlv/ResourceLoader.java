package ru.vizzi.Utils.resouces.xlv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class ResourceLoader<T> {

    private final String format;

    public abstract T loadAsync(ResourceLocationStateful resourceLocationStateful);

    protected abstract void loadSync0(ResourceLocationStateful resourceLocationStateful, T t);

    public abstract boolean isLoaded(ResourceLocationStateful resourceLocationStateful);

    public abstract void deleteResource(ResourceLocationStateful resourceLocationStateful);

    public abstract void deleteAll();

    public final void loadSync(ResourceLocationStateful resourceLocation, Object o) {
        //noinspection unchecked
        loadSync0(resourceLocation, (T) o);
    }
}
