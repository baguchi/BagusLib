package bagu_chan.bagus_lib.client.game;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;

public enum Fruit {
    MUSHROOM(1, 1, 0.5F, Blocks.MUSHROOM_STEM),
    MUSHROOM_RED(2, 6, 0.425F, Blocks.RED_MUSHROOM_BLOCK),
    MUSHROOM_BROWN(3, 13, 0.375F, Blocks.BROWN_MUSHROOM_BLOCK),
    WARP_WART(4, 22, 0.3F, Blocks.WARPED_WART_BLOCK),
    NETHER_WART(5, 46, 0.125F, Blocks.NETHER_WART_BLOCK),
    SHROOMLIGHT(6, 58, 0.075F, Blocks.SHROOMLIGHT),
    PUMPKIN(7, 72, 0.025F, Blocks.PUMPKIN),
    WATERMELON(8, 100, 0.012F, Blocks.MELON);
    private int size;
    private int score;
    private float restitution;
    private Block fruit;

    Fruit(int size, int score, float restitution, Block fruit) {
        this.size = size;
        this.score = score;
        this.restitution = restitution;
        this.fruit = fruit;
    }

    public float getRestitution() {
        return restitution;
    }

    public int getScore() {
        return score;
    }

    public Block getFruitBlock() {
        return fruit;
    }

    public static Fruit getRandom(RandomSource randomSource) {
        return values()[randomSource.nextInt(3)];
    }

    @Nullable
    public static Fruit getNextObject(Fruit fruit) {
        if (values().length - 1 < fruit.ordinal() + 1) {
            return null;
        }
        return values()[fruit.ordinal() + 1];
    }

    public float getSize() {
        return 0.15F * size;
    }
}