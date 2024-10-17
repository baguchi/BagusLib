package baguchi.bagus_lib.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.InteractionHand;
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