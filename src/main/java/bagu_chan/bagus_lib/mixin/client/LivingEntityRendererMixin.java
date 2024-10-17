package bagu_chan.bagus_lib.mixin.client;

import bagu_chan.bagus_lib.api.IBagusExtraRenderState;
import bagu_chan.bagus_lib.client.event.BagusModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends EntityRenderer<T, S> {

    @Shadow
    protected M model;

    protected LivingEntityRendererMixin(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;)V", shift = At.Shift.BEFORE), method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    protected void scalePre(S p_361886_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
        BagusModelEvent.Scale event = new BagusModelEvent.Scale(p_361886_, this.model, p_115311_);
        NeoForge.EVENT_BUS.post(event);
    }


    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;)V", shift = At.Shift.BEFORE), method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    protected void setupAnimPost(S p_361886_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
        BagusModelEvent.PostAnimate event = new BagusModelEvent.PostAnimate(p_361886_, this.model);
        NeoForge.EVENT_BUS.post(event);
    }

    @Inject(at = @At(value = "TAIL"), method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V")

    public void extractRenderState(T p_362733_, S p_360515_, float p_361157_, CallbackInfo ci) {
        if (p_360515_ instanceof IBagusExtraRenderState bagusExtraRenderState) {
            bagusExtraRenderState.setBagusLib$chestItem(p_362733_.getItemBySlot(EquipmentSlot.CHEST));
            bagusExtraRenderState.setBagusLib$legItem(p_362733_.getItemBySlot(EquipmentSlot.LEGS));
            bagusExtraRenderState.setBagusLib$feetItem(p_362733_.getItemBySlot(EquipmentSlot.FEET));
        }
    }
}
