package baguchi.bagus_lib;

import baguchi.bagus_lib.event.RegisterBagusAnimationEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.player.CriticalHitEvent;

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
    public static void onCrit(CriticalHitEvent event) {
        Player player = event.getEntity();
        Entity target = event.getTarget();
        if (BagusConfigs.COMMON.multipartAcceptCrit.get()) {
            float f2 = player.getAttackStrengthScale(0.5F);
            boolean flag3 = f2 > 0.9F;
            boolean flag1 = flag3
                    && player.fallDistance > 0.0
                    && !player.onGround()
                    && !player.onClimbable()
                    && !player.isInWater()
                    && !player.isMobilityRestricted()
                    && !player.isPassenger()
                    && target.isAttackable()
                    && !player.isSprinting();
            if (flag1) {
                event.setCriticalHit(true);
                //BagusAnimationUtil.sendAnimation(event.getEntity(), PAT);
            }
        }
    }

}
