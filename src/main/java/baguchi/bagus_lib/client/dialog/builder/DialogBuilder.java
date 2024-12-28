package baguchi.bagus_lib.client.dialog.builder;

import baguchi.bagus_lib.client.dialog.DialogType;
import baguchi.bagus_lib.util.DialogHandler;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;
import java.util.Optional;

public class DialogBuilder<T extends DialogType> {
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
    protected double dialogPerTick = 2;

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
        tag.putDouble("dialogPerTick", this.dialogPerTick);

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
        if (tag.contains("dialogPerTick")) {
            this.dialogPerTick = tag.getInt("dialogPerTick");
        }
        if (tag.contains("SoundEvent")) {
            Optional<Holder.Reference<SoundEvent>> soundEventHolder = BuiltInRegistries.SOUND_EVENT
                    .get(ResourceLocation.tryParse(tag.getString("SoundEvent")));
            soundEventHolder.ifPresent(soundEventReference -> this.soundEvent = soundEventReference);
        }
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


    public void setDialogRenderTime(long dialogRenderTime) {
        this.dialogRenderTime = dialogRenderTime;
    }

    public void setDialogPerTick(long dialogPerTick) {
        this.dialogPerTick = dialogPerTick;
    }
}
