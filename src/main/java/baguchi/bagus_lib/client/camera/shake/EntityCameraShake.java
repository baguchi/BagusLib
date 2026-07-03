package baguchi.bagus_lib.client.camera.shake;

import baguchi.bagus_lib.util.GlobalVec3;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.level.Level;

public class EntityCameraShake extends CameraShake {
    public static final MapCodec<? extends EntityCameraShake> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.INT.fieldOf("distance").forGetter(EntityCameraShake::getDistance),
                            Codec.INT.fieldOf("duration").forGetter(EntityCameraShake::getDuration),
                            Codec.FLOAT.fieldOf("amount").forGetter(EntityCameraShake::getAmount),
                            GlobalVec3.CODEC.fieldOf("pos").forGetter(EntityCameraShake::getPos),
                            EntityReference.<Entity>codec().fieldOf("entity").forGetter(EntityCameraShake::getEntity))
                    .apply(p_345644_, EntityCameraShake::new)
    );

    private final EntityReference<Entity> entity;

    public EntityCameraShake(int distance, int duration, float amount, GlobalVec3 pos, EntityReference<Entity> entity) {
        super(distance, duration, amount, pos);
        this.entity = entity;
    }

    public EntityReference<Entity> getEntity() {
        return entity;
    }

    @Override
    public GlobalVec3 getReferencePos(Level level) {
        GlobalVec3 vec3 = this.getPos();
        Entity entity1 = getEntity().getEntity(level, Entity.class);
        if (entity1 != null) {
            return GlobalVec3.of(vec3.dimension(), entity1.position());
        }
        return vec3;
    }

    @Override
    public MapCodec<? extends CameraShake> codec() {
        return CODEC;
    }
}
