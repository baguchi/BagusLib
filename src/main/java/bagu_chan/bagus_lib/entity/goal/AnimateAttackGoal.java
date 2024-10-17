package bagu_chan.bagus_lib.entity.goal;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;

public class AnimateAttackGoal extends MeleeAttackGoal {
    protected boolean attack;
    public int attackTicks;
    protected final int actionPoint;
    protected final int attackLength;

    public AnimateAttackGoal(PathfinderMob attacker, double speed, int actionPoint, int attackLength) {
        this(attacker, speed, actionPoint, attackLength, true);
    }

    public AnimateAttackGoal(PathfinderMob attacker, double speed, int actionPoint, int attackLength, boolean longPath) {
        super(attacker, speed, longPath);
        this.actionPoint = actionPoint;
        this.attackLength = attackLength;
    }

    @Override
    public void start() {
        super.start();
        this.attackTicks = 0;
    }

    @Override
    public void stop() {
        super.stop();
        this.attack = false;
        this.mob.setAggressive(false);
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target) {
        if (this.isTimeToAttack()) {
            if (this.canPerformAttack(target)) {
                this.doAttack(target);
            }
        } else if (this.attackTicks >= this.attackLength) {
            this.resetAttackCooldown();
            this.attack = false;
        } else if (this.attackTicks == 0 || !this.attack) {
            if (!this.canPerformAttack(target)) {
                this.resetAttackCooldown();
            } else {
                this.attack = true;
                this.doTheAnimation();
            }
        }
        if (this.attack) {
            this.attackTicks = Mth.clamp(this.attackTicks + 1, 0, this.attackLength);
        } else {
            this.attackTicks = 0;
        }
    }

    protected void doAttack(LivingEntity living) {
        if (living.level() instanceof ServerLevel serverLevel) {
            this.mob.doHurtTarget(serverLevel, living);
        }
    }

    protected boolean canPerformAttack(LivingEntity target) {
        return this.mob.isWithinMeleeAttackRange(target) && this.mob.getSensing().hasLineOfSight(target);
    }

    protected void doTheAnimation() {
        this.mob.level().broadcastEntityEvent(this.mob, (byte) 4);
    }

    protected void resetAttackCooldown() {
        this.attackTicks = 0;
        this.attack = false;
    }

    protected boolean isTimeToAttack() {
        return this.attackTicks == this.actionPoint;
    }


    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }
}