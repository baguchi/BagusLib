package bagu_chan.bagus_lib.api.client;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class KeyframeBagusAnimations {
    //[VanillaCopy] of {@link net.minecraft.client.animation.KeyframeAnimations}
    public static void animate(IRootModel rootModel, AnimationDefinition animationDefinition, long time, float scale, Vector3f animationCache) {
        float f = getElapsedSeconds(animationDefinition, time);

        for (Map.Entry<String, List<AnimationChannel>> entry : animationDefinition.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = rootModel.getBetterAnyDescendantWithName(entry.getKey());
            List<AnimationChannel> list = entry.getValue();
            optional.ifPresent(p_232330_ -> list.forEach(p_288241_ -> {
                Keyframe[] akeyframe = p_288241_.keyframes();
                int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, p_232315_ -> f <= akeyframe[p_232315_].timestamp()) - 1);
                int j = Math.min(akeyframe.length - 1, i + 1);
                Keyframe keyframe = akeyframe[i];
                Keyframe keyframe1 = akeyframe[j];
                float f1 = f - keyframe.timestamp();
                float f2;
                if (j != i) {
                    f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
                } else {
                    f2 = 0.0F;
                }

                keyframe1.interpolation().apply(animationCache, f2, akeyframe, i, j, scale);
                p_288241_.target().apply(p_232330_, animationCache);
            }));
        }
    }

    private static float getElapsedSeconds(AnimationDefinition p_232317_, long p_232318_) {
        float f = (float) p_232318_ / 1000.0F;
        return p_232317_.looping() ? f % p_232317_.lengthInSeconds() : f;
    }
}
