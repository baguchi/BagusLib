package baguchi.bagus_lib.mixin.client;

import baguchi.bagus_lib.animation.client.BaguKeyFrameController;
import baguchi.bagus_lib.api.client.IBaguKeyframe;
import baguchi.bagus_lib.client.event.RegisterBagusKeyframeEvents;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Model.class)
public abstract class ModelMixin implements IBaguKeyframe {
    @Unique
    public BaguKeyFrameController BAGU_KEYFRAME_CONTROLLER = new BaguKeyFrameController();

    @Shadow
    @Final
    protected ModelPart root;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo callbackInfo) {
        RegisterBagusKeyframeEvents events = NeoForge.EVENT_BUS.post(new RegisterBagusKeyframeEvents(this.root));
        for (Map.Entry<ResourceLocation, KeyframeAnimation> resourceLocations : events.getAnimationKeyframeMap().entrySet()) {
            BAGU_KEYFRAME_CONTROLLER.addAnimation(resourceLocations);
        }
    }

    @Override
    public BaguKeyFrameController getBaguKeyframeController() {
        return BAGU_KEYFRAME_CONTROLLER;
    }
}
