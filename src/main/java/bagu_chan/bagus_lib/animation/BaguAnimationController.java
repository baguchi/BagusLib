package bagu_chan.bagus_lib.animation;

import bagu_chan.bagus_lib.BagusLib;
import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

import java.util.Map;

//this animation controller make handle animation on Event
public class BaguAnimationController<T extends Entity> {
    private final T entity;
    private final Map<ResourceLocation, AnimationState> animationStateMap = Maps.newHashMap();

    public BaguAnimationController(T entity) {
        this.entity = entity;
    }

    //DON'T USE DIRECTLY
    @Deprecated
    public void addAnimation(ResourceLocation resourceLocation) {
        this.animationStateMap.put(resourceLocation, new AnimationState());
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
}
