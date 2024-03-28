package bagu_chan.bagus_lib.client.event;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class BagusModelEvent extends Event {
    private LivingEntity entityIn;

    private EntityModel model;
    private float partialTick;

    public BagusModelEvent(LivingEntity entityIn, EntityModel model, float partialTick) {
        this.entityIn = entityIn;
        this.model = model;
        this.partialTick = partialTick;
    }

    public Entity getEntityIn() {
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

        private RenderType renderType;

        public Render(LivingEntity entityIn, EntityModel model, float partialTick, RenderType renderType) {
            super(entityIn, model, partialTick);
            this.renderType = renderType;
        }

        public RenderType getRenderType() {
            return renderType;
        }
    }
}