package baguchi.bagus_lib.client;

import baguchi.bagus_lib.BagusConfigs;
import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.api.IBaguAnimate;
import baguchi.bagus_lib.client.event.BagusModelEvent;
import baguchi.bagus_lib.client.game.WaterMelonScreen;
import baguchi.bagus_lib.client.render.book.Book;
import baguchi.bagus_lib.client.render.book.component.BookComponentDefinition;
import baguchi.bagus_lib.client.render.book.component.DialogBookComponent;
import baguchi.bagus_lib.client.render.book.component.DisplayBookComponent;
import baguchi.bagus_lib.client.render.screen.BookScreen;
import baguchi.bagus_lib.util.DialogHandler;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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

@EventBusSubscriber(value = Dist.CLIENT, modid = BagusLib.MODID)
public class ClientEventHandler {
    private static boolean initDate = false;

    private static boolean aprilFools = false;

    @SubscribeEvent
    public static void screenRender(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof TitleScreen titleScreen && isAprilFools()) {
            int l = titleScreen.height / 4 + 28;
            event.addListener(Button.builder(Component.translatable("bagus_lib.watermelon"), p_280785_ -> Minecraft.getInstance().setScreenAndShow(new WaterMelonScreen(Component.empty())))
                    .bounds(titleScreen.width / 2 + 100, l + 4, 100, 20)
                    .build());
        }
    }

    public static void handleOpenPageTest(Player player) {
        if (player.level().isClientSide() && player == Minecraft.getInstance().player) {
            DisplayBookComponent title = new DisplayBookComponent(121, 158)
                    .imageDisplay(BagusLib.prefix("textures/gui/sprites/test.png"), 4, 10, 100, 100)
                    .textDisplay(Component.literal("test"), 52, 115, 1.2f);

            DialogBookComponent test2 = new DialogBookComponent(Component.literal("test2. testetteafrafdadadsadadasad"), false, 100, 158);

            Book book = new Book(Lists.newArrayList(
                    new BookComponentDefinition(title, BagusLib.prefix("title"), 10, 10, 10, 10)
                    , new BookComponentDefinition(test2, BagusLib.prefix("test2"), 10, 10, 10, 10)

            ), 256, 182, 23, 13, 12, 27,
                    BagusLib.prefix("textures/gui/screen/book/book.png"),
                    BagusLib.prefix("textures/gui/screen/book/book_back.png"),
                    BagusLib.prefix("textures/gui/screen/book/book_back.png"),
                    Identifier.withDefaultNamespace("textures/gui/sprites/widget/page_backward.png"),
                    Identifier.withDefaultNamespace("textures/gui/sprites/widget/page_forward.png"));
            Minecraft.getInstance().setScreenAndShow(new BookScreen(book));
        }
    }

    public static boolean isAprilFools() {
        if (!initDate) {
            initDate = true;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            aprilFools = calendar.get(Calendar.MONTH) + 1 == 4 && calendar.get(Calendar.DATE) == 1;
        }
        return aprilFools && BagusConfigs.COMMON.aprilFool.get() || BagusConfigs.COMMON.alwayAplilFool.get();
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
        AbstractClientPlayer abstractClientPlayer = Minecraft.getInstance().player;

        AvatarRenderer playerrenderer = (AvatarRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(abstractClientPlayer);
        EntityModel entityModel = playerrenderer.getModel();
        if (abstractClientPlayer instanceof IBaguAnimate baguAnimate) {
            boolean playFlag = baguAnimate.getBaguController().hasPlayingAnimation();

            if (playFlag) {
                TickRateManager tickratemanager = Minecraft.getInstance().level.tickRateManager();

                AvatarRenderState playerRenderState = playerrenderer.createRenderState();
                playerrenderer.extractRenderState(abstractClientPlayer, playerRenderState, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(abstractClientPlayer)));

                renderArmWithItem(abstractClientPlayer, event.getHand(), event.getSwingProgress(), event.getItemStack(), event.getEquipProgress(), event.getPoseStack(), event.getSubmitNodeCollector(), playerRenderState, event.getPackedLight(), entityModel);
                event.setCanceled(true);
            }
        }
    }

    private static void renderArmWithItem(AbstractClientPlayer abstractClientPlayer, InteractionHand interactionHand, float swingProgress, ItemStack stack, float equipProgress, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, AvatarRenderState avatarRenderState, int light, EntityModel entityModel) {
        if (!abstractClientPlayer.isScoping()) {
            boolean flag = interactionHand == InteractionHand.MAIN_HAND;
            HumanoidArm humanoidarm = flag ? abstractClientPlayer.getMainArm() : abstractClientPlayer.getMainArm().getOpposite();
            boolean flag2 = humanoidarm == HumanoidArm.RIGHT;
            poseStack.pushPose();
            if (!abstractClientPlayer.isInvisible()) {
                poseStack.pushPose();
                renderPlayerArm(poseStack, submitNodeCollector, avatarRenderState, light, equipProgress, swingProgress, humanoidarm, entityModel);
                boolean flag3 = humanoidarm == HumanoidArm.LEFT;

                if (entityModel instanceof PlayerModel playerModel) {

                    playerModel.translateToHand(avatarRenderState, humanoidarm, poseStack);
                    poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F));
                    poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
                    poseStack.translate((float) (flag3 ? -1 : 1) / 16.0F, 0.125F, -0.625F);

                }
                renderItem(abstractClientPlayer, stack, flag3 ? ItemDisplayContext.THIRD_PERSON_LEFT_HAND : ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, poseStack, submitNodeCollector, light);
                poseStack.popPose();
            }
            poseStack.popPose();
        }

    }

    private static void renderItem(LivingEntity livingEntity, ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light) {
        if (!stack.isEmpty()) {
            Minecraft.getInstance().getEntityRenderDispatcher().getItemInHandRenderer().renderItem(livingEntity, stack, itemDisplayContext, poseStack, submitNodeCollector, light);
        }

    }

    private static void renderPlayerArm(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, AvatarRenderState avatarRenderState, int light, float equipProgress, float swingProgress, HumanoidArm humanoidArm, EntityModel entityModel) {
        boolean flag = humanoidArm != HumanoidArm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = Mth.sqrt(swingProgress);
        float f2 = -0.3F * Mth.sin(f1 * (float) Math.PI);
        float f3 = 0.4F * Mth.sin(f1 * ((float) Math.PI * 2F));
        float f4 = -0.4F * Mth.sin(swingProgress * (float) Math.PI);
        poseStack.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equipProgress * -0.6F, f4 + -0.71999997F);
        poseStack.mulPose(Axis.YP.rotationDegrees(f * 45.0F));
        float f5 = Mth.sin(swingProgress * swingProgress * (float) Math.PI);
        float f6 = Mth.sin(f1 * (float) Math.PI);
        poseStack.mulPose(Axis.YP.rotationDegrees(f * f6 * 70.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(f * f5 * -20.0F));
        AbstractClientPlayer abstractclientplayer = Minecraft.getInstance().player;
        poseStack.translate(f * -1.0F, 3.6F, 3.5F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(f * 120.0F));
        poseStack.mulPose(Axis.XP.rotationDegrees(200.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(f * -135.0F));
        poseStack.translate(f * 5.6F, 0.0F, 0.0F);
        Identifier resourcelocation = abstractclientplayer.getSkin().body().texturePath();
        if (flag) {
            renderRightHand(poseStack, submitNodeCollector, light, resourcelocation, abstractclientplayer.isModelPartShown(PlayerModelPart.RIGHT_SLEEVE), abstractclientplayer, avatarRenderState, entityModel);
        } else {
            renderLeftHand(poseStack, submitNodeCollector, light, resourcelocation, abstractclientplayer.isModelPartShown(PlayerModelPart.LEFT_SLEEVE), abstractclientplayer, avatarRenderState, entityModel);
        }

    }

    public static void renderRightHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, Identifier resourceLocation, boolean partShown, AbstractClientPlayer player, AvatarRenderState avatarRenderState, EntityModel entityModel) {
        if (!ClientHooks.renderSpecificFirstPersonArm(poseStack, submitNodeCollector, light, player, HumanoidArm.RIGHT)) {
            if (entityModel instanceof PlayerModel playerModel) {
                renderHand(poseStack, submitNodeCollector, light, resourceLocation, playerModel.rightArm, partShown, avatarRenderState, playerModel);
            }
        }

    }

    public static void renderLeftHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, Identifier resourceLocation, boolean partShown, AbstractClientPlayer player, AvatarRenderState avatarRenderState, EntityModel entityModel) {
        if (!ClientHooks.renderSpecificFirstPersonArm(poseStack, submitNodeCollector, light, player, HumanoidArm.LEFT)) {
            if (entityModel instanceof PlayerModel playerModel) {
                renderHand(poseStack, submitNodeCollector, light, resourceLocation, playerModel.leftArm, partShown, avatarRenderState, playerModel);
            }
        }

    }

    private static void renderHand(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int p_117778_, Identifier resourceLocation, ModelPart modelPart, boolean modelShown, AvatarRenderState avatarRenderState, PlayerModel playermodel) {
        modelPart.resetPose();
        modelPart.visible = true;
        playermodel.leftSleeve.visible = modelShown;
        playermodel.rightSleeve.visible = modelShown;
        //playermodel.leftArm.zRot = -0.1F;
        //playermodel.rightArm.zRot = 0.1F;
        AbstractClientPlayer abstractClientPlayer = Minecraft.getInstance().player;

        if (abstractClientPlayer != null) {
            AvatarRenderer playerrenderer = (AvatarRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(abstractClientPlayer);
            EntityModel entityModel = playerrenderer.getModel();
            if (abstractClientPlayer instanceof IBaguAnimate baguAnimate) {
                BagusModelEvent.PostAnimate event2 = new BagusModelEvent.PostAnimate(avatarRenderState, entityModel);
                NeoForge.EVENT_BUS.post(event2);
            }
            submitNodeCollector.submitModelPart(modelPart, poseStack, RenderTypes.entityTranslucent(resourceLocation), avatarRenderState.lightCoords,
                    OverlayTexture.NO_OVERLAY,
                    null);
        }
    }

    /*@SubscribeEvent
    public static void soundTestEvent(ClientTickEvent.Post bagusModelEvent) {
        if(Minecraft.getInstance().player != null){
            LocalPlayer player = Minecraft.getInstance().player;
            if(player.tickCount == 100){
                DialogBuilder builder = new DialogBuilder();
                Optional<ResourceKey<SoundEvent>> soundEvent = BuiltInRegistries.SOUND_EVENT.getResourceKey(SoundEvents.WOLF_AMBIENT);
                builder.setSoundEvent(BuiltInRegistries.SOUND_EVENT.get(soundEvent.get()).get());
                builder.setDialogueBase(Component.literal("test"));
                DialogHandler.INSTANCE.addOrReplaceDialogType("bagu", ModDialogs.DIALOGS.get().getClone(builder.writeTag()));
            }
        }
    }
*/
}
