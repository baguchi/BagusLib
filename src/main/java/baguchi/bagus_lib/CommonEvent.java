package baguchi.bagus_lib;

import baguchi.bagus_lib.event.RegisterBagusAnimationStateEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = BagusLib.MODID)
public class CommonEvent {
    public static final Identifier PAT = Identifier.fromNamespaceAndPath(BagusLib.MODID, "pat");

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
    }

    @SubscribeEvent
    public static void entityAnimationRegister(RegisterBagusAnimationStateEvents events) {
        if (events.getEntity() instanceof Player) {
            events.addAnimationState(PAT);
            events.addFirstPersonPlayableAnimationState(PAT);
        }
    }

}
