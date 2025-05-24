package baguchi.bagus_lib.client.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public record NextDialogOption(Optional<ResourceLocation> nextDialogLocation, long dialogRenderTime) {
    public static final MapCodec<NextDialogOption> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(ResourceLocation.CODEC.optionalFieldOf("next_dialog").forGetter(NextDialogOption::nextDialogLocation),
                            Codec.LONG.fieldOf("render_time").orElse(-1L).forGetter(NextDialogOption::dialogRenderTime))
                    .apply(p_345644_, NextDialogOption::new));
}
