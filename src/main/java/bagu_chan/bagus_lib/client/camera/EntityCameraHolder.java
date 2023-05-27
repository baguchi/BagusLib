package bagu_chan.bagus_lib.client.camera;

import net.minecraft.core.GlobalPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.ViewportEvent;

public class EntityCameraHolder<T extends Entity> extends CameraHolder{
    private T entity;

    public EntityCameraHolder(int amount, int duration, GlobalPos pos, T entity){
        super(amount, duration, pos);
        this.entity = entity;
    }
    public void tick(ViewportEvent.ComputeCameraAngles event) {
        float dist = (float) Mth.clamp((float) this.amount / this.entity.position().distanceTo(event.getCamera().getPosition()), 0F, 0.1F);
        float leftTick = ((float) this.getDuration() / (float) this.time);
        ++this.time;
        if (dist > 0) {
            double ticks = event.getCamera().getEntity().tickCount + event.getPartialTick();
            float amount = this.amount * leftTick * dist;

            event.setPitch(event.getPitch() + amount * Mth.cos((float) (ticks * amount)) * 0.1F);
            event.setYaw(event.getYaw() + amount * Mth.cos((float) (ticks * amount)) * 0.1F);
            event.setRoll(event.getRoll() + amount * Mth.cos((float) (ticks * amount)) * 0.1F);
        }
    }

    public T getEntity() {
        return entity;
    }
}
