package baguchi.bagus_lib.client.event;

import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RegisterBagusKeyframeEvents extends Event {
    private final Map<ResourceLocation, KeyframeAnimation> keyframeMap = new HashMap<>();
    private Map<ResourceLocation, KeyframeAnimation> keyframeUnModifiableMap = Collections.unmodifiableMap(keyframeMap);

    private final Model<?> model;
    private final ModelPart modelPart;

    public RegisterBagusKeyframeEvents(Model<?> model, ModelPart modelPart) {
        this.model = model;
        this.modelPart = modelPart;
    }

    public void addAnimationKeyframe(ResourceLocation name, KeyframeAnimation keyframeAnimation) {
        this.keyframeMap.put(name, keyframeAnimation);
        this.keyframeUnModifiableMap = Collections.unmodifiableMap(keyframeMap);
    }

    public Map<ResourceLocation, KeyframeAnimation> getAnimationKeyframeMap() {
        return keyframeUnModifiableMap;
    }

    public ModelPart getModelPart() {
        return modelPart;
    }

    public Model<?> getModel() {
        return model;
    }
}
