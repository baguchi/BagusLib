package baguchi.bagus_lib.client.dialog.builder;

import baguchi.bagus_lib.client.dialog.ItemDialogType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ItemDialogBuilder<T extends ItemDialogType> extends DialogBuilder<T> {
    protected ItemStack itemStack = ItemStack.EMPTY;

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
            BuiltInRegistries.ITEM.get(ResourceLocation.tryParse(tag.getString("Item").orElseThrow())).ifPresent(itemReference -> {
                this.itemStack = itemReference.value().getDefaultInstance();
            });
        }
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
