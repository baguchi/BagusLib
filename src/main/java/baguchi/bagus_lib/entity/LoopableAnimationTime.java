package baguchi.bagus_lib.entity;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;

public class LoopableAnimationTime {
    private final AnimationState animationState = new AnimationState();
    private final int animationLength;
    private int animationTick;

    public LoopableAnimationTime(int animationLength) {
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

    public void tick(Entity entity) {
        if (this.animationTick < this.animationLength) {
            this.animationTick++;
            this.animationState.startIfStopped(entity.tickCount);
        }

        if (this.animationTick >= this.animationLength) {
            this.animationState.stop();
        }
    }

    public void start(Entity entity) {
        this.animationTick = 0;
    }
}
