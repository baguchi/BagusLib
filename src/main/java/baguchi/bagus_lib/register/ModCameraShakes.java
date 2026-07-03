package baguchi.bagus_lib.register;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.camera.shake.CameraShake;
import baguchi.bagus_lib.client.camera.shake.EntityCameraShake;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

@EventBusSubscriber(modid = BagusLib.MODID)
public class ModCameraShakes {
    public static final ResourceKey<Registry<MapCodec<? extends CameraShake>>> CAMERA_SHAKE_REGISTRY = createRegistryKey(Identifier.fromNamespaceAndPath(BagusLib.MODID, "camera_shake"));

    public static final DeferredRegister<MapCodec<? extends CameraShake>> CAMERA_SHAKES = DeferredRegister.create(CAMERA_SHAKE_REGISTRY, BagusLib.MODID);
    public static final DeferredHolder<MapCodec<? extends CameraShake>, MapCodec<? extends CameraShake>> CAMERA = CAMERA_SHAKES.register("camera_shake", () -> CameraShake.CODEC);
    public static final DeferredHolder<MapCodec<? extends CameraShake>, MapCodec<? extends CameraShake>> IMAGE_DIALOG = CAMERA_SHAKES.register("entity_camera_shake", () -> EntityCameraShake.CODEC);


    private static Registry<MapCodec<? extends CameraShake>> registry;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        registry = event.create(new RegistryBuilder<>(CAMERA_SHAKE_REGISTRY).sync(true));
    }

    public static Registry<MapCodec<? extends CameraShake>> getRegistry() {
        if (registry == null) {
            throw new IllegalStateException("Registry not yet initialized");
        }
        return registry;
    }
}