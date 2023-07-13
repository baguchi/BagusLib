package bagu_chan.bagus_lib.client.camera;

import bagu_chan.bagus_lib.util.GlobalVec3;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.ViewportEvent;

public class CameraHolder {
    public int distance;
    public int duration;
    public int time;

    public float amount;

    private final GlobalVec3 pos;

    public CameraHolder(int distance, int duration, float amount, GlobalVec3 pos) {
        this.distance = distance;
        this.duration = duration;
        this.amount = amount;
        this.pos = pos;
    }

    public CameraHolder(int distance, int duration, GlobalVec3 pos) {
        this(distance, duration, 0.05F, pos);
        this.distance = distance;
        this.duration = duration;
    }

    public int getDistance() {
        return distance;
    }

    public GlobalVec3 getPos() {
        return pos;
    }

    public int getDuration() {
        return duration;
    }

    private void preTick(ViewportEvent.ComputeCameraAngles event) {

        float dist = (float) Mth.clamp((float) this.distance / this.getPos().pos().distanceToSqr(event.getCamera().getPosition()), 0F, 1F);
        float leftTick = ((float) this.getDuration() / (float) this.time);

        if (this.getPos().pos().distanceToSqr(event.getCamera().getPosition()) < this.distance * this.distance && event.getCamera().getEntity().level().dimension() == this.getPos().dimension()) {
            double ticks = event.getCamera().getEntity().tickCount + event.getPartialTick();
            float amount = leftTick * dist;

            event.setPitch(event.getPitch() + amount * Mth.cos((float) (ticks * 3F)) * this.distance * 0.1F * this.amount);
            event.setYaw(event.getYaw() + amount * Mth.cos((float) (ticks * 2.5F)) * this.distance * 0.1F * this.amount);
            event.setRoll(event.getRoll() + amount * Mth.cos((float) (ticks * 2F)) * this.distance * 0.1F * this.amount);
        }
    }

    public void tick(ViewportEvent.ComputeCameraAngles event) {
        ++this.time;
        preTick(event);
    }
}
