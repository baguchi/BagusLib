package bagu_chan.bagus_lib.register;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.loot.LootInLootModifier;
import bagu_chan.bagus_lib.loot.OneItemLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, BagusLib.MODID);

    public static final RegistryObject<OneItemLootModifier.Serializer> ONE_IN_LOOT = LOOT_MODIFIERS.register("one_in_loot", OneItemLootModifier.Serializer::new);
    public static final RegistryObject<LootInLootModifier.Serializer> LOOT_IN_LOOT_MODIFIER = LOOT_MODIFIERS.register("loot_in_loot", LootInLootModifier.Serializer::new);
}