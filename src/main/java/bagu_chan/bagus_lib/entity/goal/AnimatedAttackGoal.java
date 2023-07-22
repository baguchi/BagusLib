package bagu_chan.bagus_lib.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class AnimatedAttackGoal extends MeleeAttackGoal {
    private final PathfinderMob mob;
    private boolean attack;

    private final int leftActionPoint;
    private final int attackLengh;

    public AnimatedAttackGoal(PathfinderMob mob, double speed, int leftActionPoint, int attackLengh) {
        super(mob, speed, true);
        this.mob = mob;
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
                this.mob.level().broadcastEntityEvent(this.mob, (byte) 4);
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

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(this.attackLengh + 1);
        this.attack = false;
    }

    protected double getAttackReachSqr(LivingEntity p_25556_) {
        return (double) (this.mob.getBbWidth() * 2.0F * this.mob.getBbWidth() * 2.0F + p_25556_.getBbWidth());
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}