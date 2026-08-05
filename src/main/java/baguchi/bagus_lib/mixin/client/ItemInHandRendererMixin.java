package baguchi.bagus_lib.mixin.client;

import baguchi.bagus_lib.api.IBaguAnimate;
import baguchi.bagus_lib.client.event.BagusModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {

    @Inject(method = "submitArmWithItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderItem(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;I)V", shift = At.Shift.BEFORE))
    private void submitArmWithItem(
            AbstractClientPlayer player, float frameInterp, float xRot, InteractionHand hand, float attack, ItemStack itemStack, float inverseArmHeight, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, CallbackInfo ci
    ) {
        TickRateManager tickratemanager = Minecraft.getInstance().level.tickRateManager();
        boolean isMainHand = hand == InteractionHand.MAIN_HAND;
        HumanoidArm arm = isMainHand ? player.getMainArm() : player.getMainArm().getOpposite();

        // do the player animation to held item
        if (player != null) {
            AvatarRenderer playerrenderer = (AvatarRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(player);
            EntityModel entityModel = playerrenderer.getModel();
            AvatarRenderState avatarRenderState = playerrenderer.createRenderState();
            playerrenderer.extractRenderState(player, avatarRenderState, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(player)));
            if (entityModel instanceof HumanoidModel<?> humanoidModel) {
                humanoidModel.rightArm.resetPose();
                humanoidModel.leftArm.resetPose();
                if (player instanceof IBaguAnimate baguAnimate) {
                    BagusModelEvent.PostAnimate event2 = new BagusModelEvent.PostAnimate(avatarRenderState, entityModel);
                    NeoForge.EVENT_BUS.post(event2);

                    if (arm == HumanoidArm.RIGHT) {
                        humanoidModel.rightArm.translateAndRotate(poseStack);
                    } else {
                        humanoidModel.leftArm.translateAndRotate(poseStack);
                    }
                }
            }
        }
    }
}
