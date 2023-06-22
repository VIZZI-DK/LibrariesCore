package ru.vizzi.Utils.resouces.xlv;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.ResourceLocation;
import ru.vizzi.Utils.gui.drawmodule.AnimationHelper;

public abstract class AbstractResource implements IResource {

    @Getter protected final ResourceLocation resourceLocation;
    @Getter @Setter protected ResourceLoadingState loadingState = ResourceLoadingState.WAIT;
    @Getter protected float lifeTime;

    public AbstractResource(ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }

    public void update() {
        lifeTime += AnimationHelper.getAnimationSpeed();
    }

    public void resetLifeTime() {
        lifeTime = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractResource resource = (AbstractResource) o;
        return resourceLocation.equals(resource.resourceLocation);
    }
}

