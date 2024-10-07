package bagu_chan.bagus_lib.mixin.client;

import bagu_chan.bagus_lib.client.event.BagusModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    protected PlayerRendererMixin(EntityRendererProvider.Context p_174557_, boolean p_174558_) {
        super(p_174557_, new PlayerModel<>(p_174557_.bakeLayer(p_174558_ ? ModelLayers.PLAYER_SLIM : ModelLayers.PLAYER), p_174558_), 0.5F);
    }

    @Inject(at = @At(value = "HEAD"), method = "renderHand")
    public void test(PoseStack p_117776_, MultiBufferSource p_117777_, int p_117778_, AbstractClientPlayer player, ModelPart p_117780_, ModelPart p_117781_, CallbackInfo callbackInfo) {
        BagusModelEvent.FirstPersonArmAnimate event = new BagusModelEvent.FirstPersonArmAnimate(player, this.model, Minecraft.getInstance().getTimer().getGameTimeDeltaTicks());
        NeoForge.EVENT_BUS.post(event);
    }
}
