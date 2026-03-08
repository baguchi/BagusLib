package baguchi.bagus_lib.client.game;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.joml.Vector2f;

import javax.annotation.Nonnull;
import java.util.List;

public class FruitObject {
    private Vector2f pos = new Vector2f();
    private Vector2f oldPos = new Vector2f();
    private Vector2f motion = new Vector2f();
    private float rotation;
    private boolean isFix = false;

    private final Fruit fruit;
    private final float restitution;


    public FruitObject(Fruit fruit) {
        this.fruit = fruit;
        this.restitution = fruit.getRestitution();
    }

    @Nonnull
    public Fruit getFruit() {
        return fruit;
    }

    public void setPos(Vector2f pos) {
        this.pos = pos;
        this.oldPos = pos;
    }

    public void setMotion(Vector2f motion) {
        this.motion = motion;
    }

    public Vector2f getMotion() {
        return motion;
    }

    public Vector2f getPosition() {
        return pos;
    }

    public final Vector2f getPosition(float partialTickTime) {
        float endX = Mth.lerp(partialTickTime, this.oldPos.x, this.pos.x);
        float endY = Mth.lerp(partialTickTime, this.oldPos.y, this.pos.y);
        return new Vector2f(endX, endY);
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

        if (!this.isFix) {
            // 両方動く場合、距離は半々
            returnDist = overlap / 2;

            Vector2f moveDirection = unit(new Vector2f(center2Center)).mul(-1);
            //back
            fruitObject.move(moveDirection.x * returnDist, moveDirection.y * returnDist);
            //Reflect
            Vector2f reflect = reflect(fruitObject.motion, new Vector2f(center2Center)).mul(fruitObject.restitution);
            fruitObject.setMotion(reflect);
        }
        var moveDirection = unit(new Vector2f(center2Center));
        //back
        this.move(moveDirection.x * returnDist, moveDirection.y * returnDist);
        //Reflect
        Vector2f reflect = reflect(this.motion, new Vector2f(center2Center)).mul(this.restitution);
        this.setMotion(reflect);
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
        this.pos = WaterMelonCraft.getInstance().collide(new Vector2f(new_x, new_y), this);
        return this.pos;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public void tick() {
        this.oldPos = this.pos;
        motion.y += 0.01F;
        motion.mul(0.96F);
        this.move(this.motion.x, this.motion.y);
    }

    public int collisionAndBig(FruitObject fruitObject, List<FruitObject> fruitObjects) {
        float dist = this.pos.distance(fruitObject.pos.x, fruitObject.pos.y);

        Vector2f center2Center = new Vector2f(
                this.pos.x - fruitObject.pos.x,
                this.pos.y - fruitObject.pos.y
        );
        if (dist > this.fruit.getSize() + fruitObject.fruit.getSize()) {
            return 0;
        }
        if (this.fruit == fruitObject.getFruit()) {
            Fruit fruit1 = Fruit.getNextObject(fruitObject.getFruit());
            if (fruit1 != null) {
                FruitObject fruitObject1 = new FruitObject(fruit1);
                fruitObject1.setPos(this.getPosition().add(center2Center.negate()));
                fruitObjects.add(fruitObject1);
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.GOLDEN_DANDELION_USE, 1F, 0.65F));
            }
            fruitObjects.remove(this);
            fruitObjects.remove(fruitObject);

            return this.fruit.getScore();
        }
        return 0;
    }
}