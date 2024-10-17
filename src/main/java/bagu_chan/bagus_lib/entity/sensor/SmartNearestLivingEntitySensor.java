package bagu_chan.bagus_lib.entity.sensor;

import com.google.common.collect.ImmutableSet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.phys.AABB;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

public class SmartNearestLivingEntitySensor<T extends LivingEntity> extends Sensor<T> {
    protected void doTick(ServerLevel level, T entity) {
        AABB aabb = entity.getBoundingBox().inflate((double) this.radiusXZ(entity), (double) this.radiusY(entity), (double) this.radiusXZ(entity));
        List<LivingEntity> list = level.getEntitiesOfClass(LivingEntity.class, aabb, (p_26717_) -> {
            return p_26717_ != entity && p_26717_.isAlive();
        });
        list.sort(Comparator.comparingDouble(entity::distanceToSqr));
        Brain<?> brain = entity.getBrain();
        brain.setMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES, list);
        brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, new NearestVisibleLivingEntities(level, entity, list));
    }

    protected int radiusXZ(T entity) {
        AttributeInstance attributeInstance = entity.getAttribute(Attributes.FOLLOW_RANGE);
        return attributeInstance != null ? (int) attributeInstance.getValue() : 16;
    }

    protected int radiusY(T entity) {
        AttributeInstance attributeInstance = entity.getAttribute(Attributes.FOLLOW_RANGE);
        return attributeInstance != null ? (int) attributeInstance.getValue() : 16;
    }

    public Set<MemoryModuleType<?>> requires() {
        return ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
    }
}