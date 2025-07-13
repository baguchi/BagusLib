package bagu_chan.bagus_lib;

import bagu_chan.bagus_lib.event.RegisterBagusAnimationEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = BagusLib.MODID)
public class CommonEvent {
    public static final ResourceLocation TEST = ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "attack");
    public static final ResourceLocation PAT = ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "pat");

    @SubscribeEvent
    public static void entityAnimationRegister(RegisterBagusAnimationEvents events) {
        events.addAnimationState(TEST);
        if (events.getEntity() instanceof Player) {
            events.addAnimationState(PAT);
        }
    }
}
