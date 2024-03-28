package bagu_chan.bagus_lib.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.Event;

public class BagusModelEvent extends Event {
    private LivingEntity entityIn;

    private EntityModel model;
    private float partialTick;

    public BagusModelEvent(LivingEntity entityIn, EntityModel model, float partialTick) {
        this.entityIn = entityIn;
        this.model = model;
        this.partialTick = partialTick;
    }

    public Entity getEntity() {
        return entityIn;
    }

    public EntityModel getModel() {
        return model;
    }

    public float getPartialTick() {
        return partialTick;
    }

    public static class Scale extends BagusModelEvent {
        private PoseStack poseStack;

        public Scale(LivingEntity entityIn, EntityModel model, float partialTick, PoseStack poseStack) {
            super(entityIn, model, partialTick);
            this.poseStack = poseStack;
        }

        public PoseStack getPoseStack() {
            return poseStack;
        }
    }

    public static class Init extends BagusModelEvent {

        public Init(LivingEntity entityIn, EntityModel model, float partialTick) {
            super(entityIn, model, partialTick);
        }
    }

    public static class PreAnimate extends BagusModelEvent {

        public PreAnimate(LivingEntity entityIn, EntityModel model, float partialTick) {
            super(entityIn, model, partialTick);
        }

        public float getAgeInTick() {
            return (float) getPartialTick() + getEntityIn().tickCount;
        }
    }

    public static class PostAnimate extends BagusModelEvent {

        public PostAnimate(LivingEntity entityIn, EntityModel model, float partialTick) {
            super(entityIn, model, partialTick);
        }

        public float getAgeInTick() {
            return (float) getPartialTick() + getEntityIn().tickCount;
        }
    }

    public static class Render extends BagusModelEvent {

        private MultiBufferSource multiBufferSource;
        private PoseStack poseStack;
        private final LivingEntityRenderer livingEntityRenderer;

        public Render(LivingEntity entityIn, EntityModel model, float partialTick, MultiBufferSource multiBufferSource, PoseStack poseStack, LivingEntityRenderer livingEntityRenderer) {
            super(entityIn, model, partialTick);
            this.multiBufferSource = multiBufferSource;
            this.poseStack = poseStack;
            this.livingEntityRenderer = livingEntityRenderer;
        }

        public MultiBufferSource getMultiBufferSource() {
            return multiBufferSource;
        }

        public PoseStack getPoseStack() {
            return this.poseStack;
        }

        public LivingEntityRenderer getLivingEntityRenderer() {
            return livingEntityRenderer;
        }
    }
}