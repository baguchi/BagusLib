package bagu_chan.bagus_lib.client.dialog;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Optional;

public class WinDialogType extends DialogType {

    public static final MapCodec<WinDialogType> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.STRING.fieldOf("dialog").forGetter(WinDialogType::getDialogueBase),
                            DialogOption.CODEC.fieldOf("dialog_option").orElse(new DialogOption(1, 1, true, Optional.empty())).forGetter(WinDialogType::getDialogueOption),
                            Codec.LONG.fieldOf("dialog_render_time").forGetter(WinDialogType::getDialogRenderTime),
                            Codec.DOUBLE.fieldOf("draw_per_tick").forGetter(WinDialogType::getDialogPerTick))
                    .apply(p_345644_, WinDialogType::new)
    );

    public WinDialogType(String dialogueBase, DialogOption dialogueOption, long dialogRenderTime, double dialogPerTick) {
        super(dialogueBase, dialogueOption, dialogRenderTime, dialogPerTick);
    }


    public void renderText(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount, int y) {

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
