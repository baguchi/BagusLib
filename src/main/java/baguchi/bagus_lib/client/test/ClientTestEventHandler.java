package baguchi.bagus_lib.client.test;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.CommonEvent;
import baguchi.bagus_lib.client.animation.TestAnimations;
import baguchi.bagus_lib.client.event.BagusModelEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(value = Dist.CLIENT, modid = BagusLib.MODID)
public class ClientTestEventHandler {


    @SubscribeEvent
    public static void modelEventInit(BagusModelEvent.PostAnimate event) {

        event.animate(event.getBaguAnimationController().getAnimationState(CommonEvent.PAT), TestAnimations.test, event.getEntityRenderState().ageInTicks);
    }
}
