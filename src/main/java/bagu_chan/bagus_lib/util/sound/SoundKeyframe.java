package bagu_chan.bagus_lib.util.sound;

import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record SoundKeyframe(float timestamp, SoundEvent sounds, float volume, float pitch) {
}
