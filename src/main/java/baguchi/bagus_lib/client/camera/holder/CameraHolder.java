package baguchi.bagus_lib.client.camera.holder;

import baguchi.bagus_lib.util.GlobalVec3;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.event.ViewportEvent;

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

        float dist = (float) Mth.clamp((float) this.distance / this.getPos().pos().distanceToSqr(event.getCamera().position()), 0F, 1F);
        float leftTick = ((float) this.getDuration() / (float) this.time);

        if (this.getPos().pos().distanceToSqr(event.getCamera().position()) < this.distance * this.distance && event.getCamera().entity().level().dimension() == this.getPos().dimension()) {
            double ticks = event.getCamera().entity().tickCount + event.getPartialTick();
            float amount = leftTick * dist;

            event.setPitch(event.getPitch() + Minecraft.getInstance().player.getRandom().nextFloat() * amount * 0.1F * Mth.cos((float) (ticks * 3F)));
            event.setYaw(event.getYaw() + Minecraft.getInstance().player.getRandom().nextFloat() * amount * 0.1F * Mth.cos((float) (ticks * 2.5F)));
            event.setRoll(event.getRoll() + Minecraft.getInstance().player.getRandom().nextFloat() * amount * 0.1F * Mth.cos((float) (ticks * 2F)));
        }
    }

    public void tick(ViewportEvent.ComputeCameraAngles event) {
        ++this.time;
        preTick(event);
    }
}
