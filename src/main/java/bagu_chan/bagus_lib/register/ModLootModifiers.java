package bagu_chan.bagus_lib.register;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.loot.LootInLootModifier;
import bagu_chan.bagus_lib.loot.OneItemLootModifier;
import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS = DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, BagusLib.MODID);

    public static final Supplier<Codec<OneItemLootModifier>> ONE_IN_LOOT = LOOT_MODIFIERS.register("one_in_loot", OneItemLootModifier.CODEC);
    public static final Supplier<Codec<LootInLootModifier>> LOOT_IN_LOOT_MODIFIER = LOOT_MODIFIERS.register("loot_in_loot", LootInLootModifier.CODEC);
}