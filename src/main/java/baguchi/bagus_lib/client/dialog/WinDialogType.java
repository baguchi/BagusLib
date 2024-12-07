package baguchi.bagus_lib.client.dialog;

import baguchi.bagus_lib.util.client.SoundUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;

public class WinDialogType extends DialogType {

    public void renderText(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount) {

        Font font = Minecraft.getInstance().font;
        float g = tickCount + f;
        if (this.dialogue == null && this.dialogueBase != null) {
            MutableComponent component = dialogueBase;
            this.dialogue = beginString(guiGraphics, g, 3, font, component.getString(), 0xFFFFFF, guiGraphics.guiWidth() - 72);
        }


        if (this.dialogue != null && this.dialogue.draw(g, 72, renderDialogY)) {
            if (this.soundEvent != null) {
                SoundUtils.playClientSound(this.soundEvent);
            }
        }
    }

    public WinDialogType getClone(CompoundTag compoundTag) {
        WinDialogType dialogType = new WinDialogType();
        dialogType.readTag(compoundTag);
        return dialogType;
    }
}
