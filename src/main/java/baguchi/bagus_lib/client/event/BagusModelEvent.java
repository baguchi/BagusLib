package baguchi.bagus_lib.client.event;

import baguchi.bagus_lib.animation.BaguAnimationController;
import baguchi.bagus_lib.animation.client.BaguKeyFrameController;
import baguchi.bagus_lib.api.IBagusExtraRenderState;
import baguchi.bagus_lib.api.client.IBaguKeyframe;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.InteractionHand;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;

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

    /*
     *This animation controller using to handle AnimationState class
     */
    @Nullable
    public BaguAnimationController getBaguAnimationController() {
        return entityIn instanceof IBagusExtraRenderState bagusExtraRenderState ? bagusExtraRenderState.bagusLib$getBaguAnimationController() : null;
    }

    /*
     *This keyframe controller using to handle KeyframeAnimation class
     */
    @Nullable
    public BaguKeyFrameController getBaguKeyframeController() {
        return this.model instanceof IBaguKeyframe iBaguKeyframe ? iBaguKeyframe.getBaguKeyframeController() : null;
    }

    public EntityModel getModel() {
        return model;
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