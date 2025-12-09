package baguchi.bagus_lib.event;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.neoforged.bus.api.Event;

import java.util.*;

public class RegisterBagusAnimationStateEvents extends Event {
    private final Map<Identifier, AnimationState> animationStateMap = new HashMap<>();
    private Map<Identifier, AnimationState> animationStateUnModifiableMap = Collections.unmodifiableMap(animationStateMap);
    private final List<Identifier> animationStateFirstPersonPlayableList = new ArrayList<>();
    private List<Identifier> animationStateFirstPersonPlayableUnModifiableList = Collections.unmodifiableList(animationStateFirstPersonPlayableList);

    private final Entity entity;

    public RegisterBagusAnimationStateEvents(Entity entity) {
        this.entity = entity;
    }

    public void addAnimationState(Identifier name) {
        this.animationStateMap.put(name, new AnimationState());
        this.animationStateUnModifiableMap = Collections.unmodifiableMap(animationStateMap);
    }

    public void addFirstPersonPlayableAnimationState(Identifier name) {
        this.animationStateMap.put(name, new AnimationState());
        this.animationStateUnModifiableMap = Collections.unmodifiableMap(animationStateMap);
        this.animationStateFirstPersonPlayableList.add(name);
        this.animationStateFirstPersonPlayableUnModifiableList = Collections.unmodifiableList(animationStateFirstPersonPlayableList);
    }

    public Map<Identifier, AnimationState> getAnimationStateMap() {
        return animationStateUnModifiableMap;
    }

    public List<Identifier> getAnimationStateFirstPersonPlayableList() {
        return animationStateFirstPersonPlayableUnModifiableList;
    }

    public Entity getEntity() {
        return entity;
    }

}
