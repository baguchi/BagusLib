package bagu_chan.bagus_lib;

import bagu_chan.bagus_lib.event.RegisterBagusAnimationEvents;
import bagu_chan.bagus_lib.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BagusLib.MODID)
public class CommonEvent {
    public static final ResourceLocation TEST = new ResourceLocation(BagusLib.MODID, "attack");


    /*@SubscribeEvent
    public static void event(LivingHurtEvent livingAttackEvent) {
        if (livingAttackEvent.getEntity().level.isClientSide()) {
            CameraEvent.addCameraHolderList(new CameraHolder(16, 20, GlobalPos.of(livingAttackEvent.getEntity().level.dimension(), livingAttackEvent.getEntity().blockPosition())));
        }
    }

    @SubscribeEvent
    public static void event(ExplosionEvent.Start event) {
        Vec3 vec3 = event.getExplosion().getPosition();
        CameraEvent.addCameraHolderList(new CameraHolder(20, 20, GlobalPos.of(event.getLevel().dimension(), BlockPos.containing(vec3))));
    }*/

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            if (Minecraft.getInstance().player == event.getEntity()) {
                MiscUtils.updateCosmetic(MiscUtils.BAGUS_COSMETIC_ID, BagusConfigs.CLIENT.enableMiniBagu.get());
            }
        }
    }

    @SubscribeEvent
    public static void entityAnimationRegister(RegisterBagusAnimationEvents events) {
        events.addAnimationState(TEST);
    }
}
