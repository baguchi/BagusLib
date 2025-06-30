package bagu_chan.bagus_lib.animation;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.BagusLib;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//this animation controller make handle animation on Event
public class BaguAnimationController<T extends Entity> {
    private final T entity;
    private final Map<ResourceLocation, AnimationState> animationStateMap = Maps.newHashMap();
    private final List<ResourceLocation> animationStateFirstPersonList = new ArrayList<>();

    public BaguAnimationController(T entity) {
        this.entity = entity;
    }

    //DON'T USE DIRECTLY
    @Deprecated
    public void addAnimation(ResourceLocation resourceLocation) {
        this.animationStateMap.put(resourceLocation, new AnimationState());
    }

    @Deprecated
    public void addFirstPersonAnimation(ResourceLocation resourceLocation) {
        if (!this.animationStateFirstPersonList.contains(resourceLocation)) {
            this.animationStateFirstPersonList.add(resourceLocation);
        }
    }

    public void startAnimation(ResourceLocation resourceLocation) {
        if (this.animationStateMap.get(resourceLocation) != null) {
            this.animationStateMap.get(resourceLocation).start(entity.tickCount);
        } else {
            BagusLib.LOGGER.error("Animation(" + resourceLocation.toString() + ") has not found!");
        }
    }

    public void stopAnimation(ResourceLocation resourceLocation) {
        if (this.animationStateMap.get(resourceLocation) != null) {
            this.animationStateMap.get(resourceLocation).stop();
        } else {
            BagusLib.LOGGER.error("Animation(" + resourceLocation.toString() + ") has not found!");
        }
    }

    public void stopAllAnimation() {
        this.animationStateMap.values().forEach(AnimationState::stop);
    }

    public AnimationState getAnimationState(ResourceLocation index) {
        if (animationStateMap.containsKey(index)) {
            return animationStateMap.get(index);
        }
        return new AnimationState();
    }

    public boolean hasPlayingAnimation() {
        if (!BagusConfigs.CLIENT.playableFirstPerson.get()) {
            return false;
        }
        Optional<Map.Entry<ResourceLocation, AnimationState>> playtest = this.animationStateMap.entrySet().stream().filter(animationStateEntry -> {
            return animationStateEntry.getValue().isStarted() && this.animationStateFirstPersonList.contains(animationStateEntry.getKey());
        }).findAny();
        return playtest.isPresent();
    }
}
