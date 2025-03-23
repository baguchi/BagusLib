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
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

/*
 * Based from Yung's Ocean Monuments!
 * https://github.com/YUNG-GANG/YUNGs-Better-Ocean-Monuments/blob/1.20/Common/src/main/java/com/yungnickyoung/minecraft/betteroceanmonuments/world/processor/LegProcessor.java
 */
public class StairsProcessor extends StructureProcessor {
    public static final MapCodec<StairsProcessor> CODEC = RecordCodecBuilder.mapCodec((p_74116_) -> {
        return p_74116_.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter((p_163729_) -> {
            return p_163729_.baseBlock;
        }), BuiltInRegistries.BLOCK.byNameCodec().fieldOf("replace_block").forGetter((p_163727_) -> {
            return p_163727_.replaceBlock;
        }), BuiltInRegistries.BLOCK.byNameCodec().fieldOf("replace_stairs_block").forGetter((p_163727_) -> {
            return p_163727_.replaceStairBlock;
        })).apply(p_74116_, StairsProcessor::new);
    });


    public final Block baseBlock;
    public final Block replaceBlock;
    public final Block replaceStairBlock;

    public StairsProcessor(Block baseBlock, Block replaceBlock, Block replaceStairsBlock) {
        this.baseBlock = baseBlock;
        this.replaceBlock = replaceBlock;
        this.replaceStairBlock = replaceStairsBlock;
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader,
                                                             BlockPos jigsawPiecePos,
                                                             BlockPos jigsawPieceBottomCenterPos,
                                                             StructureTemplate.StructureBlockInfo blockInfoLocal,
                                                             StructureTemplate.StructureBlockInfo blockInfoGlobal,
                                                             StructurePlaceSettings structurePlacementData) {
        if (blockInfoGlobal.state().getBlock() == this.baseBlock && blockInfoGlobal.state().hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
            if (levelReader instanceof WorldGenRegion worldGenRegion && !worldGenRegion.getCenter().equals(new ChunkPos(blockInfoGlobal.pos()))) {
                return blockInfoGlobal;
            }
            Direction direction = blockInfoGlobal.state().getValue(BlockStateProperties.HORIZONTAL_FACING);

            blockInfoGlobal = new StructureTemplate.StructureBlockInfo(blockInfoGlobal.pos(), this.replaceStairBlock.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction), blockInfoGlobal.nbt());
            BlockPos.MutableBlockPos mutableStair = blockInfoGlobal.pos().mutable().move(Direction.DOWN);
            BlockPos.MutableBlockPos mutable = blockInfoGlobal.pos().mutable().move(Direction.DOWN);
            BlockState currBlockState = levelReader.getBlockState(mutable);

            while (mutableStair.getY() > levelReader.getMinY()
                    && mutableStair.getY() < levelReader.getMaxY()
                    && (currBlockState.isAir() || !levelReader.getFluidState(mutableStair).isEmpty())) {
                levelReader.getChunk(mutableStair).setBlockState(mutableStair, this.replaceStairBlock.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction), false);

                mutableStair.move(direction);
                mutableStair.move(Direction.DOWN);
                mutable = mutableStair.mutable();
                currBlockState = levelReader.getBlockState(mutableStair);

                while (mutable.getY() > levelReader.getMinY()
                        && mutable.getY() < levelReader.getMaxY()
                        && (currBlockState.isAir() || !levelReader.getFluidState(mutable).isEmpty())) {

                    levelReader.getChunk(mutable).setBlockState(mutable, this.replaceBlock.defaultBlockState(), false);
                    mutable.move(Direction.DOWN);
                    currBlockState = levelReader.getBlockState(mutable);
                }
            }
        }
        return blockInfoGlobal;
    }

    protected StructureProcessorType<?> getType() {
        return ModStructureProcessorTypes.STAIRS.get();
    }
}