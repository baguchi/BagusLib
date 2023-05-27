package bagu_chan.bagus_lib.client.camera;

import net.minecraft.core.GlobalPos;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.ViewportEvent;

public abstract class EntityConditionCameraHolder<T extends Entity> extends EntityCameraHolder<T> {

    public EntityConditionCameraHolder(int amount, int duration, GlobalPos pos, T entity) {
        super(amount, duration, pos, entity);
    }

    @Override
    public void tick(ViewportEvent.ComputeCameraAngles event) {
        if (!this.hasCondition(this.getEntity())) {
            ++this.time;
            super.tick(event);
        } else {
            this.time = 1;
        }

    }

    protected abstract boolean hasCondition(Entity entity);
}
