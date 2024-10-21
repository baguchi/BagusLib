package baguchi.bagus_lib.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.AnimationState;
import net.neoforged.bus.api.Event;

public abstract class BagusModelEvent extends Event {
    private final LivingEntityRenderState entityIn;

    private final EntityModel model;

    public BagusModelEvent(LivingEntityRenderState entityIn, EntityModel model) {
        this.entityIn = entityIn;
        this.model = model;
    }

    public LivingEntityRenderState getEntityRenderState() {
        return entityIn;
    }

    public EntityModel getModel() {
        return model;
    }

    public void animate(AnimationState p_364820_, AnimationDefinition p_361968_, float ageInTick) {
        this.model.animate(p_364820_, p_361968_, ageInTick, 1.0F);
    }

    public void animate(AnimationState p_364820_, AnimationDefinition p_361968_, float ageInTick, float scale) {
        this.model.animate(p_364820_, p_361968_, ageInTick, scale);
    }

    public void animate(AnimationState animationState, net.neoforged.neoforge.client.entity.animation.json.AnimationHolder animation, float ageInTicks) {
        this.animate(animationState, animation.get(), ageInTicks);
    }

    public void animateWalk(AnimationDefinition animation, float limbSwing, float limbSwingAmount, float maxAnimationSpeed, float animationScaleFactor) {
        this.model.animateWalk(animation, limbSwing, limbSwingAmount, maxAnimationSpeed, animationScaleFactor);
    }

    public void animateWalk(net.neoforged.neoforge.client.entity.animation.json.AnimationHolder animation, float limbSwing, float limbSwingAmount, float maxAnimationSpeed, float animationScaleFactor) {
        this.model.animateWalk(animation.get(), limbSwing, limbSwingAmount, maxAnimationSpeed, animationScaleFactor);
    }


    public void animate(AnimationState animationState, net.neoforged.neoforge.client.entity.animation.json.AnimationHolder animation, float ageInTicks, float speed) {
        this.model.animate(animationState, animation.get(), ageInTicks, speed);
    }

    public void applyStatic(net.neoforged.neoforge.client.entity.animation.json.AnimationHolder animation) {
        this.applyStatic(animation.get());
    }

    public void applyStatic(AnimationDefinition p_362055_) {
        this.model.applyStatic(p_362055_);
    }

    public static class Scale extends BagusModelEvent {
        private final PoseStack poseStack;

        public Scale(LivingEntityRenderState entityIn, EntityModel model, PoseStack poseStack) {
            super(entityIn, model);
            this.poseStack = poseStack;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }
    }

    public static class PostAnimate extends BagusModelEvent {

        public PostAnimate(LivingEntityRenderState entityIn, EntityModel model) {
            super(entityIn, model);
        }
    }

    public static class FirstPersonArmAnimate extends BagusModelEvent {

        private final InteractionHand arm;
        private final PoseStack poseStack;

        public FirstPersonArmAnimate(LivingEntityRenderState entityIn, EntityModel model, InteractionHand arm, PoseStack poseStack) {
            super(entityIn, model);
            this.arm = arm;
            this.poseStack = poseStack;
        }
        public InteractionHand getArm() {
            return this.arm;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }
    }
}