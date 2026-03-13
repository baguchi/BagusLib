package baguchi.bagus_lib.animation.client;

import com.google.common.collect.Maps;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.resources.Identifier;

import javax.annotation.Nullable;
import java.util.Map;

//this Keyframe controller make handle Keyframe on Client Event
public class BaguKeyFrameController {
    private final Map<Identifier, KeyframeAnimation> keyframeMap = Maps.newHashMap();

    public BaguKeyFrameController() {
    }

    //DON'T USE DIRECTLY
    @Deprecated
    public void addAnimationKeyframe(Map.Entry<Identifier, KeyframeAnimation> entry) {
        this.keyframeMap.put(entry.getKey(), entry.getValue());
    }

    @Nullable
    public KeyframeAnimation getKeyframe(Identifier index) {
        if (keyframeMap.containsKey(index)) {
            return keyframeMap.get(index);
        }
        return null;
    }
}
