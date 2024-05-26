package bagu_chan.bagus_lib.client.dialog;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;


/*
 * If using this dialog. Image Path should be textures/gui/sprites/[filename]
 */
public class ImageDialogType extends DialogType {
    @Nullable
    protected ResourceLocation resourceLocation;
    protected int sizeX = 32;
    protected int sizeY = 32;

    @Override
    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount) {
        if (this.resourceLocation != null) {
            poseStack.pushPose();
            poseStack.translate(0, renderDialogY, 0);
            poseStack.scale(this.scaleX, this.scaleY, 1.0f);
            guiGraphics.blitSprite(resourceLocation, this.posX, this.posY, this.sizeX, this.sizeY);
            poseStack.popPose();
        }
    }

    public CompoundTag writeTag() {
        CompoundTag tag = super.writeTag();
        if (resourceLocation != null) {
            tag.putString("ImagePath", resourceLocation.toString());
        }
        tag.putInt("sizeX", sizeX);
        tag.putInt("sizeY", sizeY);
        return tag;
    }

    public void readTag(CompoundTag tag) {
        super.readTag(tag);
        if (tag.contains("ImagePath")) {
            this.resourceLocation = ResourceLocation.tryParse(tag.getString("ImagePath"));
        }
        if (tag.contains("sizeX")) {
            this.sizeX = tag.getInt("sizeX");
        }
        if (tag.contains("sizeY")) {
            this.sizeY = tag.getInt("sizeY");
        }
    }

    @Override
    public ImageDialogType getClone() {
        ImageDialogType dialog = new ImageDialogType();
        dialog.readTag(this.writeTag());
        return dialog;
    }

    public void setSize(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void setResourceLocation(@Nullable ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }
}
