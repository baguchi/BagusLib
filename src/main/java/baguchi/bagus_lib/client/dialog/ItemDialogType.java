package baguchi.bagus_lib.client.dialog;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ItemDialogType extends DialogType {
    public static final MapCodec<ItemDialogType> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.STRING.fieldOf("dialog").forGetter(ItemDialogType::getDialogueBase),
                            DialogEffectOption.CODEC.fieldOf("dialog_option").orElse(new DialogEffectOption(1, 1, true, Optional.empty())).forGetter(ItemDialogType::getDialogueOption),
                            NextDialogOption.CODEC.fieldOf("next_dialog").orElse(new NextDialogOption(Optional.empty(), -1)).forGetter(DialogType::getNextDialogOption),
                            Codec.DOUBLE.fieldOf("draw_per_tick").forGetter(ItemDialogType::getDialogPerTick),
                            ItemStack.SINGLE_ITEM_CODEC.fieldOf("item").forGetter(ItemDialogType::getItemStack))
                    .apply(p_345644_, ItemDialogType::new)
    );
    protected final ItemStack itemStack;

    public ItemDialogType(String dialogueBase, DialogEffectOption dialogueOption, NextDialogOption nextDialogOption, double dialogPerTick, ItemStack itemStack) {
        super(dialogueBase, dialogueOption, nextDialogOption, dialogPerTick);
        this.itemStack = itemStack;
    }

    @Override
    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount, int y) {
        if (this.itemStack != null) {
            poseStack.pushPose();
            poseStack.translate(0, y, 0);
            poseStack.scale(this.dialogueOption.scaleX(), this.dialogueOption.scaleY(), 1.0f);
            guiGraphics.renderItem(itemStack, 0, 0);
            poseStack.popPose();
        }
    }

    @Override
    public MapCodec<ItemDialogType> codec() {
        return CODEC;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
