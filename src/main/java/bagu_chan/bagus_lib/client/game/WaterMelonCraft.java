package bagu_chan.bagus_lib.client.game;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.apache.commons.compress.utils.Lists;
import org.joml.Quaternionf;
import org.joml.Vector2f;

import java.util.List;
import java.util.Set;

public class WaterMelonCraft {
    static WaterMelonCraft instance;

    protected final RandomSource random = RandomSource.create();

    private int score;
    private float fallingX;
    private int finishTime = 0;
    private int keyCooldown;
    private static int HEIGHT = 7;
    private static int WIDTH = 5;
    private FruitObject tossFruit;
    private final List<FruitObject> fruitObjects = Lists.newArrayList();
    private boolean gameOver = false;

    private FruitObject nextFruit;

    public WaterMelonCraft() {
        instance = this;
        reset();
    }

    public void tick(Screen screen) {
        if (keyCooldown > 0) {
            keyCooldown--;
        }
        if (!gameOver) {
            if (tossFruit == null) {
                generateFruit();
                generateNextFruit();
            } else {
                float f = 0.15F;
                if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), InputConstants.KEY_DOWN)) {
                    f = 1F;
                }
                if (keyPressed(InputConstants.KEY_LEFT)) {
                    fallingX = restrictX(screen, fallingX - 0.5F);
                }
                if (keyPressed(InputConstants.KEY_RIGHT)) {
                    fallingX = restrictX(screen, fallingX + 0.5F);
                }
                if (keyPressed(InputConstants.KEY_DOWN)) {
                    FruitObject fruitObject = new FruitObject(tossFruit.getFruit());
                    fruitObject.setPos(new Vector2f(fallingX, 1));
                    fruitObjects.add(fruitObject);
                    tossFruit = null;
                    Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.ITEM_PICKUP, 1.0F, 0.7F));
                }
            }

            boolean flag = false;
            boolean flag2 = false;
            Set<FruitObject> fruitObjects1 = Set.copyOf(this.fruitObjects);
            Set<FruitObject> fruitObjects2 = Set.copyOf(this.fruitObjects);
            for (FruitObject fruitObject : fruitObjects1) {

                for (FruitObject fruitObject2 : fruitObjects2) {
                    if (fruitObject != fruitObject2) {
                        int i = fruitObject.collisionAndBig(fruitObject2, this.fruitObjects);
                        fruitObject.collisionBox(fruitObject2);
                        if (i > 0) {
                            flag2 = true;
                            score += i;
                            break;
                        }
                    }
                }
                fruitObject.tick();
                if (flag2) {
                    break;
                }
                if (fruitObject.getPos().y < 0) {
                    flag = true;
                }
            }
            if (flag) {
                ++finishTime;
                if (finishTime >= 60) {
                    gameOver = true;
                }
            } else {
                finishTime = 0;
            }
        }
        if (keyPressed(InputConstants.KEY_W)) {
            reset();
        }
    }

    public static WaterMelonCraft getInstance() {
        return instance;
    }

    protected Vector2f collide(Vector2f p_20273_) {
        if (p_20273_.x < 0) {
            return new Vector2f(0, p_20273_.y);
        }
        if (p_20273_.x > WIDTH) {
            return new Vector2f(WIDTH, p_20273_.y);
        }

        if (p_20273_.y > HEIGHT) {
            return new Vector2f(p_20273_.x, HEIGHT);
        }
        return p_20273_;
    }


    private float restrictX(Screen screen, float xIn) {
        float scale = WIDTH;

        xIn = Mth.clamp(xIn, 0, scale);
        return xIn;
    }

    private boolean keyPressed(int keyId) {
        if (keyCooldown == 0 && InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), keyId)) {
            keyCooldown = 4;
            return true;
        }
        return false;
    }

    private void generateNextFruit() {
        nextFruit = new FruitObject(Fruit.getRandom(random));
    }

    private void generateFruit() {
        tossFruit = nextFruit;
    }

    private void renderFruit(FruitObject fruit, float x, float y, float scale, float offsetX, float offsetY) {
        renderBlockState(fruit.getFruit().getFruitBlock().defaultBlockState(), fruit.getFruit(), offsetX + (x) * scale, offsetY + (y) * scale, scale);
    }

    private static Vector2f transform(Vector2f vector2f, float rotation, Vector2f relativeTo) {
        float i = vector2f.x;
        float k = vector2f.y;
        boolean flag = true;
        Quaternionf quaternionf = Axis.XP.rotationDegrees(rotation);
        return flag ? new Vector2f(i, k).add(quaternionf.x, quaternionf.y) : vector2f;

    }

    private void renderBlockState(BlockState state, Fruit fruit, float offsetX, float offsetY, float size) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getBlockRenderer().getBlockModel(state).getParticleIcon(ModelData.EMPTY);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        float f = size * fruit.getSize();
        bufferbuilder.vertex(-f + offsetX, f + offsetY, 80.0D).uv(sprite.getU0(), sprite.getV1()).endVertex();
        bufferbuilder.vertex(f + offsetX, f + offsetY, 80.0D).uv(sprite.getU1(), sprite.getV1()).endVertex();
        bufferbuilder.vertex(f + offsetX, -f + offsetY, 80.0D).uv(sprite.getU1(), sprite.getV0()).endVertex();
        bufferbuilder.vertex(-f + offsetX, -f + offsetY, 80.0D).uv(sprite.getU0(), sprite.getV0()).endVertex();
        tesselator.end();

    }

    public void render(Screen screen, GuiGraphics gui, float partialTick) {
        float scale = Math.min(screen.width / 15F, screen.height / (float) HEIGHT);
        float offsetX = screen.width / 2F - scale * 5F;
        float offsetY = scale * 0.5F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
        if (tossFruit != null) {
            renderFruit(tossFruit, fallingX, 0, scale, offsetX, offsetY);
        }
        if (nextFruit != null) {
            renderFruit(nextFruit, 0, 0, scale, screen.width * 0.85F, screen.height * 0.4F);
        }
        for (FruitObject fruitObject : this.fruitObjects) {
            renderFruit(fruitObject, fruitObject.getPos().x, fruitObject.getPos().y, scale, offsetX, offsetY);
        }

        float hue = 1f;
        int color = 0xFFFFFF;
        gui.pose().pushPose();
        gui.pose().scale(2, 2, 2);
        gui.drawCenteredString(Minecraft.getInstance().font, "Score", (int) (screen.width * 0.065F), (int) (screen.height * 0.175F), color);
        gui.drawCenteredString(Minecraft.getInstance().font, "" + score, (int) (screen.width * 0.065F), (int) (screen.height * 0.175F) + 10, color);
        gui.pose().popPose();
        gui.drawString(Minecraft.getInstance().font, "[LEFT ARROW] move left", (int) (screen.width * 0.71F), (int) (screen.height * 0.55F), color);
        gui.drawString(Minecraft.getInstance().font, "[RIGHT ARROW] move right", (int) (screen.width * 0.71F), (int) (screen.height * 0.55F) + 10, color);
        gui.drawString(Minecraft.getInstance().font, "[DOWN ARROW] drop fruit", (int) (screen.width * 0.71F), (int) (screen.height * 0.55F) + 30, color);
        gui.drawString(Minecraft.getInstance().font, "[W] start over", (int) (screen.width * 0.71F), (int) (screen.height * 0.55F) + 50, color);
        if (gameOver) {
            gui.pose().pushPose();
            gui.pose().translate((int) (screen.width * 0.5F), (int) (screen.height * 0.5F), 150);
            gui.pose().scale(3, 3, 3);
            gui.drawCenteredString(Minecraft.getInstance().font, "GAME OVER", 0, 0, color);
            gui.pose().popPose();
        }
        if (finishTime > 0) {
            gui.pose().pushPose();
            gui.pose().translate((int) (screen.width * 0.5F), (int) (screen.height * 0.5F), 150);
            gui.pose().scale(2, 2, 2);
            gui.drawCenteredString(Minecraft.getInstance().font, "" + this.finishTime / 20, 0, 0, color);
            gui.pose().popPose();
        }

    }

    public void reset() {
        score = 0;
        fruitObjects.removeAll(fruitObjects);
        gameOver = false;
        generateNextFruit();
        generateFruit();
        generateNextFruit();
    }
}