package bagu_chan.bagus_lib.client.camera;

import bagu_chan.bagus_lib.util.GlobalVec3;
import net.minecraft.world.entity.Entity;

public class EntityCameraHolder<T extends Entity> extends CameraHolder{
    private T entity;

    public EntityCameraHolder(int amount, int duration, GlobalVec3 pos, T entity) {
        super(amount, duration, pos);
        this.entity = entity;
    }

    public T getEntity() {
        return entity;
    }
}
