package bagu_chan.bagus_lib.api.client;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.AnimationState;
import org.joml.Vector3f;

import java.util.Optional;

public interface IRootModel {


    ModelPart getBagusRoot();

    Vector3f getCacheVec();

    default Optional<ModelPart> getBetterAnyDescendantWithName(String name) {
        return name.equals("root") ? Optional.of(this.getBagusRoot()) : this.getBagusRoot().getAllParts().filter((p_233400_) -> {
            return p_233400_.hasChild(name);
        }).findFirst().map((p_233397_) -> {
            return p_233397_.getChild(name);
        });
    }

    default void animateBagu(AnimationState p_233382_, AnimationDefinition p_233383_, float p_233384_) {
        this.animateBagu(p_233382_, p_233383_, p_233384_, 1.0F);
    }

    default void animateWalkBagu(AnimationDefinition p_268159_, float p_268057_, float p_268347_, float p_268138_, float p_268165_) {
        long i = (long) (p_268057_ * 50.0F * p_268138_);
        float f = Math.min(p_268347_ * p_268165_, 1.0F);
        KeyframeBagusAnimations.animate(this, p_268159_, i, f, getCacheVec());
    }

    default void animateBagu(AnimationState p_233386_, AnimationDefinition p_233387_, float p_233388_, float p_233389_) {
        p_233386_.updateTime(p_233388_, p_233389_);
        p_233386_.ifStarted(p_233392_ -> KeyframeBagusAnimations.animate(this, p_233387_, p_233392_.getAccumulatedTime(), 1.0F, getCacheVec()));
    }

    default void applyStaticBagu(AnimationDefinition p_288996_) {
        KeyframeBagusAnimations.animate(this, p_288996_, 0L, 1.0F, getCacheVec());
    }

}
