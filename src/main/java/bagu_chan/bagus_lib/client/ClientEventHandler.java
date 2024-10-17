package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.event.BagusModelEvent;
import bagu_chan.bagus_lib.client.game.WaterMelonScreen;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderHandEvent;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Calendar;
import java.util.Date;

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
    public static void animationArmEvent(RenderHandEvent event) {
        PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(Minecraft.getInstance().player);
        EntityModel entityModel = playerrenderer.getModel();

        LivingEntityRenderState renderState = playerrenderer.createRenderState();
        playerrenderer.createRenderState(Minecraft.getInstance().player, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks());
        BagusModelEvent.FirstPersonArmAnimate event2 = new BagusModelEvent.FirstPersonArmAnimate(renderState, entityModel, event.getHand(), event.getPoseStack());
        NeoForge.EVENT_BUS.post(event2);
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
}
