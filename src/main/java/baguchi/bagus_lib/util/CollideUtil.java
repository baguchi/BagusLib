package baguchi.bagus_lib.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CollideUtil {

    public static Vec3 getPrevPositionVec(Entity collideEntity) {
        return new Vec3(collideEntity.xOld, collideEntity.yOld, collideEntity.zOld);
    }

    public static void collideEntities(Entity collideEntity) {
        Level world = collideEntity.level();
        AABB bounds = collideEntity.getBoundingBox();
        if (bounds == null)
            return;

        Vec3 position = collideEntity.position();
        Vec3 motion = position.subtract(getPrevPositionVec(collideEntity));
        AABB boundCheck = bounds.inflate(0.18F);
        List<Entity> entitiesWithinAABB = world.getEntitiesOfClass(Entity.class, boundCheck);
        for (Entity entity : entitiesWithinAABB) {
            if (entity != collideEntity && !collideEntity.isPassenger() && !entity.canBeCollidedWith(collideEntity)) {
                Vec3 entityPosition = entity.position();
                Vec3 entityMotion = position.subtract(getPrevPositionVec(entity));
                Vec3 vec3 = entity.collide(motion);
                if (boundCheck.expandTowards(motion).intersects(entity.getBoundingBox())) {
                    entity.setPos(entityPosition.x + vec3.x, entityPosition.y + vec3.y,
                            entityPosition.z + vec3.z);
                    entity.setOnGround(true);
                    entity.fallDistance = 0f;
                }
            }
        }
    }
}
