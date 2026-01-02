package bagu_chan.bagus_lib.entity.brain.behaviors;

import com.google.common.collect.ImmutableMap;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;

public class AttackWithAnimation<E extends PathfinderMob> extends Behavior<E> {
    protected boolean attack;

    protected final int actionPoint;
    protected final int attackLength;

    protected final int cooldownBetweenAttacks;

    protected int attackTicks;
    protected final float speed;

    public AttackWithAnimation(int actionPoint, int attackLength, int cooldownBetweenAttacks, float speed) {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED, MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT));
        this.actionPoint = actionPoint;
        this.attackLength = attackLength;
        this.cooldownBetweenAttacks = cooldownBetweenAttacks;
        this.speed = speed;
    }

    protected boolean checkExtraStartConditions(ServerLevel level, E mob) {
        LivingEntity livingentity = this.getAttackTarget(mob);
        return livingentity != null && mob.hasLineOfSight(livingentity);
    }

    protected void start(ServerLevel p_23524_, E p_23525_, long p_23526_) {
        LivingEntity livingentity = this.getAttackTarget(p_23525_);
        p_23525_.lookAt(EntityAnchorArgument.Anchor.EYES, livingentity.position());
        this.attackTicks = 0;
    }

    @Override
    protected void tick(ServerLevel p_22551_, E p_22552_, long p_22553_) {
        super.tick(p_22551_, p_22552_, p_22553_);

        LivingEntity livingentity = this.getAttackTarget(p_22552_);
        if (livingentity != null) {
            p_22552_.lookAt(EntityAnchorArgument.Anchor.EYES, livingentity.position());
            p_22552_.getBrain().setMemory(MemoryModuleType.WALK_TARGET, new WalkTarget(livingentity.position(), this.speed, 0));
            checkAndPerformAttack(p_22552_, livingentity, p_22551_);

        }
    }

    @Override
    protected boolean canStillUse(ServerLevel p_22545_, E p_22546_, long p_22547_) {
        return true;
    }

    protected void checkAndPerformAttack(E entity, LivingEntity target, ServerLevel serverLevel) {
        if (this.isTimeToAttack()) {
            if (this.canPerformAttack(entity, target)) {
                this.doAttack(entity, target);
            }
        } else if (this.attackTicks >= this.attackLength) {
            this.resetAttackCooldown();
            this.attack = false;
        } else if (!this.attack) {
            if (!this.canPerformAttack(entity, target)) {
                this.resetAttackCooldown();
            } else {
                this.attack = true;
                this.doTheAnimation(entity, serverLevel);
            }
        }
        if (this.attack) {
            this.attackTicks = Mth.clamp(this.attackTicks + 1, 0, this.attackLength);
        } else {
            this.attackTicks = 0;
        }
    }

    protected void doAttack(E attacker, LivingEntity living) {
        attacker.doHurtTarget(living);
    }

    protected boolean isTimeToAttack() {
        return this.attackTicks == this.actionPoint;
    }


    protected boolean canPerformAttack(E entity, LivingEntity p_301160_) {
        return entity.isWithinMeleeAttackRange(p_301160_) && entity.getSensing().hasLineOfSight(p_301160_);
    }

    public void doTheAnimation(E entity, ServerLevel serverLevel) {
        serverLevel.broadcastEntityEvent(entity, (byte) 4);
    }

    private void resetAttackCooldown() {
        attackTicks = 0;
    }

    @Override
    protected void stop(ServerLevel p_22548_, E p_22549_, long p_22550_) {
        super.stop(p_22548_, p_22549_, p_22550_);
        this.attack = false;
        p_22549_.getBrain().setMemoryWithExpiry(MemoryModuleType.ATTACK_COOLING_DOWN, true, this.cooldownBetweenAttacks);
    }

    private LivingEntity getAttackTarget(E p_23533_) {
        return p_23533_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).isPresent() ? p_23533_.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).get() : null;
    }
}