package baguchi.bagus_lib.client.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;

import java.util.Optional;

public record DialogOption(float scaleX, float scaleY, boolean translate, Optional<Holder<SoundEvent>> soundEvent) {
    public static final MapCodec<DialogOption> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.FLOAT.fieldOf("scaleX").orElse(1F).forGetter(DialogOption::scaleX),
                            Codec.FLOAT.fieldOf("scaleY").orElse(1F).forGetter(DialogOption::scaleY),
                            Codec.BOOL.fieldOf("translate").orElse(true).forGetter(DialogOption::translate),
                            BuiltInRegistries.SOUND_EVENT.holderByNameCodec().optionalFieldOf("sound_event").forGetter(DialogOption::soundEvent))
                    .apply(p_345644_, DialogOption::new)
    );
}
