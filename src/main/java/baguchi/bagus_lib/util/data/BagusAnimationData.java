package baguchi.bagus_lib.util.data;


import baguchi.bagus_lib.util.client.BagusAnimationUtil;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public class BagusAnimationData {

    public int animationTick;
    public boolean started;
    public final Identifier animation;
    public final int maxAnimationTick;
    public final boolean loop;


    public BagusAnimationData(Identifier animation, int maxAnimationTick) {
        this(animation, maxAnimationTick, false);
    }

    public BagusAnimationData(Identifier animation, int maxAnimationTick, boolean loop) {
        this.animation = animation;
        this.maxAnimationTick = maxAnimationTick;
        this.loop = loop;
    }

    public void tick(Entity entity) {
        if (!entity.level().isClientSide()) {

            if (this.started && this.animationTick < this.maxAnimationTick) {
                this.animationTick++;
            }
            if (this.started && this.animationTick >= this.maxAnimationTick && !this.loop) {
                this.stop(entity);
            }
        }
    }

    public void start(Entity entity) {
        if (!entity.level().isClientSide()) {
           BagusAnimationUtil.sendAnimation(entity, this.animation);

            this.animationTick = 0;
            this.started = true;
        }
    }

    public void stop(Entity entity) {
        if (!entity.level().isClientSide()) {
          BagusAnimationUtil.sendStopAnimation(entity, this.animation);


            this.animationTick = 0;
            this.started = false;
        }
    }

    public Identifier getAnimation() {
        return animation;
    }

    public int getMaxAnimationTick() {
        return maxAnimationTick;
    }

    public int getAnimationTick() {
        return animationTick;
    }
}
