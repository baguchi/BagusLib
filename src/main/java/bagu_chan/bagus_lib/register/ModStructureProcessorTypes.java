package bagu_chan.bagus_lib.register;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.world.processor.BaseProcessor;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModStructureProcessorTypes {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSOR_TYPE = DeferredRegister.create(BuiltInRegistries.STRUCTURE_PROCESSOR, BagusLib.MODID);

    public static final DeferredHolder<StructureProcessorType<?>, StructureProcessorType<BaseProcessor>> BASE = register("base", BaseProcessor.CODEC);

    static <P extends StructureProcessor> DeferredHolder<StructureProcessorType<?>, StructureProcessorType<P>> register(String p_74477_, MapCodec<P> p_74478_) {
        return PROCESSOR_TYPE.register(p_74477_, () -> () -> {
            return p_74478_;
        });
    }
}