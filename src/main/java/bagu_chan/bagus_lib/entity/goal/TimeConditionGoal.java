package bagu_chan.bagus_lib.entity.goal;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public class TimeConditionGoal extends Goal {
    protected final Mob mob;
    protected int tick;
    private int cooldown;
    private int maxCooldown;
    private int maxActiveTime;
    private final UniformInt timeBetweenCooldown;
    private final UniformInt timeBetween;

    public TimeConditionGoal(Mob mob, UniformInt cooldown) {
        this(mob, cooldown, cooldown);
    }

    public TimeConditionGoal(Mob mob, UniformInt cooldown, UniformInt time) {
        this.mob = mob;
        this.timeBetweenCooldown = cooldown;
        this.timeBetween = time;
    }

    @Override
    public boolean canUse() {
        if (this.maxCooldown <= 0) {
            this.maxCooldown = timeBetweenCooldown.sample(this.mob.getRandom());
            return false;
        } else {
            if (cooldown > this.maxCooldown) {
                this.cooldown = 0;
                this.maxCooldown = timeBetweenCooldown.sample(this.mob.getRandom());
                this.maxActiveTime = timeBetween.sample(this.mob.getRandom());
                return this.isMatchCondition();
            } else {
                ++this.cooldown;
                return false;
            }
        }
    }

    public boolean isMatchCondition() {
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return this.maxActiveTime >= this.tick;
    }

    @Override
    public void tick() {
        super.tick();
        ++this.tick;
    }

    @Override
    public void start() {
        super.start();
        this.tick = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.tick = 0;
    }
}