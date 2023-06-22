package ru.vizzi.Utils.resouces.xlv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.minecraft.util.ResourceLocation;

@ToString
@Setter
@Getter
@RequiredArgsConstructor
public class ResourceLocationStateful {

    private static final long DEFAULT_REMOVAL_TIME_MILLS = 300000L;

    public static class Builder {
        private String domain;
        private long removalTimeMills;

        public Builder withDomain(String domain) {
            this.domain = domain;
            return this;
        }

        public Builder temporary() {
            this.removalTimeMills = DEFAULT_REMOVAL_TIME_MILLS;
            return this;
        }

        public Builder temporary(long removalTimeMills) {
            this.removalTimeMills = removalTimeMills;
            return this;
        }

        public ResourceLocationStateful of(String path) {
            if(path == null) return null;
            ResourceLocationStateful resourceLocationStateful = new ResourceLocationStateful((domain != null ? domain + ":" : "") + path);
            resourceLocationStateful.setRemovalTimeMills(removalTimeMills);
            return resourceLocationStateful;
        }

        public static Builder builder() {
            return new Builder();
        }
    }

    private final ResourceLocation resourceLocation;
    private volatile ResourceLoadingState loadingState = ResourceLoadingState.WAIT;
    private volatile long creationTimeMills;
    private long removalTimeMills;

    public ResourceLocationStateful(String path) {
        this.resourceLocation = new ResourceLocation(path);
    }

    public ResourceLocationStateful(String domain, String path) {
        this.resourceLocation = new ResourceLocation(domain, path);
    }

    private void setRemovalTimeMills(long removalTimeMills) {
        this.removalTimeMills = removalTimeMills;
    }

    public void updateRemovalTime() {
        creationTimeMills = System.currentTimeMillis();
    }
}
