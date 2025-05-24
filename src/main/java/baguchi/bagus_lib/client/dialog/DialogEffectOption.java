package baguchi.bagus_lib.client.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

public record DialogEffectOption(float scaleX, float scaleY, boolean translate,
                                 Optional<Holder<SoundEvent>> soundEvent) {
    public static final MapCodec<DialogEffectOption> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.FLOAT.fieldOf("scaleX").orElse(1F).forGetter(DialogEffectOption::scaleX),
                            Codec.FLOAT.fieldOf("scaleY").orElse(1F).forGetter(DialogEffectOption::scaleY),
                            Codec.BOOL.fieldOf("translate").orElse(true).forGetter(DialogEffectOption::translate),
                            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().optionalFieldOf("sound_event").forGetter(DialogEffectOption::soundEvent))
                    .apply(p_345644_, DialogEffectOption::new)
    );
}
