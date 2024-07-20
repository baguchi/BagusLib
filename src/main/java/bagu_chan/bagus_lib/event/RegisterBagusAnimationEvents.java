package bagu_chan.bagus_lib.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegisterBagusAnimationEvents extends Event {
    private final Map<ResourceLocation, AnimationState> animationStateMap = new HashMap<>();
    private Map<ResourceLocation, AnimationState> animationStateUnModifiableMap = Collections.unmodifiableMap(animationStateMap);
    private final Entity entity;

    public RegisterBagusAnimationEvents(Entity entity) {
        this.entity = entity;
    }

    public void addAnimationState(ResourceLocation name) {
        this.animationStateMap.put(name, new AnimationState());
        this.animationStateUnModifiableMap = Collections.unmodifiableMap(animationStateMap);
    }

    public Map<ResourceLocation, AnimationState> getAnimationStateMap() {
        return animationStateUnModifiableMap;
    }

    public Entity getEntity() {
        return entity;
    }

}
