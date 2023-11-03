package bagu_chan.bagus_lib.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class AnimatedAttackGoal extends MeleeAttackGoal {
    protected boolean attack;

    protected final int leftActionPoint;
    protected final int attackLength;

    public AnimatedAttackGoal(PathfinderMob attacker, double speed, int leftActionPoint, int attackLength) {
        this(attacker, speed, leftActionPoint, attackLength, true);
    }

    public AnimatedAttackGoal(PathfinderMob attacker, double speed, int leftActionPoint, int attackLength, boolean longPath) {
        super(attacker, speed, longPath);
        this.leftActionPoint = leftActionPoint;
        this.attackLength = attackLength;
    }

    @Override
    public void stop() {
        super.stop();
        this.attack = false;
        this.mob.setAggressive(false);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity p_29589_) {
        if (this.getTicksUntilNextAttack() == this.leftActionPoint) {
            if (this.canPerformAttack(p_29589_)) {
                this.mob.doHurtTarget(p_29589_);
            }


            if (this.getTicksUntilNextAttack() == 0) {
                this.resetAttackCooldown();
            }
        } else if (this.canPerformAttack(p_29589_) && this.getTicksUntilNextAttack() >= this.attackLength) {
            if (this.getTicksUntilNextAttack() == this.attackLength) {
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

    protected boolean canPerformAttack(LivingEntity p_301160_) {
        return this.mob.isWithinMeleeAttackRange(p_301160_) && this.mob.getSensing().hasLineOfSight(p_301160_);
    }

    protected void doTheAnimation() {
        this.mob.level().broadcastEntityEvent(this.mob, (byte) 4);
    }

    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = this.adjustedTickDelay(this.attackLength + 1);
        this.attack = false;
    }

    protected boolean isTimeToAttack() {
        return this.ticksUntilNextAttack <= 0;
    }


    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}