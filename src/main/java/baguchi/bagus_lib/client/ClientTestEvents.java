package baguchi.bagus_lib.client;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.animation.BaguAnimationController;
import baguchi.bagus_lib.animation.client.BaguKeyFrameController;
import baguchi.bagus_lib.client.event.BagusModelEvent;
import net.minecraft.client.animation.KeyframeAnimation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

import javax.annotation.Nullable;

@EventBusSubscriber(modid = BagusLib.MODID, value = Dist.CLIENT)
public class ClientTestEvents {
    @SubscribeEvent
    public static void onAnimateModelEvent(BagusModelEvent.PostAnimate event) {
        BaguAnimationController animations = event.getBaguAnimationController();
        @Nullable BaguKeyFrameController keyFrames = event.getBaguKeyframeController();
        if (keyFrames != null) {
            KeyframeAnimation cough = keyFrames.getKeyframe(TofuAnimations.COUGH);
            KeyframeAnimation thrownRight = keyFrames.getKeyframe(TofuAnimations.THROWN_RIGHT);
            KeyframeAnimation thrownLeft = keyFrames.getKeyframe(TofuAnimations.THROWN_LEFT);
            KeyframeAnimation busterRight = keyFrames.getKeyframe(TofuAnimations.BUSTER_RIGHT);
            KeyframeAnimation busterLeft = keyFrames.getKeyframe(TofuAnimations.BUSTER_LEFT);
            if (animations != null && cough != null) {
                cough.apply(animations.getAnimationState(TofuAnimations.COUGH), event.getEntityRenderState().ageInTicks);
            }

            if (animations != null) {
                if (thrownRight != null && thrownLeft != null) {
                    if (animations.getAnimationState(TofuAnimations.THROWN_RIGHT).isStarted() || animations.getAnimationState(TofuAnimations.THROWN_LEFT).isStarted()) {
                        event.getModel().root().getChild("right_arm").resetPose();
                        event.getModel().root().getChild("left_arm").resetPose();


                        thrownRight.apply(animations.getAnimationState(TofuAnimations.THROWN_RIGHT), event.getEntityRenderState().ageInTicks);
                        thrownLeft.apply(animations.getAnimationState(TofuAnimations.THROWN_LEFT), event.getEntityRenderState().ageInTicks);
                    }
                }
                if (busterRight != null && busterLeft != null) {
                    if (animations.getAnimationState(TofuAnimations.BUSTER_RIGHT).isStarted() || animations.getAnimationState(TofuAnimations.BUSTER_LEFT).isStarted()) {
                        event.getModel().root().getChild("right_arm").resetPose();
                        event.getModel().root().getChild("left_arm").resetPose();
                        event.getModel().root().getChild("right_leg").resetPose();
                        event.getModel().root().getChild("left_leg").resetPose();
                        event.getModel().root().getChild("body").resetPose();
                        event.getModel().root().getChild("head").resetPose();

                        event.getModel().root().getChild("head").xRot = event.getEntityRenderState().xRot * (float) (Math.PI / 180.0);
                        event.getModel().root().getChild("head").yRot = event.getEntityRenderState().yRot * (float) (Math.PI / 180.0);
                        busterRight.apply(animations.getAnimationState(TofuAnimations.BUSTER_RIGHT), event.getEntityRenderState().ageInTicks);
                        busterLeft.apply(animations.getAnimationState(TofuAnimations.BUSTER_LEFT), event.getEntityRenderState().ageInTicks);
                    }
                }
            }
        }
    }
}
