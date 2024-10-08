package bagu_chan.bagus_lib;

import bagu_chan.bagus_lib.event.RegisterBagusAnimationEvents;
import bagu_chan.bagus_lib.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = BagusLib.MODID)
public class CommonEvent {
    public static final ResourceLocation TEST = ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "attack");
    public static final ResourceLocation PAT = ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "pat");

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getEntity().level().isClientSide()) {
            if (Minecraft.getInstance().player == event.getEntity()) {
                MiscUtils.updateCosmetic(MiscUtils.BAGUS_COSMETIC_ID, BagusConfigs.CLIENT.enableMiniBagu.get());
            }
        }
    }

    @SubscribeEvent
    public static void entityAnimationRegister(RegisterBagusAnimationEvents events) {
        events.addAnimationState(TEST);
        if (events.getEntity() instanceof Player) {
            events.addAnimationState(PAT);
        }
    }

    /*@SubscribeEvent
    public static void onStartUse(LivingEntityUseItemEvent.Start event) {
        if(!event.getEntity().level().isClientSide) {
            AnimationUtil.sendAnimation(event.getEntity(), PAT);
        }
    }

    @SubscribeEvent
    public static void onStopUse(LivingEntityUseItemEvent.Stop event) {
        if (!event.getEntity().level().isClientSide) {
            AnimationUtil.sendStopAnimation(event.getEntity(), PAT);
        }
    }*/
}
