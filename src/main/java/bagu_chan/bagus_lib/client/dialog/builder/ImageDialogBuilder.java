package bagu_chan.bagus_lib.client.dialog.builder;

import bagu_chan.bagus_lib.client.dialog.ImageDialogType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class ImageDialogBuilder<T extends ImageDialogType> extends DialogBuilder<T> {
    @Nullable
    protected ResourceLocation resourceLocation;
    protected int sizeX = 32;
    protected int sizeY = 32;

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
}
