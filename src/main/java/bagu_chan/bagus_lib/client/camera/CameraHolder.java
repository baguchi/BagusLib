package bagu_chan.bagus_lib.client.camera;

import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraftforge.client.event.ViewportEvent;

public class CameraHolder {
    public int amount;
    public int duration;
    public int time;

    private final GlobalPos pos;

    public CameraHolder(int amount, int duration, GlobalPos pos){
        this.amount = amount;
        this.duration = duration;
        this.pos = pos;
    }

    public int getAmount() {
        return amount;
    }

    public GlobalPos getPos() {
        return pos;
    }

    public int getDuration() {
        return duration;
    }

    private void preTick(ViewportEvent.ComputeCameraAngles event) {

        float dist = (float) Mth.clamp((float) this.amount / this.getPos().pos().distManhattan(event.getCamera().getBlockPosition()), 0F, 0.1F);
        float leftTick = ((float) this.getDuration() / (float) this.time);

        if (dist > 0 && event.getCamera().getEntity().level.dimension() == this.getPos().dimension()) {
            double ticks = event.getCamera().getEntity().tickCount + event.getPartialTick();
            float amount = this.amount * leftTick * dist + 0.001F;

            event.setPitch(event.getPitch() + amount * Mth.cos((float) (ticks * amount)) * 0.5F);
            event.setYaw(event.getYaw() + amount * Mth.cos((float) (ticks * amount)) * 0.5F);
            event.setRoll(event.getRoll() + amount * Mth.cos((float) (ticks * amount)) * 0.5F);
        }
    }

    public void tick(ViewportEvent.ComputeCameraAngles event) {
        ++this.time;
        preTick(event);
    }
}
