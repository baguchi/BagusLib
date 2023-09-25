package bagu_chan.bagus_lib.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class ManyAnimatedAttackGoal extends MeleeAttackGoal {
    protected boolean attack;

    protected final int[] leftActionPoints;
    protected final int attackLengh;
    protected int maxAttackLengh;
    protected int currentAttackLengh;

    public ManyAnimatedAttackGoal(PathfinderMob attacker, double speed, int[] leftActionPoints, int attackLengh) {
        this(attacker, speed, leftActionPoints, attackLengh, true);
    }

    public ManyAnimatedAttackGoal(PathfinderMob attacker, double speed, int[] leftActionPoints, int attackLengh, boolean longPath) {
        super(attacker, speed, longPath);
        this.leftActionPoints = leftActionPoints;
        this.attackLengh = attackLengh;
        this.maxAttackLengh = leftActionPoints.length;
    }

    @Override
    public void start() {
        super.start();
        this.currentAttackLengh = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.attack = false;
        this.mob.setAggressive(false);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity p_29589_) {
        if (this.getTicksUntilNextAttack() == this.leftActionPoints[this.currentAttackLengh]) {
            if (this.canPerformAttackWithoutTimer(p_29589_)) {
                this.mob.doHurtTarget(p_29589_);
            }

            if (this.currentAttackLengh < this.maxAttackLengh - 1) {
                this.maxAttackLengh += 1;
            }
            if (this.getTicksUntilNextAttack() == 0) {
                this.resetAttackCooldown();
            }
        } else if (this.canPerformAttack(p_29589_)) {
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

    protected boolean canPerformAttackWithoutTimer(LivingEntity p_301160_) {
        return this.mob.isWithinMeleeAttackRange(p_301160_) && this.mob.getSensing().hasLineOfSight(p_301160_);
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