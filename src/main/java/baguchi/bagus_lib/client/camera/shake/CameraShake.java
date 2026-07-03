package baguchi.bagus_lib.client.camera.shake;

import baguchi.bagus_lib.register.ModCameraShakes;
import baguchi.bagus_lib.util.GlobalVec3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.event.ViewportEvent;

import java.util.function.Function;

public class CameraShake {

    public static final MapCodec<CameraShake> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.INT.fieldOf("distance").forGetter(CameraShake::getDistance),
                            Codec.INT.fieldOf("duration").forGetter(CameraShake::getDuration),
                            Codec.FLOAT.fieldOf("amount").forGetter(CameraShake::getAmount),
                            GlobalVec3.CODEC.fieldOf("pos").forGetter(CameraShake::getPos))
                    .apply(p_345644_, CameraShake::new)
    );

    public static final Codec<CameraShake> DIRECT_CODEC = ModCameraShakes.getRegistry()
            .byNameCodec()
            .dispatch(CameraShake::codec, Function.identity());
    public final int distance;
    public final int duration;
    public final float amount;

    private final GlobalVec3 pos;

    public CameraShake(int distance, int duration, float amount, GlobalVec3 pos) {
        this.distance = distance;
        this.duration = duration;
        this.amount = amount;
        this.pos = pos;
    }

    public CameraShake(int distance, int duration, GlobalVec3 pos) {
        this(distance, duration, 0.05F, pos);
    }

    public int getDistance() {
        return distance;
    }

    public GlobalVec3 getReferencePos(Level level) {
        return getPos();
    }


    public GlobalVec3 getPos() {
        return pos;
    }

    public int getDuration() {
        return duration;
    }

    public float getAmount() {
        return amount;
    }

    private void preTick(ViewportEvent.ComputeCameraAngles event, int time) {

        float dist = (float) Mth.clamp((float) this.distance / this.getPos().pos().distanceToSqr(event.getCamera().position()), 0F, 1F);
        float leftTick = ((float) this.getDuration() / (float) time);

        if (this.getPos().pos().distanceToSqr(event.getCamera().position()) < this.distance * this.distance && event.getCamera().entity().level().dimension() == this.getPos().dimension()) {
            double ticks = event.getCamera().entity().tickCount + event.getPartialTick();
            float amount = leftTick * dist;

            event.setPitch(event.getPitch() + Minecraft.getInstance().player.getRandom().nextFloat() * amount * 0.1F * Mth.cos((float) (ticks * 3F)));
            event.setYaw(event.getYaw() + Minecraft.getInstance().player.getRandom().nextFloat() * amount * 0.1F * Mth.cos((float) (ticks * 2.5F)));
            event.setRoll(event.getRoll() + Minecraft.getInstance().player.getRandom().nextFloat() * amount * 0.1F * Mth.cos((float) (ticks * 2F)));
        }
    }

    public void tick(ViewportEvent.ComputeCameraAngles event, int time) {
        preTick(event, time);
    }

    public MapCodec<? extends CameraShake> codec() {
        return CODEC;
    }
}
