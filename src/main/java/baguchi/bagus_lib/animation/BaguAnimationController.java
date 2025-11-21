package baguchi.bagus_lib.animation;

import baguchi.bagus_lib.BagusConfigs;
import baguchi.bagus_lib.BagusLib;
import com.google.common.collect.Maps;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

//this animation controller make handle animation on Event
public class BaguAnimationController<T extends Entity> {
    private final T entity;
    private final Map<Identifier, AnimationState> animationStateMap = Maps.newHashMap();
    private final List<Identifier> animationStateFirstPersonList = new ArrayList<>();

    public BaguAnimationController(T entity) {
        this.entity = entity;
    }

    //DON'T USE DIRECTLY
    @Deprecated
    public void addAnimation(Identifier resourceLocation) {
        this.animationStateMap.put(resourceLocation, new AnimationState());
    }

    @Deprecated
    public void addFirstPersonAnimation(Identifier resourceLocation) {
        if (!this.animationStateFirstPersonList.contains(resourceLocation)) {
            this.animationStateFirstPersonList.add(resourceLocation);
        }
    }

    public void startAnimation(Identifier resourceLocation) {
        if (this.animationStateMap.get(resourceLocation) != null) {
            this.animationStateMap.get(resourceLocation).start(entity.tickCount);
        } else {
            BagusLib.LOGGER.error("Animation(" + resourceLocation.toString() + ") has not found!");
        }
    }

    public void stopAnimation(Identifier resourceLocation) {
        if (this.animationStateMap.get(resourceLocation) != null) {
            this.animationStateMap.get(resourceLocation).stop();
        } else {
            BagusLib.LOGGER.error("Animation(" + resourceLocation.toString() + ") has not found!");
        }
    }

    public void stopAllAnimation() {
        this.animationStateMap.values().forEach(AnimationState::stop);
    }

    public boolean hasPlayingAnimation() {
        if (!BagusConfigs.COMMON.playableFirstPerson.get()) {
            return false;
        }
        Optional<Map.Entry<Identifier, AnimationState>> playtest = this.animationStateMap.entrySet().stream().filter(animationStateEntry -> {
            return animationStateEntry.getValue().isStarted() && this.animationStateFirstPersonList.contains(animationStateEntry.getKey());
        }).findAny();
        return playtest.isPresent();
    }

    public AnimationState getAnimationState(Identifier index) {
        if (animationStateMap.containsKey(index)) {
            return animationStateMap.get(index);
        }
        return new AnimationState();
    }
}
