package baguchi.bagus_lib.client.dialog;

import baguchi.bagus_lib.register.ModDialogs;
import baguchi.bagus_lib.util.DialogHandler;
import baguchi.bagus_lib.util.client.SoundUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DialogType {
    public static final MapCodec<DialogType> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.STRING.fieldOf("dialog").forGetter(DialogType::getDialogueBase),
                            DialogOption.CODEC.fieldOf("dialog_option").orElse(new DialogOption(1, 1, true, Optional.empty())).forGetter(DialogType::getDialogueOption),
                            Codec.LONG.fieldOf("dialog_render_time").forGetter(DialogType::getDialogRenderTime),
                            Codec.DOUBLE.fieldOf("draw_per_tick").forGetter(DialogType::getDialogPerTick))
                    .apply(p_345644_, DialogType::new)
    );

    public static final Codec<DialogType> DIRECT_CODEC = ModDialogs.getRegistry()
            .byNameCodec()
            .dispatch(DialogType::codec, Function.identity());
    @Nullable
    protected DialogHandler.DrawString drawingString;
    protected final String dialogueBase;
    protected final DialogOption dialogueOption;

    protected final long dialogRenderTime;
    protected long lastDialogRenderTime;
    protected final double dialogPerTick;

    public DialogType(String dialogueBase, DialogOption dialogueOption, long dialogRenderTime, double dialogPerTick) {
        this.dialogueBase = dialogueBase;
        this.dialogueOption = dialogueOption;
        this.dialogRenderTime = dialogRenderTime;
        this.dialogPerTick = dialogPerTick;
    }

    public MapCodec<? extends DialogType> codec() {
        return CODEC;
    }

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount, int y) {
    }

    @OnlyIn(Dist.CLIENT)
    public void renderText(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount, int y) {

        Font font = Minecraft.getInstance().font;
        float g = tickCount + f;
        if (this.drawingString == null && this.dialogueBase != null) {
            MutableComponent component = this.dialogueOption.translate() ? Component.translatable(dialogueBase) : Component.literal(dialogueBase);
            this.drawingString = beginString(guiGraphics, g, this.dialogPerTick, font, component.getString(), 0xFFFFFF, guiGraphics.guiWidth() - 72);
        }


        if (this.drawingString != null && this.drawingString.draw(g, 72, y)) {
            if (this.dialogueOption.soundEvent().isPresent()) {
                SoundUtils.playClientSound(this.dialogueOption.soundEvent().get());
            }
        }
    }

    public DialogHandler.DrawString beginString(GuiGraphics guiGraphics, double lastTick, double perTick, Font font, String string2, int i, int j2) {
        List<FormattedText> list = font.getSplitter().splitLines(string2, j2, Style.EMPTY);
        String string22 = list.stream().map(FormattedText::getString).collect(Collectors.joining("\n"));
        return new DialogHandler.DrawString(lastTick, perTick, string22, (string, j, k) -> {
            String[] strings = string.split("\\r?\\n");
            int l = k;
            for (String string3 : strings) {
                guiGraphics.drawString(font, string3, j, l, i);
                l += font.lineHeight + 4;
            }
        });
    }

    @Nullable
    public String getDialogueBase() {
        return dialogueBase;
    }

    public DialogOption getDialogueOption() {
        return dialogueOption;
    }

    public long getDialogRenderTime() {
        return dialogRenderTime;
    }


    public double getDialogPerTick() {
        return dialogPerTick;
    }

    public void setLastDialogRenderTime(long lastDialogRenderTime) {
        this.lastDialogRenderTime = lastDialogRenderTime;
    }

    public long getLastDialogRenderTime() {
        return lastDialogRenderTime;
    }
}
