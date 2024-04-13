package bagu_chan.bagus_lib.client.dialog;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public class ItemDialogType extends DialogType {
    protected ItemStack itemStack = ItemStack.EMPTY;


    @Override
    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount) {
        if (this.itemStack != null) {
            poseStack.pushPose();
            poseStack.scale(this.scaleX, this.scaleY, 1.0f);
            guiGraphics.renderItem(itemStack, this.posX, this.posY);
            poseStack.popPose();
        }
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
