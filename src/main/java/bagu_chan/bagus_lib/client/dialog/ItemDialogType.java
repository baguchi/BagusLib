package bagu_chan.bagus_lib.client.dialog;

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
                            DialogOption.CODEC.fieldOf("dialog_option").orElse(new DialogOption(1, 1, true, Optional.empty())).forGetter(ItemDialogType::getDialogueOption),
                            Codec.LONG.fieldOf("dialog_render_time").forGetter(ItemDialogType::getDialogRenderTime),
                            Codec.DOUBLE.fieldOf("draw_per_tick").forGetter(ItemDialogType::getDialogPerTick),
                            ItemStack.SINGLE_ITEM_CODEC.fieldOf("item").forGetter(ItemDialogType::getItemStack))
                    .apply(p_345644_, ItemDialogType::new)
    );
    protected final ItemStack itemStack;

    public ItemDialogType(String dialogueBase, DialogOption dialogueOption, long dialogRenderTime, double dialogPerTick, ItemStack itemStack) {
        super(dialogueBase, dialogueOption, dialogRenderTime, dialogPerTick);
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
