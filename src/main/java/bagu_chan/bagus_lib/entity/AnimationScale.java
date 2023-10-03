package bagu_chan.bagus_lib.entity;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

/*
 * This class using For animation scaling(Such as when changing from walking to running animation)
 */

public class AnimationScale {

    private boolean flag;
    private float animationScale;
    private float animationScale0;
    private final float amount;

    public AnimationScale() {
        this(0.1F);
    }

    public AnimationScale(float amount) {
        this.amount = amount;
    }


    public void tick(Entity entity) {
        this.animationScale0 = this.animationScale;
        if (flag) {
            this.animationScale = Mth.clamp(this.animationScale + amount, 0.0F, 1.0F);
        } else {
            this.animationScale = Mth.clamp(this.animationScale - amount, 0.0F, 1.0F);
        }
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public float getAnimationScale(float p_29570_) {
        return Mth.lerp(p_29570_, this.animationScale0, this.animationScale);
    }
}
