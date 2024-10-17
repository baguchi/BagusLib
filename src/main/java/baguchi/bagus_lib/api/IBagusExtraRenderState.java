package baguchi.bagus_lib.api;

import net.minecraft.world.item.ItemStack;

public interface IBagusExtraRenderState {
    ItemStack getBagusLib$chestItem();

    ItemStack getBagusLib$feetItem();

    ItemStack getBagusLib$legItem();

    void setBagusLib$chestItem(ItemStack bagusLib$chestItem);

    void setBagusLib$legItem(ItemStack bagusLib$legItem);

    void setBagusLib$feetItem(ItemStack bagusLib$feetItem);
}
