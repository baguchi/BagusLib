package bagu_chan.bagus_lib.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
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

    public static final Supplier<Codec<OneItemLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
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
        LootTable extraTable = context.getResolver().getLootTable(this.lootTable);

        ObjectArrayList<ItemStack> stacks = new ObjectArrayList<>();
        extraTable.getRandomItemsRaw(context, stacks::add);
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
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}