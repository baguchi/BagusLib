package bagu_chan.bagus_lib.util;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class BrainUtils {
    private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT = TargetingConditions.forCombat().ignoreLineOfSight();
    private static final TargetingConditions ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT = TargetingConditions.forCombat().ignoreLineOfSight().ignoreInvisibilityTesting();


    public static boolean isEntityAttackableIgnoringLineOfSight(LivingEntity p_182378_, LivingEntity p_182379_, int range) {
        return p_182378_.getBrain().isMemoryValue(MemoryModuleType.ATTACK_TARGET, p_182379_) ? ATTACK_TARGET_CONDITIONS_IGNORE_INVISIBILITY_AND_LINE_OF_SIGHT.range(range).test(p_182378_, p_182379_) : ATTACK_TARGET_CONDITIONS_IGNORE_LINE_OF_SIGHT.range(range).test(p_182378_, p_182379_);
    }
}
