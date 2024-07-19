package bagu_chan.bagus_lib.client.dialog;

import bagu_chan.bagus_lib.util.DialogHandler;
import bagu_chan.bagus_lib.util.client.SoundUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class WinDialogType extends DialogType {

    public void renderText(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount) {

        Font font = Minecraft.getInstance().font;
        float g = (float) tickCount + f;
        if (this.dialogue == null && this.dialogueBase != null) {
            MutableComponent component = dialogueBase;
            this.dialogue = beginString(guiGraphics, g, 2.0, font, component.getString(), 0xFFFFFF, guiGraphics.guiWidth() - 72);
        }


        if (this.dialogue != null && this.dialogue.draw(g, 72, renderDialogY)) {
            if (this.soundEvent != null) {
                SoundUtils.playClientSound(this.soundEvent);
            }
        }
    }

    public DialogHandler.DrawString beginString(GuiGraphics guiGraphics, double d, double e, Font font, String string2, int i, int j2, boolean ignoreWhiteSpace) {
        List<FormattedText> list = font.getSplitter().splitLines(string2, j2, Style.EMPTY);
        String string22 = list.stream().map(FormattedText::getString).collect(Collectors.joining("\n"));
        return new DialogHandler.DrawString(d, e, string22, (string, j, k) -> {
            String[] strings = string.split("\\r?\\n");
            int l = k;
            for (String string3 : strings) {
                guiGraphics.drawString(font, string3, j, l, i);
                l += font.lineHeight + 4;
            }
        }, ignoreWhiteSpace);
    }

    public DialogHandler.DrawString beginString(GuiGraphics guiGraphics, double d, double e, Font font, String string2, int i, int j2) {
        List<FormattedText> list = font.getSplitter().splitLines(string2, j2, Style.EMPTY);
        String string22 = list.stream().map(FormattedText::getString).collect(Collectors.joining("\n"));
        return new DialogHandler.DrawString(d, e, string22, (string, j, k) -> {
            String[] strings = string.split("\\r?\\n");
            int l = k;
            for (String string3 : strings) {
                guiGraphics.drawString(font, string3, j, l, i);
                l += font.lineHeight + 4;
            }
        });
    }

    public void setDialogueBase(@Nullable MutableComponent dialogueBase) {
        this.dialogue = null;
        this.dialogueBase = dialogueBase;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void setPos(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    public void setSoundEvent(@Nullable Holder<SoundEvent> soundEvent) {
        this.soundEvent = soundEvent;
    }

    public void setRenderDialogY(int renderDialogY) {
        this.renderDialogY = renderDialogY;
    }
}
