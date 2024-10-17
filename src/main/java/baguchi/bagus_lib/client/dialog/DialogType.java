package baguchi.bagus_lib.client.dialog;

import baguchi.bagus_lib.util.DialogHandler;
import baguchi.bagus_lib.util.client.SoundUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DialogType {
    @Nullable
    protected DialogHandler.DrawString dialogue;
    @Nullable
    protected MutableComponent dialogueBase;
    @Nullable
    protected Holder<SoundEvent> soundEvent;

    protected float scaleX = 1;
    protected float scaleY = 1;
    protected int posX = 1;
    protected int posY = 1;
    protected int renderDialogY = 16;
    protected long dialogRenderTime = -1;

    @OnlyIn(Dist.CLIENT)
    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount) {
    }

    @OnlyIn(Dist.CLIENT)
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

    public CompoundTag writeTag() {
        CompoundTag tag = new CompoundTag();
        if (this.dialogueBase != null) {
            tag.putString("message", this.dialogueBase.getString());
        }
        tag.putFloat("scaleX", this.scaleX);
        tag.putFloat("scaleY", this.scaleY);
        tag.putInt("posX", this.posX);
        tag.putInt("posY", this.posY);
        tag.putInt("dialogY", this.renderDialogY);
        tag.putLong("dialogRenderTime", this.dialogRenderTime);
        if (this.soundEvent != null) {
            tag.putString("SoundEvent", BuiltInRegistries.SOUND_EVENT.getKey(this.soundEvent.value()).toString());
        }
        return tag;
    }

    public void readTag(CompoundTag tag) {
        if (tag.contains("message")) {
            this.dialogueBase = Component.literal(tag.getString("message"));
        }
        if (tag.contains("scaleX")) {
            this.scaleX = tag.getFloat("scaleX");
        }
        if (tag.contains("scaleY")) {
            this.scaleY = tag.getFloat("scaleY");
        }
        if (tag.contains("posX")) {
            this.posX = tag.getInt("posX");
        }
        if (tag.contains("posY")) {
            this.posY = tag.getInt("posY");
        }
        if (tag.contains("dialogY")) {
            this.renderDialogY = tag.getInt("dialogY");
        }
        if (tag.contains("dialogRenderTime")) {
            this.dialogRenderTime = tag.getInt("dialogRenderTime");
        }
        if (tag.contains("SoundEvent")) {
            Optional<Holder.Reference<SoundEvent>> soundEventHolder = BuiltInRegistries.SOUND_EVENT
                    .get(ResourceLocation.tryParse(tag.getString("SoundEvent")));
            soundEventHolder.ifPresent(soundEventReference -> this.soundEvent = soundEventReference);
        }
    }

    public DialogType getClone() {
        DialogType dialogType = new DialogType();
        dialogType.readTag(this.writeTag());
        return dialogType;
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

    public long getDialogRenderTime() {
        return dialogRenderTime;
    }

    public void setDialogRenderTime(long dialogRenderTime) {
        this.dialogRenderTime = dialogRenderTime;
    }
}
