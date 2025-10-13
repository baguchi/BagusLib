package baguchi.bagus_lib.item;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.register.ModEntities;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEM_REGISTRY = DeferredRegister.createItems(BagusLib.MODID);

    public static final DeferredItem<BookItem> BOOK = ITEM_REGISTRY.registerItem("book", BookItem::new);
    public static final DeferredItem<SpawnEggItem> MINI_BAGU_SPAWN_EGG = ITEM_REGISTRY.registerItem("mini_bagu_spawn_egg", (properties -> new SpawnEggItem(properties.spawnEgg(ModEntities.MINI_BAGU.get()))));

    private static ResourceKey<Item> prefix(String path) {
        return ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, path));
    }
}