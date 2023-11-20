package bagu_chan.bagus_lib.register;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.entity.sensor.SmartNearestLivingEntitySensor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModSensors {
    public static final DeferredRegister<SensorType<?>> SENSOR_TYPES = DeferredRegister.create(BuiltInRegistries.SENSOR_TYPE, BagusLib.MODID);

    public static final Supplier<SensorType<SmartNearestLivingEntitySensor<LivingEntity>>> SMART_NEAREST_LIVING_ENTITY_SENSOR = SENSOR_TYPES.register("smart_nearest_living_entity_sensor",
            () -> new SensorType<>(SmartNearestLivingEntitySensor::new));
}
