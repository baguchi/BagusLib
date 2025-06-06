package baguchi.bagus_lib;

import baguchi.bagus_lib.event.RegisterBagusAnimationEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid = BagusLib.MODID)
public class CommonEvent {
    public static final ResourceLocation PAT = ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "pat");

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
    }

    @SubscribeEvent
    public static void entityAnimationRegister(RegisterBagusAnimationEvents events) {
        if (events.getEntity() instanceof Player) {
            events.addAnimationState(PAT);
            events.addFirstPersonPlayableAnimationState(PAT);
        }
    }

    @SubscribeEvent
    public static void onStartUse(LivingEntityUseItemEvent.Start event) {
        //BagusAnimationUtil.sendAnimation(event.getEntity(), PAT);
    }

    @SubscribeEvent
    public static void onStopUse(LivingEntityUseItemEvent.Stop event) {
        //BagusAnimationUtil.sendStopAnimation(event.getEntity(), PAT);
    }

}
