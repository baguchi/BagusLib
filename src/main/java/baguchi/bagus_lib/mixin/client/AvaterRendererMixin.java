package baguchi.bagus_lib.mixin.client;

import baguchi.bagus_lib.api.IBaguAnimate;
import baguchi.bagus_lib.client.event.BagusModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.ClientAvatarEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.TickRateManager;
import net.minecraft.world.entity.Avatar;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AvatarRenderer.class)
public abstract class AvaterRendererMixin<AvatarlikeEntity extends Avatar & ClientAvatarEntity> extends LivingEntityRenderer<AvatarlikeEntity, AvatarRenderState, PlayerModel> {

    public AvaterRendererMixin(EntityRendererProvider.Context context, PlayerModel model, float shadow) {
        super(context, model, shadow);
    }

    @Inject(method = "renderHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModelPart(Lnet/minecraft/client/model/geom/ModelPart;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IILnet/minecraft/client/renderer/texture/TextureAtlasSprite;)V", shift = At.Shift.BEFORE))
    private void renderHand(
            PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, Identifier skinTexture, ModelPart arm, boolean hasSleeve, CallbackInfo ci
    ) {
        AbstractClientPlayer abstractClientPlayer = Minecraft.getInstance().player;
        TickRateManager tickratemanager = Minecraft.getInstance().level.tickRateManager();

        if (abstractClientPlayer != null) {
            AvatarRenderer playerrenderer = (AvatarRenderer) Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(abstractClientPlayer);
            EntityModel entityModel = playerrenderer.getModel();
            AvatarRenderState avatarRenderState = playerrenderer.createRenderState();
            playerrenderer.extractRenderState(abstractClientPlayer, avatarRenderState, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(!tickratemanager.isEntityFrozen(abstractClientPlayer)));

            if (abstractClientPlayer instanceof IBaguAnimate baguAnimate) {
                BagusModelEvent.PostAnimate event2 = new BagusModelEvent.PostAnimate(avatarRenderState, entityModel);
                NeoForge.EVENT_BUS.post(event2);
            }
        }
    }
}
