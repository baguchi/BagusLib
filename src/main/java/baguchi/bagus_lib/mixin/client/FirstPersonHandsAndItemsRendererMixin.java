package baguchi.bagus_lib.mixin.client;

import baguchi.bagus_lib.client.event.BagusModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.FirstPersonHandsAndItemsRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.state.level.PlayerRenderState;
import net.minecraft.world.entity.HumanoidArm;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FirstPersonHandsAndItemsRenderer.class)
public class FirstPersonHandsAndItemsRendererMixin {

    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(method = "renderPlayerHand", at = @At(value = "INVOKE", target = "Lnet/neoforged/neoforge/client/ClientHooks;renderSpecificFirstPersonArm(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;ZLnet/minecraft/client/renderer/state/level/PlayerRenderState;Lnet/minecraft/world/entity/HumanoidArm;Lnet/minecraft/client/model/geom/ModelPart;)Z", shift = At.Shift.BEFORE))
    private void renderPlayerHand(
            PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HumanoidArm arm, PlayerRenderState playerState, CallbackInfo ci
    ) {
        AvatarRenderState avatarRenderState = playerState.avatarRenderState;

        // do the player animation to held item
        if (avatarRenderState != null) {
            AvatarRenderer<?> avatarRenderer = this.minecraft.getEntityRenderDispatcher().getRenderer(avatarRenderState);
            PlayerModel entityModel = avatarRenderer.getModel();
            entityModel.rightArm.resetPose();
            entityModel.leftArm.resetPose();
            BagusModelEvent.PostAnimate event2 = new BagusModelEvent.PostAnimate(avatarRenderState, entityModel);
            NeoForge.EVENT_BUS.post(event2);
            if (event2.getBaguAnimationController() != null && event2.getBaguAnimationController().hasPlayingAnimation()) {
                if (arm == HumanoidArm.RIGHT) {
                    entityModel.rightArm.translateAndRotate(poseStack);
                } else {
                    entityModel.leftArm.translateAndRotate(poseStack);
                }
            }

        }
    }
}
