package bagu_chan.bagus_lib.entity.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class ManyAnimatedAttackGoal extends MeleeAttackGoal {
    protected boolean attack;

    protected final int[] leftActionPoints;
    protected final int attackLength;
    protected int maxAttackLength;
    protected int currentAttackLength;
    private int ticksUntilNextAttack;

    public ManyAnimatedAttackGoal(PathfinderMob attacker, double speed, int[] leftActionPoints, int attackLength) {
        this(attacker, speed, leftActionPoints, attackLength, true);
    }

    public ManyAnimatedAttackGoal(PathfinderMob attacker, double speed, int[] leftActionPoints, int attackLength, boolean longPath) {
        super(attacker, speed, longPath);
        this.leftActionPoints = leftActionPoints;
        this.attackLength = attackLength;
        this.maxAttackLength = leftActionPoints.length;
    }

    @Override
    public void start() {
        super.start();
        this.currentAttackLength = 0;
        this.ticksUntilNextAttack = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.attack = false;
        this.mob.setAggressive(false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.mob.getTarget() != null) {
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
        }
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity p_29589_) {
        if (this.getTicksUntilNextAttack() == this.leftActionPoints[this.currentAttackLength]) {
            if (this.canPerformAttack(p_29589_)) {
                this.mob.doHurtTarget(p_29589_);
            }

            if (this.currentAttackLength < this.maxAttackLength - 1) {
                this.maxAttackLength += 1;
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


    protected int getTicksUntilNextAttack() {
        return this.ticksUntilNextAttack;
    }



    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}