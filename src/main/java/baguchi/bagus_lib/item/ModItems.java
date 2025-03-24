package baguchi.bagus_lib.item;

import baguchi.bagus_lib.BagusLib;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEM_REGISTRY = DeferredRegister.createItems(BagusLib.MODID);

    private static ResourceKey<Item> prefix(String path) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, path));
    }
}