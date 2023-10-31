package bagu_chan.bagus_lib.register;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.entity.MiniBagu;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.SpawnPlacementRegisterEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = BagusLib.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES_REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, BagusLib.MODID);

    public static final RegistryObject<EntityType<MiniBagu>> MINI_BAGU = ENTITIES_REGISTRY.register("mini_bagu", () -> EntityType.Builder.of(MiniBagu::new, MobCategory.CREATURE).sized(0.6F, 0.6F).build(prefix("mini_bagu")));

    private static String prefix(String path) {
        return BagusLib.MODID + "." + path;
    }

    @SubscribeEvent
    public static void registerEntity(EntityAttributeCreationEvent event) {
        event.put(MINI_BAGU.get(), MiniBagu.createAttributeMap().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(MINI_BAGU.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
