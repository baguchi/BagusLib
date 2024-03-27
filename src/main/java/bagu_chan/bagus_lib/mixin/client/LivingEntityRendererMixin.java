package bagu_chan.bagus_lib.mixin.client;

import bagu_chan.bagus_lib.client.event.BagusModelEvent;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    @Shadow
    protected M model;

    protected LivingEntityRendererMixin(EntityRendererProvider.Context p_174008_) {
        super(p_174008_);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;prepareMobModel(Lnet/minecraft/world/entity/Entity;FFF)V", shift = At.Shift.BEFORE), method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    protected void scalePre(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
        BagusModelEvent.Scale event = new BagusModelEvent.Scale(p_115308_, this.model, p_115310_, p_115311_);
        NeoForge.EVENT_BUS.post(event);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;prepareMobModel(Lnet/minecraft/world/entity/Entity;FFF)V", shift = At.Shift.BEFORE), method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    protected void prepareModelPre(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
        BagusModelEvent.Init event = new BagusModelEvent.Init(p_115308_, this.model, p_115310_);
        NeoForge.EVENT_BUS.post(event);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", shift = At.Shift.BEFORE), method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    protected void setupAnimPre(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
        BagusModelEvent.PreAnimate event = new BagusModelEvent.PreAnimate(p_115308_, this.model, p_115310_);
        NeoForge.EVENT_BUS.post(event);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", shift = At.Shift.AFTER), method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    protected void setupAnimPost(T p_115308_, float p_115309_, float p_115310_, PoseStack p_115311_, MultiBufferSource p_115312_, int p_115313_, CallbackInfo ci) {
        BagusModelEvent.PostAnimate event = new BagusModelEvent.PostAnimate(p_115308_, this.model, p_115310_);
        NeoForge.EVENT_BUS.post(event);
    }
}
