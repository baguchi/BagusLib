package baguchi.bagus_lib;

import baguchi.bagus_lib.event.RegisterBagusAnimationStateEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Avatar;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = BagusLib.MODID)
public class CommonEvent {
    public static final Identifier PAT = Identifier.fromNamespaceAndPath(BagusLib.MODID, "pat");

    @SubscribeEvent
    public static void entityAnimationRegister(RegisterBagusAnimationStateEvents events) {
        if (events.getEntity() instanceof Avatar) {
            events.addAnimationState(PAT);
            events.addFirstPersonPlayableAnimationState(PAT);
        }
    }
}
