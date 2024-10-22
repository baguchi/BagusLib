package baguchi.bagus_lib.api;

import baguchi.bagus_lib.animation.BaguAnimationController;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Unique;

public interface IBagusExtraRenderState {
    ItemStack getBagusLib$chestItem();

    ItemStack getBagusLib$feetItem();

    ItemStack getBagusLib$legItem();

    void setBagusLib$chestItem(ItemStack bagusLib$chestItem);

    void setBagusLib$legItem(ItemStack bagusLib$legItem);

    void setBagusLib$feetItem(ItemStack bagusLib$feetItem);

    @Unique
    void bagusLib$setBaguAnimationController(BaguAnimationController baguAnimationController);

    @Unique
    BaguAnimationController bagusLib$getBaguAnimationController();
}
