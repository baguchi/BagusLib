package baguchi.bagus_lib.client.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.joml.Matrix3x2fStack;

import java.util.Optional;

public class WinDialogType extends DialogType {

    public static final MapCodec<WinDialogType> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.STRING.fieldOf("dialog").forGetter(WinDialogType::getDialogueBase),
                            DialogEffectOption.CODEC.fieldOf("dialog_option").orElse(new DialogEffectOption(1, 1, true, Optional.empty())).forGetter(WinDialogType::getDialogueOption),
                            NextDialogOption.CODEC.fieldOf("next_dialog").orElse(new NextDialogOption(Optional.empty(), -1)).forGetter(DialogType::getNextDialogOption),
                            Codec.DOUBLE.fieldOf("draw_per_tick").forGetter(WinDialogType::getDialogPerTick))
                    .apply(p_345644_, WinDialogType::new)
    );

    public WinDialogType(String dialogueBase, DialogEffectOption dialogueOption, NextDialogOption nextDialogOption, double dialogPerTick) {
        super(dialogueBase, dialogueOption, nextDialogOption, dialogPerTick);
    }

    @Override
    public void renderText(GuiGraphicsExtractor guiGraphics, Matrix3x2fStack poseStack, float f, float tickCount, int y) {
        super.renderText(guiGraphics, poseStack, f, tickCount, y);

        Font font = Minecraft.getInstance().font;
        float g = tickCount + f;
        if (this.drawingString == null && this.dialogueBase != null) {
            MutableComponent component = Component.translatable(dialogueBase);
            this.drawingString = beginString(guiGraphics, g, 3, font, component.getString(), 0xFFFFFF, guiGraphics.guiWidth() - 72);
        }
    }

    @Override
    public MapCodec<WinDialogType> codec() {
        return CODEC;
    }
}
