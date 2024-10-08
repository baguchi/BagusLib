package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.CommonEvent;
import bagu_chan.bagus_lib.animation.BaguAnimationController;
import bagu_chan.bagus_lib.api.client.IRootModel;
import bagu_chan.bagus_lib.client.animation.TestAnimations;
import bagu_chan.bagus_lib.client.animation.TestPlayerAnimations;
import bagu_chan.bagus_lib.client.event.BagusModelEvent;
import bagu_chan.bagus_lib.client.game.WaterMelonScreen;
import bagu_chan.bagus_lib.util.DialogHandler;
import bagu_chan.bagus_lib.util.client.AnimationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Calendar;
import java.util.Date;

import static bagu_chan.bagus_lib.CommonEvent.PAT;

@EventBusSubscriber(value = Dist.CLIENT, modid = BagusLib.MODID)
public class ClientEventHandler {
    private static boolean initDate = false;

    private static boolean aprilFools = false;

    @SubscribeEvent
    public static void screenRender(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof TitleScreen titleScreen && isAprilFools()) {
            int l = titleScreen.height / 4 + 48;
            event.addListener(Button.builder(Component.translatable("bagus_lib.watermelon"), p_280785_ -> Minecraft.getInstance().setScreen(new WaterMelonScreen(Component.empty())))
                    .bounds(titleScreen.width / 2 - 100, l - 24, 100, 20)
                    .build());
        }
    }

    public static boolean isAprilFools() {
        if (!initDate) {
            initDate = true;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            aprilFools = calendar.get(Calendar.MONTH) + 1 == 4 && calendar.get(Calendar.DATE) == 1;
        }
        return aprilFools && BagusConfigs.COMMON.aprilFool.get();
    }

    @SubscribeEvent
    public static void clientLoggOut(PlayerEvent.PlayerLoggedOutEvent event) {
        DialogHandler.INSTANCE.removeAllDialogType();
    }

    @SubscribeEvent
    public static void clientRespawn(PlayerEvent.PlayerRespawnEvent event) {
        DialogHandler.INSTANCE.removeAllDialogType();
    }


    @SubscribeEvent
    public static void animationInitEvent(BagusModelEvent.Init bagusModelEvent) {
        IRootModel rootModel = bagusModelEvent.getRootModel();
        if (bagusModelEvent.isSupportedAnimateModel()) {
            rootModel.getBagusRoot().getAllParts().forEach(ModelPart::resetPose);
        }
    }

    @SubscribeEvent
    public static void animationnInitEvent(BagusModelEvent.PostAnimate bagusModelEvent) {
        IRootModel rootModel = bagusModelEvent.getRootModel();
        BaguAnimationController animationController = AnimationUtil.getAnimationController(bagusModelEvent.getEntity());
        if (bagusModelEvent.isSupportedAnimateModel() && animationController != null) {
            rootModel.animateBagu(animationController.getAnimationState(CommonEvent.TEST), TestAnimations.ATTACK, bagusModelEvent.getAgeInTick());

            if (animationController.getAnimationState(PAT).isStarted()) {
                if (bagusModelEvent.getModel() instanceof PlayerModel<?> playerModel) {
                    playerModel.leftArm.resetPose();
                    playerModel.rightArm.resetPose();
                }
                if (bagusModelEvent.getEntity() instanceof LivingEntity livingEntity && livingEntity.getUsedItemHand() == InteractionHand.MAIN_HAND) {
                    rootModel.animateBagu(animationController.getAnimationState(PAT), TestPlayerAnimations.pat_right, bagusModelEvent.getAgeInTick());
                } else {
                    rootModel.animateBagu(animationController.getAnimationState(PAT), TestPlayerAnimations.pat_left, bagusModelEvent.getAgeInTick());
                }

            }

            if (bagusModelEvent.getModel() instanceof PlayerModel<?> playerModel) {
                playerModel.leftSleeve.copyFrom(playerModel.leftArm);
                playerModel.rightSleeve.copyFrom(playerModel.rightArm);
            }
        }
    }

    @SubscribeEvent
    public static void animationArmEvent(RenderHandEvent event) {
        PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(Minecraft.getInstance().player);
        EntityModel entityModel = playerrenderer.getModel();
        if (entityModel instanceof IRootModel rootModel) {
            BaguAnimationController animationController = AnimationUtil.getAnimationController(Minecraft.getInstance().player);
            if (isSupportedAnimateModel(rootModel) && animationController != null) {
                BagusModelEvent.FirstPersonArmAnimate event2 = new BagusModelEvent.FirstPersonArmAnimate(Minecraft.getInstance().player, entityModel, Minecraft.getInstance().getTimer().getGameTimeDeltaTicks(), event.getHand(), event.getPoseStack());
                NeoForge.EVENT_BUS.post(event2);
            }
        }
    }

    /*@SubscribeEvent
    public static void patTestEvent(BagusModelEvent.FirstPersonArmAnimate bagusModelEvent) {
        IRootModel rootModel = bagusModelEvent.getRootModel();
        BaguAnimationController animationController = AnimationUtil.getAnimationController(bagusModelEvent.getEntity());
        if (bagusModelEvent.isSupportedAnimateModel() && animationController != null) {
            if(bagusModelEvent.getModel() instanceof PlayerModel<?> playerModel) {
                rootModel.getBagusRoot().getAllParts().forEach(ModelPart::resetPose);
                if(animationController.getAnimationState(PAT).isStarted()) {
                    ModelPart modelPart;
                    if (bagusModelEvent.getArm() == InteractionHand.MAIN_HAND) {
                        rootModel.animateBagu(animationController.getAnimationState(PAT), TestPlayerAnimations.pat_right, bagusModelEvent.getAgeInTick());
                        modelPart = playerModel.rightArm;
                        bagusModelEvent.getPoseStack().mulPose(new Quaternionf().rotateAxis(45, 1, 0, 0));
                        bagusModelEvent.getPoseStack().mulPose(new Quaternionf().rotationZYX(modelPart.zRot, modelPart.yRot, modelPart.xRot));
                    } else {
                        rootModel.animateBagu(animationController.getAnimationState(PAT), TestPlayerAnimations.pat_left, bagusModelEvent.getAgeInTick());
                        modelPart = playerModel.leftArm;
                        bagusModelEvent.getPoseStack().mulPose(new Quaternionf().rotateAxis(45, 1, 0, 0));
                        bagusModelEvent.getPoseStack().mulPose(new Quaternionf().rotationZYX(modelPart.zRot, modelPart.yRot, modelPart.xRot));
                    }
                    playerModel.leftSleeve.copyFrom(playerModel.leftArm);
                    playerModel.rightSleeve.copyFrom(playerModel.rightArm);
                }
            }
        }
    }
*/

    public static boolean isSupportedAnimateModel(IRootModel rootModel) {
        return rootModel != null && rootModel.getBagusRoot() != null;
    }
}
