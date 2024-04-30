package bagu_chan.bagus_lib.client.dialog;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemDialogType extends DialogType {
    protected ItemStack itemStack = ItemStack.EMPTY;


    @Override
    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount) {
        if (this.itemStack != null) {
            poseStack.pushPose();
            poseStack.translate(0, renderDialogY, 0);
            poseStack.scale(this.scaleX, this.scaleY, 1.0f);
            guiGraphics.renderItem(itemStack, this.posX, this.posY);
            poseStack.popPose();
        }
    }


    public CompoundTag writeTag() {
        CompoundTag tag = super.writeTag();
        if (itemStack != null) {
            tag.putString("Item", BuiltInRegistries.ITEM.getKey(itemStack.getItem()).toString());
        }
        return tag;
    }

    public void readTag(CompoundTag tag) {
        super.readTag(tag);
        if (tag.contains("Item")) {
            this.itemStack = BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(tag.getString("Item"))).getDefaultInstance();
        }
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
