package bagu_chan.bagus_lib.util.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
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

    public static void playClientSound(Holder<SoundEvent> soundEvent) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(soundEvent.value(), 1.0F, 1.0F));
    }
}
