package bagu_chan.bagus_lib.client.dialog;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

/*
 * If using this dialog. Image Path should be [modid]:textures/.../[filename].png
 */
public class ImageDialogType extends DialogType {
    @Nullable
    protected ResourceLocation resourceLocation;
    protected int sizeX = 16;
    protected int sizeY = 16;

    @Override
    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount) {
        if (this.resourceLocation != null) {
            poseStack.pushPose();
            poseStack.scale(this.scaleX, this.scaleY, 1.0f);
            guiGraphics.blit(resourceLocation, this.posX, this.posY, 0, 0, this.sizeX, this.sizeY);
            poseStack.popPose();
        }
    }

    public void setSize(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public void setResourceLocation(@Nullable ResourceLocation resourceLocation) {
        this.resourceLocation = resourceLocation;
    }
}
