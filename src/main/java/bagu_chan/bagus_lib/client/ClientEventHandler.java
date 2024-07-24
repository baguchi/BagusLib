package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.CommonEvent;
import bagu_chan.bagus_lib.animation.BaguAnimationController;
import bagu_chan.bagus_lib.api.client.IRootModel;
import bagu_chan.bagus_lib.client.animation.TestAnimations;
import bagu_chan.bagus_lib.client.event.BagusModelEvent;
import bagu_chan.bagus_lib.util.DialogHandler;
import bagu_chan.bagus_lib.util.client.AnimationUtil;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BagusLib.MODID)
public class ClientEventHandler {
    private static boolean initDate = false;

    private static boolean aprilFools = false;


    @SubscribeEvent
    public static void clientLoggOut(PlayerEvent.PlayerLoggedOutEvent event) {
        DialogHandler.INSTANCE.removeAllDialogType();
    }

    @SubscribeEvent
    public static void clientRespawn(PlayerEvent.PlayerRespawnEvent event) {
        DialogHandler.INSTANCE.removeAllDialogType();
    }

    /*@SubscribeEvent
    public static void clientTick(TickEvent.PlayerTickEvent event) {
        if (Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.tickCount == 20) {
                ImageDialogType dialogType = new ImageDialogType();
                dialogType.setDialogueBase(Component.literal("Hmm... something different"));
                dialogType.setSize(280, 336);
                dialogType.setScale(0.25F, 0.25F);
                dialogType.setResourceLocation(new ResourceLocation(BagusLib.MODID, "foresight"));
                DialogHandler.INSTANCE.addOrReplaceDialogType("Host", dialogType);
            }
        }
    }*/

    @SubscribeEvent
    public static void animationEvent(BagusModelEvent.Init bagusModelEvent) {
        IRootModel rootModel = bagusModelEvent.getRootModel();
        if (rootModel != null && rootModel.getBagusRoot() != null) {
            rootModel.getBagusRoot().getAllParts().forEach(ModelPart::resetPose);
        }
    }

    @SubscribeEvent
    public static void animationEvent(BagusModelEvent.PostAnimate bagusModelEvent) {
        IRootModel rootModel = bagusModelEvent.getRootModel();
        BaguAnimationController animationController = AnimationUtil.getAnimationController(bagusModelEvent.getEntity());
        if (rootModel != null && animationController != null && rootModel.getBagusRoot() != null) {
            rootModel.animateBagu(animationController.getAnimationState(CommonEvent.TEST), TestAnimations.ATTACK, bagusModelEvent.getAgeInTick());
        }
    }
}
