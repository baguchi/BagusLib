package bagu_chan.bagus_lib.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class AnimatedAttackGoal extends MeleeAttackGoal {
    protected boolean attack;

    protected final int leftActionPoint;
    protected final int attackLengh;

    public AnimatedAttackGoal(PathfinderMob attacker, double speed, int leftActionPoint, int attackLengh) {
        this(attacker, speed, leftActionPoint, attackLengh, true);
    }

    public AnimatedAttackGoal(PathfinderMob attacker, double speed, int leftActionPoint, int attackLengh, boolean longPath) {
        super(attacker, speed, longPath);
        this.leftActionPoint = leftActionPoint;
        this.attackLengh = attackLengh;
    }

    @Override
    public void stop() {
        super.stop();
        this.attack = false;
        this.mob.setAggressive(false);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity p_29589_, double p_29590_) {
        double d0 = this.getAttackReachSqr(p_29589_);
        if (this.getTicksUntilNextAttack() == this.leftActionPoint) {
            if (p_29590_ <= d0) {
                this.mob.doHurtTarget(p_29589_);
            }


            if (this.getTicksUntilNextAttack() == 0) {
                this.resetAttackCooldown();
            }
        } else if (p_29590_ <= d0) {
            if (this.getTicksUntilNextAttack() == this.attackLengh) {
                this.doTheAnimation();
                this.attack = true;
            }
            if (this.getTicksUntilNextAttack() == 0) {
                this.resetAttackCooldown();
            }
        } else {
            if (this.getTicksUntilNextAttack() == 0 || !this.attack) {
                this.resetAttackCooldown();
            }
        }

    }

    protected void doTheAnimation() {
        this.mob.level().broadcastEntityEvent(this.mob, (byte) 4);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(this.attackLengh + 1);
        this.attack = false;
    }


    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}