package bagu_chan.bagus_lib.loot;

import com.google.gson.JsonObject;
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

public class LootInLootModifier extends LootModifier {

    public final ResourceLocation lootTable;

    protected LootInLootModifier(LootItemCondition[] conditionsIn, ResourceLocation lootTable) {
        super(conditionsIn);
        this.lootTable = lootTable;
    }

    @NotNull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        LootTable extraTable = context.getLootTable(this.lootTable);
        extraTable.getRandomItemsRaw(context, generatedLoot::add);
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<LootInLootModifier> {
        @Override
        public LootInLootModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] ailootcondition) {
            ResourceLocation addedItem = new ResourceLocation((GsonHelper.getAsString(object, "loot_table")));
            return new LootInLootModifier(ailootcondition, addedItem);
        }

        @Override
        public JsonObject write(LootInLootModifier instance) {
            return new JsonObject();
        }
    }
}