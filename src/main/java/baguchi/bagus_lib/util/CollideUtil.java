package baguchi.bagus_lib.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CollideUtil {

    public static Vec3 getPrevPositionVec(Entity collideEntity) {
        return new Vec3(collideEntity.xo, collideEntity.yo, collideEntity.zo);
    }

    public static void collideEntities(Entity collideEntity) {
        Level world = collideEntity.level();
        AABB bounds = collideEntity.getBoundingBox();
        if (bounds == null)
            return;

        Vec3 position = collideEntity.position();
        Vec3 motion = position.subtract(getPrevPositionVec(collideEntity));
        List<Entity> entitiesWithinAABB = world.getEntitiesOfClass(Entity.class, bounds.inflate(2));
        for (Entity entity : entitiesWithinAABB) {
            if (entity != collideEntity && !collideEntity.isPassenger() && !entity.canBeCollidedWith(collideEntity)) {
                Vec3 entityPosition = entity.position();
                Vec3 entityMotion2 = entity.getDeltaMovement();
                Vec3 vec3 = entity.collide(motion);
                if (bounds.expandTowards(0, 0.01F, 0).expandTowards(0, motion.y * 1.15F, 0).intersects(entity.getBoundingBox().expandTowards(0, -0.01F, 0))) {
                    entity.setPos(entityPosition.x + vec3.x, entityPosition.y + vec3.y,
                            entityPosition.z + vec3.z);
                    entity.setOnGround(true);
                }
            }
        }
    }
}
