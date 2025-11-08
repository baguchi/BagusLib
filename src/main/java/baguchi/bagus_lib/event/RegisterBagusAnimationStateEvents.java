package baguchi.bagus_lib.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;

import java.util.*;

public class RegisterBagusAnimationStateEvents extends Event {
    private final Map<ResourceLocation, AnimationState> animationStateMap = new HashMap<>();
    private Map<ResourceLocation, AnimationState> animationStateUnModifiableMap = Collections.unmodifiableMap(animationStateMap);
    private final List<ResourceLocation> animationStateFirstPersonPlayableList = new ArrayList<>();
    private List<ResourceLocation> animationStateFirstPersonPlayableUnModifiableList = Collections.unmodifiableList(animationStateFirstPersonPlayableList);

    private final Entity entity;

    public RegisterBagusAnimationStateEvents(Entity entity) {
        this.entity = entity;
    }

    public void addAnimationState(ResourceLocation name) {
        this.animationStateMap.put(name, new AnimationState());
        this.animationStateUnModifiableMap = Collections.unmodifiableMap(animationStateMap);
    }

    public void addFirstPersonPlayableAnimationState(ResourceLocation name) {
        this.animationStateMap.put(name, new AnimationState());
        this.animationStateUnModifiableMap = Collections.unmodifiableMap(animationStateMap);
        this.animationStateFirstPersonPlayableList.add(name);
        this.animationStateFirstPersonPlayableUnModifiableList = Collections.unmodifiableList(animationStateFirstPersonPlayableList);
    }

    public Map<ResourceLocation, AnimationState> getAnimationStateMap() {
        return animationStateUnModifiableMap;
    }

    public List<ResourceLocation> getAnimationStateFirstPersonPlayableList() {
        return animationStateFirstPersonPlayableUnModifiableList;
    }

    public Entity getEntity() {
        return entity;
    }

}
