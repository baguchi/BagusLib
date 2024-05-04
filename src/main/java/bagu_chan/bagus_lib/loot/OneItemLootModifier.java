package bagu_chan.bagus_lib.loot;

import bagu_chan.bagus_lib.register.ModLootModifiers;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class OneItemLootModifier extends LootModifier {

    public static final Supplier<MapCodec<OneItemLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
                    .and(ResourceLocation.CODEC.fieldOf("loot_table").forGetter((m) -> m.lootTable))
                    .apply(inst, OneItemLootModifier::new)));

    public final ResourceLocation lootTable;

    public OneItemLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ObjectArrayList<ItemStack> stacks = new ObjectArrayList<>();
        Optional<Holder.Reference<LootTable>> extraTable = context.getResolver().get(Registries.LOOT_TABLE, ResourceKey.create(Registries.LOOT_TABLE, this.lootTable));
        if (extraTable.isPresent()) {
            extraTable.get().value().getRandomItemsRaw(context, stacks::add);
        }
        List<ItemStack> itemStacks = stacks.stream().filter(itemStack -> {
            return !itemStack.isEmpty();
        }).toList();
        if (!itemStacks.isEmpty()) {
            return stacks;
        } else {
            return generatedLoot;
        }
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return ModLootModifiers.ONE_IN_LOOT.get();
    }
}