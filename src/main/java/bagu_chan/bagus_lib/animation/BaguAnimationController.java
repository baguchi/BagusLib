package bagu_chan.bagus_lib.animation;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

//this animation controller make handle animation in mixin
public class BaguAnimationController<T extends Entity> {
    private final T entity;
    private final List<AnimationState> animationStateList;

    public BaguAnimationController(T entity, int animationSize) {
        this.entity = entity;
        this.animationStateList = Lists.newArrayList();
        for (int i = 0; i < animationSize; i++) {
            this.animationStateList.add(new AnimationState());
        }
    }

    public void startAnimation(int index) {
        this.animationStateList.get(index).start(entity.tickCount);
    }

    public void stopAllAnimation() {
        this.animationStateList.forEach(AnimationState::stop);
    }

    public AnimationState getAnimationState(int index) {
        return animationStateList.get(index);
    }
}
