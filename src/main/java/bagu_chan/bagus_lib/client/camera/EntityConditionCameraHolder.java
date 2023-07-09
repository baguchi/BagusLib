package bagu_chan.bagus_lib.client.camera;

import bagu_chan.bagus_lib.util.GlobalVec3;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.client.event.ViewportEvent;

import java.util.function.Predicate;

public class EntityConditionCameraHolder<T extends Entity> extends EntityCameraHolder<T> {
    public Predicate<T> predicate = Entity::isAlive;

    public EntityConditionCameraHolder(int amount, int duration, GlobalVec3 pos, T entity) {
        super(amount, duration, pos, entity);
    }

    @Override
    public void tick(ViewportEvent.ComputeCameraAngles event) {
        if (!this.hasCondition(this.getEntity())) {
            ++this.time;
            super.tick(event);
        } else {
            this.time = 1;
            super.tick(event);
        }

    }

    protected boolean hasCondition(T entity) {
        return this.predicate.test(entity);
    }

    public EntityConditionCameraHolder<T> setPredicate(Predicate<T> predicate) {
        this.predicate = predicate;
        return this;
    }
}
