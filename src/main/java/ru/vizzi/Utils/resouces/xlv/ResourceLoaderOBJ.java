package ru.vizzi.Utils.resouces.xlv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.vizzi.Utils.model.MeshData;
import ru.vizzi.Utils.model.Model;
import ru.vizzi.Utils.model.ModelLoader;

import java.util.List;

public class ResourceLoaderOBJ extends ResourceLoader<ResourceLoaderOBJ.ModelContainer> {

    @Getter
    @RequiredArgsConstructor
    public static class ModelContainer {
        private final List<MeshData> meshData;
        private final Model model;
    }

    public ResourceLoaderOBJ() {
        super("obj");
    }

    @Override
    public ModelContainer loadAsync(ResourceLocationStateful resourceLocationStateful) {
        Model model = ModelLoader.getModel(resourceLocationStateful.getResourceLocation());
        if (model == null) {
            ModelLoader.addModel(model = new Model(resourceLocationStateful.getResourceLocation()));
        }
        return new ModelContainer(ModelLoader.readModelMeshes(model), model);
    }

    @Override
    protected void loadSync0(ResourceLocationStateful resourceLocationStateful, ModelContainer modelContainer) {
        ModelLoader.loadModelMeshes(modelContainer.getModel(), modelContainer.getMeshData());
    }

    @Override
    public boolean isLoaded(ResourceLocationStateful resourceLocationStateful) {
        return ModelLoader.isLoaded(resourceLocationStateful.getResourceLocation());
    }

    @Override
    public void deleteResource(ResourceLocationStateful resourceLocationStateful) {
        ModelLoader.deleteModel(resourceLocationStateful.getResourceLocation());
    }

    @Override
    public void deleteAll() {
        throw new UnsupportedOperationException();
    }
}
