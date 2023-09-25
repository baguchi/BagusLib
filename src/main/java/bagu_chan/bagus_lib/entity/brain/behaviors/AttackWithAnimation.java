package bagu_chan.bagus_lib.entity.brain.behaviors;

import com.google.common.collect.ImmutableMap;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class AttackWithAnimation<E extends PathfinderMob> extends Behavior<E> {
    protected boolean attack;

    protected final int leftActionPoint;
    protected final int attackLengh;

    private final int cooldownBetweenAttacks;

    private int cooldownTick;
    private final double speed;

    public AttackWithAnimation(int leftActionPoint, int attackLengh, int cooldownBetweenAttacks, double speed) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED));
        this.leftActionPoint = leftActionPoint;
        this.attackLengh = attackLengh;
        this.cooldownBetweenAttacks = cooldownBetweenAttacks;
        this.speed = speed;
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E mob) {
        LivingEntity livingentity = this.getAttackTarget(mob);
        return mob.hasLineOfSight(livingentity);
    }

    protected void start(ServerLevel p_23524_, E p_23525_, long p_23526_) {
        LivingEntity livingentity = this.getAttackTarget(p_23525_);
        p_23525_.lookAt(EntityAnchorArgument.Anchor.EYES, livingentity.position());
    }

    @Override
    protected void tick(ServerLevel p_22551_, E p_22552_, long p_22553_) {
        super.tick(p_22551_, p_22552_, p_22553_);

        LivingEntity livingentity = this.getAttackTarget(p_22552_);
        if (livingentity != null) {
            p_22552_.lookAt(EntityAnchorArgument.Anchor.EYES, livingentity.position());
            p_22552_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(livingentity.position(), 1.0F, 0));
            checkAndPerformAttack(p_22552_, livingentity, p_22551_);
            this.cooldownTick = Math.max(this.cooldownTick - 1, 0);
        }
    }

    @Override
    protected boolean canStillUse(ServerLevel p_22545_, E p_22546_, long p_22547_) {
        return true;
    }

    protected void checkAndPerformAttack(E entity, LivingEntity p_29589_, ServerLevel serverLevel) {
        if (this.cooldownTick == this.leftActionPoint) {
            if (this.canPerformAttack(entity, p_29589_)) {
                entity.doHurtTarget(p_29589_);
            }


            if (this.cooldownTick == 0) {
                this.resetAttackCooldown();
            }
        } else if (this.canPerformAttack(entity, p_29589_)) {
            if (this.cooldownTick == this.attackLengh) {
                this.doTheAnimation(entity, serverLevel);
                this.attack = true;
            }
            if (this.cooldownTick == 0) {
                this.resetAttackCooldown();
            }
        } else {
            if (this.cooldownTick == 0 || !this.attack) {
                this.resetAttackCooldown();
            }
        }

    }

    protected boolean canPerformAttack(E entity, LivingEntity p_301160_) {
        return entity.isWithinMeleeAttackRange(p_301160_) && entity.getSensing().hasLineOfSight(p_301160_);
    }

    public void doTheAnimation(E entity, ServerLevel serverLevel) {
        serverLevel.broadcastEntityEvent(entity, (byte) 4);
    }

    private void resetAttackCooldown() {
        cooldownTick = attackLengh + 1;
    }

    @Override
    protected void stop(ServerLevel p_22548_, E p_22549_, long p_22550_) {
        super.stop(p_22548_, p_22549_, p_22550_);
        p_22549_.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, (long) this.cooldownBetweenAttacks);
    }

    private LivingEntity getAttackTarget(E p_23533_) {
        return p_23533_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() ? p_23533_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get() : null;
    }
}