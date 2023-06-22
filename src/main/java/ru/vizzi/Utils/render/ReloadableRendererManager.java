package ru.vizzi.Utils.render;

import java.util.ArrayList;
import java.util.List;

public class ReloadableRendererManager {

    private static final List<IReloadableRenderer> renderers = new ArrayList<>();

    public static void registerRenderer(IReloadableRenderer renderer) {
        renderers.add(renderer);
    }

    public static void reload() {
        renderers.forEach(IReloadableRenderer::reload);
    }
}
