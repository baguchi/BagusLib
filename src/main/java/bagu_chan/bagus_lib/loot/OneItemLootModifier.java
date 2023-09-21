package bagu_chan.bagus_lib.loot;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class OneItemLootModifier extends LootModifier {
    public final ResourceLocation lootTable;

    public OneItemLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        LootTable extraTable = context.getLootTable(this.lootTable);

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

    public static class Serializer extends GlobalLootModifierSerializer<OneItemLootModifier> {
        @Override
        public OneItemLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            ResourceLocation addedItem = new ResourceLocation((GsonHelper.getAsString(object, "loot_table")));
            return new OneItemLootModifier(ailootcondition, addedItem);
        }

        @Override
        public JsonObject write(OneItemLootModifier instance) {
            return new JsonObject();
        }
    }
}