package baguchi.bagus_lib.register;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.loot.OneItemLootModifier;
import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ModLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BagusLib.MODID);

    public static final DeferredHolder<MapCodec<? extends IGlobalLootModifier>, MapCodec<OneItemLootModifier>> ONE_IN_LOOT = LOOT_MODIFIERS.register("one_in_loot", OneItemLootModifier.CODEC);
}