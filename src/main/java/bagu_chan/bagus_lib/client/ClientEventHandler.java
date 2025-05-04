package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.CommonEvent;
import bagu_chan.bagus_lib.animation.BaguAnimationController;
import bagu_chan.bagus_lib.api.IBaguAnimate;
import bagu_chan.bagus_lib.api.client.IRootModel;
import bagu_chan.bagus_lib.client.animation.TestAnimations;
import bagu_chan.bagus_lib.client.animation.TestPlayerAnimations;
import bagu_chan.bagus_lib.client.event.BagusModelEvent;
import bagu_chan.bagus_lib.client.game.WaterMelonScreen;
import bagu_chan.bagus_lib.util.DialogHandler;
import bagu_chan.bagus_lib.util.client.AnimationUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.ClientHooks;
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
        AbstractClientPlayer abstractClientPlayer = Minecraft.getInstance().player;

        PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(abstractClientPlayer);
        EntityModel entityModel = playerrenderer.getModel();
        if (abstractClientPlayer instanceof IBaguAnimate baguAnimate) {
            boolean playFlag = baguAnimate.getBaguController().hasPlayingAnimation();

            if (playFlag) {

                renderArmWithItem(abstractClientPlayer, event.getHand(), event.getSwingProgress(), event.getItemStack(), event.getEquipProgress(), event.getPoseStack(), event.getMultiBufferSource(), event.getPackedLight(), entityModel);
                event.setCanceled(true);
            }
        }
    }

    private static void renderArmWithItem(AbstractClientPlayer p_109372_, InteractionHand p_109375_, float p_109376_, ItemStack p_109377_, float p_109378_, PoseStack p_109379_, MultiBufferSource p_109380_, int p_109381_, EntityModel entityModel) {
        if (!p_109372_.isScoping()) {
            boolean flag = p_109375_ == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidarm = flag ? p_109372_.getMainArm() : p_109372_.getMainArm().getOpposite();
            boolean flag2 = humanoidarm == HumanoidArm.RIGHT;
            p_109379_.pushPose();
            if (!p_109372_.isInvisible()) {
                p_109379_.pushPose();
                renderPlayerArm(p_109379_, p_109380_, p_109381_, p_109378_, p_109376_, humanoidarm, entityModel);
                boolean flag3 = humanoidarm == HumanoidArm.LEFT;

                if (entityModel instanceof PlayerModel playerModel) {

                    playerModel.translateToHand(humanoidarm, p_109379_);
                    p_109379_.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    p_109379_.mulPose(Axis.YP.rotationDegrees(180.0F));
                    p_109379_.translate((float) (flag3 ? -1 : 1) / 16.0F, 0.125F, -0.625F);

                }
                renderItem(p_109372_, p_109377_, flag3 ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, p_109379_, p_109380_, p_109381_, flag2);
                p_109379_.popPose();
            }
            p_109379_.popPose();
        }

    }

    private static void renderItem(LivingEntity p_270072_, ItemStack p_270793_, ItemDisplayContext p_270837_, PoseStack p_270974_, MultiBufferSource p_270686_, int p_270103_, boolean rightHand) {
        if (!p_270793_.isEmpty()) {
            Minecraft.getInstance().getItemRenderer().renderStatic(p_270072_, p_270793_, p_270837_, !rightHand, p_270974_, p_270686_, p_270072_.level(), p_270103_, OverlayTexture.NO_OVERLAY, p_270072_.getId() + p_270837_.ordinal());
        }

    }

    private static void renderPlayerArm(PoseStack p_109347_, MultiBufferSource p_109348_, int p_109349_, float p_109350_, float p_109351_, HumanoidArm p_109352_, EntityModel entityModel) {
        boolean flag = p_109352_ != HumanoidArm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = Mth.sqrt(p_109351_);
        float f2 = -0.3F * Mth.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * Mth.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * Mth.sin(p_109351_ * (float) Math.PI);
        p_109347_.translate(f * (f2 + 0.64000005F), f3 + -0.6F + p_109350_ * -0.6F, f4 + -0.71999997F);
        p_109347_.mulPose(Axis.YP.rotationDegrees(f * 45.0F));
        float f5 = Mth.sin(p_109351_ * p_109351_ * (float) Math.PI);
        float f6 = Mth.sin(f1 * (float) Math.PI);
        p_109347_.mulPose(Axis.YP.rotationDegrees(f * f6 * 70.0F));
        p_109347_.mulPose(Axis.ZP.rotationDegrees(f * f5 * -20.0F));
        AbstractClientPlayer abstractclientplayer = Minecraft.getInstance().player;
        p_109347_.translate(f * -1.0F, 3.6F, 3.5F);
        p_109347_.mulPose(Axis.ZP.rotationDegrees(f * 120.0F));
        p_109347_.mulPose(Axis.XP.rotationDegrees(200.0F));
        p_109347_.mulPose(Axis.YP.rotationDegrees(f * -135.0F));
        p_109347_.translate(f * 5.6F, 0.0F, 0.0F);
        PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(abstractclientplayer);
        ResourceLocation resourcelocation = abstractclientplayer.getSkin().texture();
        if (flag) {
            renderRightHand(p_109347_, p_109348_, p_109349_, resourcelocation, abstractclientplayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE), abstractclientplayer, entityModel);
        } else {
            renderLeftHand(p_109347_, p_109348_, p_109349_, resourcelocation, abstractclientplayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE), abstractclientplayer, entityModel);
        }

    }

    public static void renderRightHand(PoseStack p_117771_, MultiBufferSource p_117772_, int p_117773_, ResourceLocation p_363694_, boolean p_366898_, AbstractClientPlayer player, EntityModel entityModel) {
        if (!ClientHooks.renderSpecificFirstPersonArm(p_117771_, p_117772_, p_117773_, player, HumanoidArm.RIGHT)) {
            if (entityModel instanceof PlayerModel playerModel) {
                renderHand(p_117771_, p_117772_, p_117773_, p_363694_, playerModel.rightArm, p_366898_, playerModel);
            }
        }

    }

    public static void renderLeftHand(PoseStack p_117814_, MultiBufferSource p_117815_, int p_117816_, ResourceLocation p_361745_, boolean p_366730_, AbstractClientPlayer player, EntityModel entityModel) {
        if (!ClientHooks.renderSpecificFirstPersonArm(p_117814_, p_117815_, p_117816_, player, HumanoidArm.LEFT)) {
            if (entityModel instanceof PlayerModel playerModel) {
                renderHand(p_117814_, p_117815_, p_117816_, p_361745_, playerModel.leftArm, p_366730_, playerModel);
            }
        }

    }

    private static void renderHand(PoseStack p_117776_, MultiBufferSource p_117777_, int p_117778_, ResourceLocation p_360319_, ModelPart p_117780_, boolean p_366655_, PlayerModel playermodel) {
        p_117780_.resetPose();
        p_117780_.visible = true;
        playermodel.leftSleeve.visible = p_366655_;
        playermodel.rightSleeve.visible = p_366655_;
        //playermodel.leftArm.zRot = -0.1F;
        //playermodel.rightArm.zRot = 0.1F;
        AbstractClientPlayer abstractClientPlayer = Minecraft.getInstance().player;

        if (abstractClientPlayer != null) {
            PlayerRenderer playerrenderer = (PlayerRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(abstractClientPlayer);
            EntityModel entityModel = playerrenderer.getModel();
            if (abstractClientPlayer instanceof IBaguAnimate baguAnimate) {
                BagusModelEvent.PostAnimate event2 = new BagusModelEvent.PostAnimate(abstractClientPlayer, entityModel, Minecraft.getInstance().getTimer().getGameTimeDeltaTicks());
                NeoForge.EVENT_BUS.post(event2);
            }
        }

        p_117780_.render(p_117776_, p_117777_.getBuffer(RenderType.entityTranslucent(p_360319_)), p_117778_, OverlayTexture.NO_OVERLAY);
    }

    public static boolean isSupportedAnimateModel(IRootModel rootModel) {
        return rootModel != null && rootModel.getBagusRoot() != null;
    }
}
