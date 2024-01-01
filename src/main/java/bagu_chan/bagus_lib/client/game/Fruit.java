package bagu_chan.bagus_lib.client.game;

import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;

public enum Fruit {
    MUSHROOM(1, 1, Blocks.MUSHROOM_STEM),
    MUSHROOM_RED(2, 6, Blocks.RED_MUSHROOM_BLOCK),
    MUSHROOM_BROWN(3, 13, Blocks.BROWN_MUSHROOM_BLOCK),
    WARP_WART(4, 22, Blocks.WARPED_WART_BLOCK),
    NETHER_WART(5, 46, Blocks.NETHER_WART_BLOCK),
    PUMPKIN(6, 58, Blocks.PUMPKIN),
    WATERMELON(7, 66, Blocks.MELON);
    private int size;
    private int score;
    private Block fruit;

    Fruit(int size, int score, Block fruit) {
        this.size = size;
        this.score = score;
        this.fruit = fruit;
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
        return 0.2F * size;
    }
}