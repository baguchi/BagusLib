package baguchi.bagus_lib.client.game;


import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
    private static final int HEIGHT = 7;
    private static final int WIDTH = 5;
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
                if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_DOWN)) {
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

            if (keyPressed(InputConstants.KEY_W)) {
                reset();
            }

            boolean flag2 = false;
            Set<FruitObject> fruitObjects1 = Set.copyOf(this.fruitObjects);
            Set<FruitObject> fruitObjects2 = Set.copyOf(this.fruitObjects);
            this.fruitObjects.forEach(fruitObject -> {
                boolean flag = false;
                fruitObject.tick();
                if (fruitObject.getPos().y < 0) {
                    flag = true;
                }
                if (flag) {
                    ++finishTime;
                    if (finishTime >= 60) {
                        gameOver = true;
                    }
                } else {
                    finishTime = 0;
                }
            });

            for (FruitObject fruitObject : fruitObjects1) {

                for (FruitObject fruitObject2 : fruitObjects2) {
                    if (fruitObject != fruitObject2) {
                        int i = fruitObject.collisionAndBig(fruitObject2, this.fruitObjects);
                        if (i > 0) {
                            flag2 = true;
                            score += i;
                            break;
                        } else {
                            fruitObject.collisionBox(fruitObject2);
                        }
                    }
                }
                if (flag2) {
                    break;
                }

            }
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
        if (keyCooldown == 0 && InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), keyId)) {
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

    private void renderFruit(GuiGraphics gui, FruitObject fruit, float x, float y, float scale, float offsetX, float offsetY) {
        renderBlockState(gui, fruit, offsetX + (x) * scale, offsetY + (y) * scale, scale);
    }

    private static Vector2f transform(Vector2f vector2f, float rotation, Vector2f relativeTo) {
        float i = vector2f.x;
        float k = vector2f.y;
        boolean flag = true;
        Quaternionf quaternionf = Axis.XP.rotationDegrees(rotation);
        return flag ? new Vector2f(i, k).add(quaternionf.x, quaternionf.y) : vector2f;

    }

    private void renderBlockState(GuiGraphics gui, FruitObject fruit, float offsetX, float offsetY, float size) {
        gui.pose().pushMatrix();
        TextureAtlasSprite sprite = Minecraft.getInstance().getBlockRenderer().getBlockModel(fruit.getFruit().getFruitBlock().defaultBlockState()).particleIcon();
        float f = size * fruit.getFruit().getSize();
        PoseStack stack = new PoseStack();
        stack.pushPose();
        int i = ARGB.white(1F);
        gui.pose().translate(-f / 2F, -f / 2F);
        //stack.mulPose(fruit.getRotation());
        gui.pose().translate(offsetX, offsetY);
        gui.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, (int) (f * 2F), (int) (f * 2F), i);
        gui.pose().popMatrix();
    }

    public void render(Screen screen, GuiGraphics gui, float partialTick) {
        float scale = Math.min(screen.width / 15F, screen.height / (float) HEIGHT);
        float offsetX = screen.width / 2F - scale * 5F;
        float offsetY = scale * 0.5F;
        if (tossFruit != null) {
            renderFruit(gui, tossFruit, fallingX, 0, scale, offsetX, offsetY);
        }
        if (nextFruit != null) {
            renderFruit(gui, nextFruit, 0, 0, scale, screen.width * 0.85F, screen.height * 0.4F);
        }
        for (FruitObject fruitObject : this.fruitObjects) {
            renderFruit(gui, fruitObject, fruitObject.getPos().x, fruitObject.getPos().y, scale, offsetX, offsetY);
        }

        float hue = 1f;
        int i = ARGB.white(1F);
        int color = i;
        gui.pose().pushMatrix();
        gui.pose().scale(2, 2);
        gui.drawCenteredString(Minecraft.getInstance().font, "Score", (int) (screen.width * 0.065F), (int) (screen.height * 0.175F), color);
        gui.drawCenteredString(Minecraft.getInstance().font, "" + score, (int) (screen.width * 0.065F), (int) (screen.height * 0.175F) + 10, color);
        gui.pose().popMatrix();
        gui.drawString(Minecraft.getInstance().font, "[LEFT ARROW] move left", (int) (screen.width * 0.71F), (int) (screen.height * 0.55F), color);
        gui.drawString(Minecraft.getInstance().font, "[RIGHT ARROW] move right", (int) (screen.width * 0.71F), (int) (screen.height * 0.55F) + 10, color);
        gui.drawString(Minecraft.getInstance().font, "[DOWN ARROW] drop fruit", (int) (screen.width * 0.71F), (int) (screen.height * 0.55F) + 30, color);
        gui.drawString(Minecraft.getInstance().font, "[W] start over", (int) (screen.width * 0.71F), (int) (screen.height * 0.55F) + 50, color);
        if (gameOver) {
            gui.pose().pushMatrix();
            gui.pose().translate((int) (screen.width * 0.5F), (int) (screen.height * 0.5F));
            gui.pose().scale(3, 3);
            gui.drawCenteredString(Minecraft.getInstance().font, "GAME OVER", 0, 0, color);
            gui.pose().popMatrix();
        }
        if (finishTime > 0) {
            gui.pose().pushMatrix();
            gui.pose().translate((int) (screen.width * 0.5F), (int) (screen.height * 0.5F));
            gui.pose().scale(2, 2);
            gui.drawCenteredString(Minecraft.getInstance().font, "" + this.finishTime / 20, 0, 0, color);
            gui.pose().popMatrix();
        }

    }

    public void reset() {
        score = 0;
        fruitObjects.clear();
        gameOver = false;
        generateNextFruit();
        generateFruit();
        generateNextFruit();
    }
}
