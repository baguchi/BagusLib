package bagu_chan.bagus_lib.util;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.pools.alias.Random;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import org.apache.commons.compress.utils.Lists;

import java.util.ArrayList;
import java.util.List;

public class JigsawHelper {
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(
            Registries.PROCESSOR_LIST, ResourceLocation.fromNamespaceAndPath("minecraft", "empty"));

    public static void registerJigsawWithRandomPoolAliases(MinecraftServer server, ResourceLocation structureLocation, ResourceLocation poolAliasLocation, SimpleWeightedRandomList<String> targets) {
        RegistryAccess manager = server.registryAccess();
        Registry<Structure> structures = manager.registry(Registries.STRUCTURE).orElseThrow();
        Structure structure = structures.get(structureLocation);
        if (structure == null) return;

        if (structure instanceof JigsawStructure jigsawStructure) {
            List<PoolAliasBinding> list = Lists.newArrayList();
            list.addAll(jigsawStructure.poolAliases);
            jigsawStructure.poolAliases = list;
            jigsawStructure.poolAliases.add(random(poolAliasLocation.toString(), targets));
        }
    }

    public static void registerJigsaw(MinecraftServer server, ResourceLocation poolLocation, ResourceLocation nbtLocation, int weight) {
        RegistryAccess manager = server.registryAccess();
        Registry<StructureTemplatePool> templatePoolRegistry = manager.registry(Registries.TEMPLATE_POOL).orElseThrow();
        Registry<StructureProcessorList> processorListRegistry = manager.registry(Registries.PROCESSOR_LIST).orElseThrow();
        StructureTemplatePool pool = templatePoolRegistry.get(poolLocation);

        if (pool == null) return;

        ObjectArrayList<StructurePoolElement> elements = pool.templates;

        Holder<StructureProcessorList> processorListHolder = processorListRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY);

        StructurePoolElement element = SinglePoolElement.legacy(nbtLocation.toString(), processorListHolder).apply(StructureTemplatePool.Projection.RIGID);
        for (int i = 0; i < weight; i++) {
            elements.add(element);
        }

        List<Pair<StructurePoolElement, Integer>> elementCounts = new ArrayList<>(pool.rawTemplates);

        elements.addAll(pool.templates);
        elementCounts.addAll(pool.rawTemplates);

        pool.templates = elements;
        pool.rawTemplates = elementCounts;
    }

    public static Random random(String alias, SimpleWeightedRandomList<String> targets) {
        SimpleWeightedRandomList.Builder<ResourceKey<StructureTemplatePool>> builder = SimpleWeightedRandomList.builder();
        targets.unwrap().forEach(p_338103_ -> builder.add(Pools.parseKey(p_338103_.data()), p_338103_.getWeight().asInt()));
        return random(Pools.parseKey(alias), builder.build());
    }

    public static Random random(ResourceKey<StructureTemplatePool> alias, SimpleWeightedRandomList<ResourceKey<StructureTemplatePool>> targets) {
        return new Random(alias, targets);
    }
}