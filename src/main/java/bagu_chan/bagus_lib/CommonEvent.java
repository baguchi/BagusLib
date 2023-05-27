package bagu_chan.bagus_lib;

import bagu_chan.bagus_lib.client.camera.CameraEvent;
import bagu_chan.bagus_lib.client.camera.CameraHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BagusLib.MODID)
public class CommonEvent {
    @SubscribeEvent
    public static void event(LivingAttackEvent livingAttackEvent) {
        if (livingAttackEvent.getEntity().level.isClientSide()) {
            CameraEvent.addCameraHolderList(new CameraHolder(16, 20, GlobalPos.of(livingAttackEvent.getEntity().level.dimension(), livingAttackEvent.getEntity().getOnPos())));
        }
    }

    @SubscribeEvent
    public static void event(ExplosionEvent.Start event) {
        Vec3 vec3 = event.getExplosion().getPosition();
        CameraEvent.addCameraHolderList(new CameraHolder(20, 20, GlobalPos.of(event.getLevel().dimension(), BlockPos.containing(vec3))));
    }
}
