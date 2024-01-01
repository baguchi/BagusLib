package bagu_chan.bagus_lib.client.game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import org.joml.Vector2f;

import javax.annotation.Nonnull;
import java.util.List;

public class FruitObject {
    private Vector2f pos = new Vector2f();
    private Vector2f motion = new Vector2f();
    private float rotation;

    private Fruit fruit;

    public FruitObject(Fruit fruit) {
        this.fruit = fruit;
    }

    @Nonnull
    public Fruit getFruit() {
        return fruit;
    }


    public void setFruit(@Nonnull Fruit fruit) {
        this.fruit = fruit;
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
    }

    public void setMotion(Vector2f motion) {
        this.motion = motion;
    }

    public Vector2f getMotion() {
        return motion;
    }

    public Vector2f getPos() {
        return pos;
    }

    public void collisionBox(FruitObject fruitObject) {
        float dist = this.pos.distance(fruitObject.pos.x, fruitObject.pos.y);
        if (dist > this.fruit.getSize() + fruitObject.fruit.getSize()) {
            // 衝突していないので何もしない
            return;
        }
        float overlap = this.fruit.getSize() + fruitObject.fruit.getSize() - dist;

        // 円->円のベクトル
        Vector2f center2Center = new Vector2f(
                this.pos.x - fruitObject.pos.x,
                this.pos.y - fruitObject.pos.y
        );
        float returnDist = overlap;

        // 両方動く場合、距離は半々
        returnDist = overlap / 2;

        Vector2f moveDirection = unit(center2Center).mul(-1);
        //back
        fruitObject.move(moveDirection.x * returnDist, moveDirection.y * returnDist);
        //Reflect
        fruitObject.motion = reflect(fruitObject.motion, center2Center).mul(0.5F);

        moveDirection = unit(center2Center);
        //back
        this.move(moveDirection.x * returnDist, moveDirection.y * returnDist);
        //Reflect
        this.motion = reflect(fruitObject.motion, center2Center);
    }

    public Vector2f reflect(Vector2f vec2, Vector2f vec21) {
        Vector2f normUnit = vec21;
        float dot = vec2.dot(normUnit);
        if (dot == 0) {
            return vec2.mul(-1);
        } else {
            Vector2f ref_vec = normUnit.mul(-2 * dot);
            return vec2.add(ref_vec.x, ref_vec.y);
        }
    }

    public Vector2f unit(Vector2f vec2) {
        return vec2.mul(1 / vec2.length());
    }

    public Vector2f move(float dx, float dy) {

        float new_x = this.pos.x + dx;
        float new_y = this.pos.y + dy;
        this.pos = WaterMelonCraft.getInstance().collide(new Vector2f(new_x, new_y));
        return this.pos;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void tick() {
        this.move(this.motion.x, this.motion.y);
        motion.mul(0.96F);
        motion.y += 0.01F;

    }

    public int collisionAndBig(FruitObject fruitObject, List<FruitObject> fruitObjects) {
        float dist = this.pos.distance(fruitObject.pos.x, fruitObject.pos.y);
        if (dist > this.fruit.getSize() + fruitObject.fruit.getSize()) {
            return 0;
        }
        if (this.fruit == fruitObject.getFruit()) {
            Fruit fruit1 = Fruit.getNextObject(fruitObject.getFruit());
            fruitObjects.remove(this);
            fruitObjects.remove(fruitObject);
            if (fruit1 != null) {
                FruitObject fruitObject1 = new FruitObject(fruit1);
                fruitObject1.setPos(this.getPos());
                fruitObjects.add(fruitObject1);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.CAT_EAT, 1.0F, 1.0F));
            }

            return this.fruit.getScore();
        }
        return 0;
    }
}