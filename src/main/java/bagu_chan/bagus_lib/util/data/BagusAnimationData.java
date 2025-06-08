package bagu_chan.bagus_lib.util.data;

import bagu_chan.bagus_lib.util.client.AnimationUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class BagusAnimationData {

    public int animationTick;
    public boolean started;
    public final ResourceLocation animation;
    public final int maxAnimationTick;

    public BagusAnimationData(ResourceLocation animation, int maxAnimationTick) {
        this.animation = animation;
        this.maxAnimationTick = maxAnimationTick;
    }

    public void tick(Entity entity) {
        if (!entity.level().isClientSide()) {

            if (this.started && this.animationTick < this.maxAnimationTick) {
                this.animationTick++;
            }
            if (this.started && this.animationTick >= this.maxAnimationTick) {
                this.stop(entity);
            }
        }
    }

    public void start(Entity entity) {
        if (!entity.level().isClientSide()) {
            AnimationUtil.sendAnimation(entity, this.animation);

            this.animationTick = 0;
            this.started = true;
        }
    }

    public void stop(Entity entity) {
        if (!entity.level().isClientSide()) {
            AnimationUtil.sendStopAnimation(entity, this.animation);

            this.animationTick = 0;
            this.started = false;
        }
    }

    public ResourceLocation getAnimation() {
        return animation;
    }

    public int getMaxAnimationTick() {
        return maxAnimationTick;
    }

    public int getAnimationTick() {
        return animationTick;
    }
}
