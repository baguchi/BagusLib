package baguchi.bagus_lib.mixin.client;

import baguchi.bagus_lib.client.event.BagusModelEvent;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ModelFeatureRenderer.class)
public class ModelFeatureRendererMixin {
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;setupAnim(Ljava/lang/Object;)V", shift = At.Shift.AFTER), method = "prepareModel")
    private <S> void renderModel(ModelFeatureRenderer.Submit<S> submit, CallbackInfo ci, @Local Model<? super S> model) {
        if (submit.state() instanceof LivingEntityRenderState livingEntityRenderState && model instanceof EntityModel<?> entityModel) {
            BagusModelEvent.PostAnimate event = new BagusModelEvent.PostAnimate(livingEntityRenderState, entityModel);
            NeoForge.EVENT_BUS.post(event);
        }
    }
}
