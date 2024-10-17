package baguchi.bagus_lib.mixin.client;

import baguchi.bagus_lib.api.IBagusExtraRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(net.minecraft.client.renderer.entity.state.LivingEntityRenderState.class)
public class LivingEntityRenderState implements IBagusExtraRenderState {
    @Unique
    public ItemStack bagusLib$chestItem;
    @Unique
    public ItemStack bagusLib$legItem;
    @Unique
    public ItemStack bagusLib$feetItem;

    @Unique
    public ItemStack getBagusLib$chestItem() {
        return bagusLib$chestItem;
    }

    @Unique
    public ItemStack getBagusLib$feetItem() {
        return bagusLib$feetItem;
    }

    @Unique
    public ItemStack getBagusLib$legItem() {
        return bagusLib$legItem;
    }

    @Unique
    public void setBagusLib$chestItem(ItemStack bagusLib$chestItem) {
        this.bagusLib$chestItem = bagusLib$chestItem;
    }

    @Unique
    public void setBagusLib$legItem(ItemStack bagusLib$legItem) {
        this.bagusLib$legItem = bagusLib$legItem;
    }

    @Unique
    public void setBagusLib$feetItem(ItemStack bagusLib$feetItem) {
        this.bagusLib$feetItem = bagusLib$feetItem;
    }
}
