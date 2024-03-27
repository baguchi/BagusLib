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
    public static void animate(IRootModel p_232320_, AnimationDefinition p_232321_, long p_232322_, float p_232323_, Vector3f p_253861_) {
        float f = getElapsedSeconds(p_232321_, p_232322_);

        for (Map.Entry<String, List<AnimationChannel>> entry : p_232321_.boneAnimations().entrySet()) {
            Optional<ModelPart> optional = p_232320_.getBetterAnyDescendantWithName(entry.getKey());
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

                keyframe1.interpolation().apply(p_253861_, f2, akeyframe, i, j, p_232323_);
                p_288241_.target().apply(p_232330_, p_253861_);
            }));
        }
    }

    private static float getElapsedSeconds(AnimationDefinition p_232317_, long p_232318_) {
        float f = (float) p_232318_ / 1000.0F;
        return p_232317_.looping() ? f % p_232317_.lengthInSeconds() : f;
    }
}
