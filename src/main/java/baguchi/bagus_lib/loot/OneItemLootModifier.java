package baguchi.bagus_lib.loot;

import baguchi.bagus_lib.register.ModLootModifiers;
import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Supplier;

public class OneItemLootModifier extends LootModifier {

    public static final Supplier<MapCodec<OneItemLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
                    .and(Identifier.CODEC.fieldOf("loot_table").forGetter((m) -> m.lootTable))
                    .apply(inst, OneItemLootModifier::new)));

    public final Identifier lootTable;

    public OneItemLootModifier(LootItemCondition[] conditionsIn, Identifier lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    @Nonnull
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        ObjectArrayList<ItemStack> stacks = new ObjectArrayList<>();
        context.getResolver().lookupOrThrow(Registries.LOOT_TABLE).get(ResourceKey.create(Registries.LOOT_TABLE, this.lootTable)).ifPresent(extraTable -> {
            // Don't run loot modifiers for subtables;
            // the added loot will be modifiable by downstream loot modifiers modifying the target table,
            // so if we modify it here then it could get modified twice.

            extraTable.value().getRandomItemsRaw(context, LootTable.createStackSplitter(context.getLevel(), stacks::add));
        });
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