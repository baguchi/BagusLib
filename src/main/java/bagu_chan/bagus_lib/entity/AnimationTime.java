package bagu_chan.bagus_lib.entity;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

public class AnimationTime {
    private final AnimationState animationState = new AnimationState();
    private final int animationLength;
    private int animationTick;

    public AnimationTime(int animationLength) {
        this.animationLength = animationLength;
    }


    public AnimationState getAnimationState() {
        return animationState;
    }

    public int getAnimationLength() {
        return animationLength;
    }

    public int getAnimationTick() {
        return animationTick;
    }

    public void tick() {
        if (this.animationTick < this.animationLength) {
            this.animationTick++;
        }

        if (this.animationTick >= this.animationLength) {
            this.animationState.stop();
        }
    }

    public void start(Entity entity) {
        this.animationState.start(entity.tickCount);
        this.animationTick = 0;
    }
}
