package baguchi.bagus_lib.world.processor;

import baguchi.bagus_lib.register.ModStructureProcessorTypes;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jspecify.annotations.Nullable;

/*
 * Based from Yung's Ocean Monuments!
 * https://github.com/YUNG-GANG/YUNGs-Better-Ocean-Monuments/blob/1.20/Common/src/main/java/com/yungnickyoung/minecraft/betteroceanmonuments/world/processor/LegProcessor.java
 */
public class BaseProcessor implements StructureProcessor {
    public static final MapCodec<BaseProcessor> CODEC = RecordCodecBuilder.mapCodec((p_74116_) -> {
        return p_74116_.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter((p_163729_) -> {
            return p_163729_.baseBlock;
        }), BuiltInRegistries.BLOCK.byNameCodec().fieldOf("replace_block").forGetter((p_163727_) -> {
            return p_163727_.replaceBlock;
        })).apply(p_74116_, BaseProcessor::new);
    });


    public final Block baseBlock;
    public final Block replaceBlock;

    public BaseProcessor(Block baseBlock, Block replaceBlock) {
        this.baseBlock = baseBlock;
        this.replaceBlock = replaceBlock;
    }

    @Override
    public StructureTemplate.@Nullable StructureBlockInfo processBlock(LevelReader level, BlockPos targetPosition, BlockPos referencePos, BlockPos templateRelativePos, StructureTemplate.StructureBlockInfo processedBlockInfo, StructurePlaceSettings settings) {
        if (processedBlockInfo.state().getBlock() == this.baseBlock) {
            if (level instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(ChunkPos.containing(processedBlockInfo.pos()))) {
                return processedBlockInfo;
            }

            processedBlockInfo = new StructureTemplate.StructureBlockInfo(processedBlockInfo.pos(), this.replaceBlock.defaultBlockState(), processedBlockInfo.nbt());
            BlockPos.MutableBlockPos mutable = processedBlockInfo.pos().mutable().move(Direction.DOWN);
            BlockState currBlockState = level.getBlockState(mutable);

            while (mutable.getY() > level.getMinY()
                    && mutable.getY() < level.getMaxY()
                    && (currBlockState.isAir() || !level.getFluidState(mutable).isEmpty())) {
                level.getChunk(mutable).setBlockState(mutable, this.replaceBlock.defaultBlockState(), 3);
                mutable.move(Direction.DOWN);
                currBlockState = level.getBlockState(mutable);
            }
        }
        return StructureProcessor.super.processBlock(level, targetPosition, referencePos, templateRelativePos, processedBlockInfo, settings);
    }

    @Override
    public MapCodec<? extends StructureProcessor> codec() {
        return ModStructureProcessorTypes.BASE.get();
    }
}