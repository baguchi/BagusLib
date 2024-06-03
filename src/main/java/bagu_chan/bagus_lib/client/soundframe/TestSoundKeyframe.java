package bagu_chan.bagus_lib.client.soundframe;

import bagu_chan.bagus_lib.util.sound.SoundChannel;
import bagu_chan.bagus_lib.util.sound.SoundDefinition;
import bagu_chan.bagus_lib.util.sound.SoundKeyframe;
import net.minecraft.sounds.SoundEvents;

public class TestSoundKeyframe {
    public static final SoundDefinition ATTACK = SoundDefinition.Builder.withLength(1.0F)
            .addSound("root", new SoundChannel(
                    new SoundKeyframe(0.5F, SoundEvents.PLAYER_ATTACK_WEAK, 1.0F, 1.0F),
                    new SoundKeyframe(0.8F, SoundEvents.PLAYER_ATTACK_WEAK, 1.0F, 1.0F)
            ))
            .build();
}
