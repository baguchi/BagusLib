package bagu_chan.bagus_lib.util.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoundUtils {
    @OnlyIn(Dist.CLIENT)
    public static void playSound(Entity entity, AnimationState animationState, SoundDefinition soundDefinition) {
        animationState.updateTime(entity.tickCount + Minecraft.getInstance().getTimer().getGameTimeDeltaTicks(), 1.0F);
        animationState.ifStarted(p_233392_ -> KeyframeSounds.playSound(entity, soundDefinition, animationState.getAccumulatedTime()));
    }
}
