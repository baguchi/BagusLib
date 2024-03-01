package bagu_chan.bagus_lib.register;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.world.processor.BaseProcessor;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraftforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModStructureProcessorTypes {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSOR_TYPE = DeferredRegister.create(Registries.STRUCTURE_PROCESSOR, BagusLib.MODID);

    public static final Supplier<StructureProcessorType<BaseProcessor>> BASE = register("base", BaseProcessor.CODEC);

    static <P extends StructureProcessor> Supplier<StructureProcessorType<P>> register(String p_74477_, Codec<P> p_74478_) {
        return PROCESSOR_TYPE.register(p_74477_, () -> () -> {
            return p_74478_;
        });
    }
}