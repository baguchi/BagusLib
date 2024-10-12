package bagu_chan.bagus_lib.api.client;

import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.AnimationState;
import net.neoforged.neoforge.client.entity.animation.json.AnimationHolder;
import org.joml.Vector3f;

import java.util.Optional;

public interface IRootModel {


    ModelPart getBagusRoot();

    Vector3f getCacheVec();

    default Optional<ModelPart> getBetterAnyDescendantWithName(String name) {
        if (this.getBagusRoot() == null) {
            return Optional.empty();
        }

        return name.equals("root") ? Optional.of(this.getBagusRoot()) : this.getBagusRoot().getAllParts().filter((p_233400_) -> {
            return p_233400_.hasChild(name);
        }).findFirst().map((p_233397_) -> {
            return p_233397_.getChild(name);
        });
    }

    default void animateBagu(AnimationState animationState, AnimationDefinition animationDefinition, float ageInTick) {
        this.animateBagu(animationState, animationDefinition, ageInTick, 1.0F);
    }

    default void animateWalkBagu(AnimationDefinition animationDefinition, float limbSwing, float limbSwingScale, float animationSpeedScale, float animationScale) {
        long i = (long) (limbSwing * 50.0F * animationSpeedScale);
        float f = Math.min(limbSwingScale * animationScale, 1.0F);
        KeyframeBagusAnimations.animate(this, animationDefinition, i, f, getCacheVec());
    }

    default void animateBagu(AnimationState animationState, AnimationDefinition animationDefinition, float ageInTick, float animationSpeedScale) {
        animationState.updateTime(ageInTick, animationSpeedScale);
        animationState.ifStarted(p_233392_ -> KeyframeBagusAnimations.animate(this, animationDefinition, p_233392_.getAccumulatedTime(), 1.0F, getCacheVec()));
    }

    default void applyStaticBagu(AnimationDefinition animationDefinition) {
        KeyframeBagusAnimations.animate(this, animationDefinition, 0L, 1.0F, getCacheVec());
    }


    default void animateBagu(AnimationState animationState, AnimationHolder animationDefinition, float ageInTick) {
        this.animateBagu(animationState, animationDefinition, ageInTick, 1.0F);
    }

    default void animateWalkBagu(AnimationHolder animationDefinition, float limbSwing, float limbSwingScale, float animationSpeedScale, float animationScale) {
        long i = (long) (limbSwing * 50.0F * animationSpeedScale);
        float f = Math.min(limbSwingScale * animationScale, 1.0F);
        KeyframeBagusAnimations.animate(this, animationDefinition.get(), i, f, getCacheVec());
    }

    default void animateBagu(AnimationState animationState, AnimationHolder animationDefinition, float ageInTick, float animationSpeedScale) {
        animationState.updateTime(ageInTick, animationSpeedScale);
        animationState.ifStarted(p_233392_ -> KeyframeBagusAnimations.animate(this, animationDefinition.get(), p_233392_.getAccumulatedTime(), 1.0F, getCacheVec()));
    }

    default void applyStaticBagu(AnimationHolder animationDefinition) {
        KeyframeBagusAnimations.animate(this, animationDefinition.get(), 0L, 1.0F, getCacheVec());
    }

}
