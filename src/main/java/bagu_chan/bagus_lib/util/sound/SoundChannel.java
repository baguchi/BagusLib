package bagu_chan.bagus_lib.util.sound;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public record SoundChannel(SoundKeyframe... keyframes) {
}
