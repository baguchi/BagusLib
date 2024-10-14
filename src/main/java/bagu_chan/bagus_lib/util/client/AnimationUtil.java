package bagu_chan.bagus_lib.util.client;

import bagu_chan.bagus_lib.animation.BaguAnimationController;
import bagu_chan.bagus_lib.api.IBaguAnimate;
import bagu_chan.bagus_lib.api.client.IRootModel;
import bagu_chan.bagus_lib.client.event.BagusModelEvent;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsMessage;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsStopAllMessage;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsStopMessage;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.Optional;

/*
 * This util handle animate every different type mob(of course. You can animate them separately.)
 * In ResourceLocation, enter the id of the animation you want to start or stop.
 * of course. you need register id on bagu_chan.bagus_lib.event.RegisterBagusAnimationEvents.class
 * @author bagu_chan
 */
public class AnimationUtil {
    @OnlyIn(Dist.CLIENT)
    public static void handleAnimationPacket(Entity entity, ResourceLocation resourceLocation) {
        if (entity instanceof IBaguAnimate baguAnimate) {
            if (entity != null) {
                baguAnimate.getBaguController().startAnimation(resourceLocation);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleStopAnimationPacket(Entity entity, ResourceLocation resourceLocation) {
        if (entity instanceof IBaguAnimate baguAnimate) {
            if (entity != null) {
                baguAnimate.getBaguController().stopAnimation(resourceLocation);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void handleStopAllAnimationPacket(Entity entity) {
        if (entity instanceof IBaguAnimate baguAnimate) {
            if (entity != null) {
                baguAnimate.getBaguController().stopAllAnimation();
            }
        }
    }

    @Nullable
    public static BaguAnimationController getAnimationController(Entity entity) {
        if (entity instanceof IBaguAnimate baguAnimate) {
            return baguAnimate.getBaguController();
        }
        return null;
    }

    public static void sendAnimation(Entity entity, ResourceLocation resourceLocation) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new SyncBagusAnimationsMessage(entity.getId(), resourceLocation));
    }

    public static void sendStopAnimation(Entity entity, ResourceLocation resourceLocation) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new SyncBagusAnimationsStopMessage(entity.getId(), resourceLocation));
    }

    public static void sendStopAllAnimation(Entity entity) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new SyncBagusAnimationsStopAllMessage(entity.getId()));
    }

    public static void animationWithHandHeadRotation(BagusModelEvent.PostAnimate event, AnimationDefinition animationDefinition, ResourceLocation resourceLocation) {
        Entity entity = event.getEntity();
        IRootModel rootModel = event.getRootModel();
        if (entity instanceof LivingEntity livingEntity) {
            if (event.isSupportedAnimateModel()) {
                BaguAnimationController animationController = AnimationUtil.getAnimationController(event.getEntity());

                if (animationController != null) {
                    if (animationController.getAnimationState(resourceLocation).isStarted()) {
                        Optional<ModelPart> headPart = rootModel.getBetterAnyDescendantWithName("head");
                        Optional<ModelPart> hatPart = rootModel.getBetterAnyDescendantWithName("hat");
                        Optional<ModelPart> right_arm = rootModel.getBetterAnyDescendantWithName("right_arm");
                        Optional<ModelPart> left_arm = rootModel.getBetterAnyDescendantWithName("left_arm");
                        Optional<ModelPart> right_sleeve = rootModel.getBetterAnyDescendantWithName("right_sleeve");
                        Optional<ModelPart> left_sleeve = rootModel.getBetterAnyDescendantWithName("left_sleeve");

                        Vector3f headVec = new Vector3f();
                        Vector3f rightVec = new Vector3f();
                        Vector3f leftVec = new Vector3f();


                        if (headPart.isPresent()) {
                            headVec = VectorUtil.movePartToVec(headPart.get());
                        }
                        if (right_arm.isPresent()) {
                            rightVec = VectorUtil.movePartToVec(right_arm.get());
                        }
                        if (left_arm.isPresent()) {
                            leftVec = VectorUtil.movePartToVec(left_arm.get());
                        }

                        rootModel.getBagusRoot().getAllParts().forEach(ModelPart::resetPose);
                        if (headPart.isPresent()) {
                            VectorUtil.moveVecToPart(headVec, headPart.get());
                        }
                        if (right_arm.isPresent()) {
                            VectorUtil.moveVecToPart(rightVec, right_arm.get());
                        }
                        if (left_arm.isPresent()) {
                            VectorUtil.moveVecToPart(leftVec, left_arm.get());
                        }
                        if (hatPart.isPresent()) {
                            VectorUtil.moveVecToPart(headVec, hatPart.get());
                        }
                        if (right_sleeve.isPresent()) {
                            VectorUtil.moveVecToPart(rightVec, right_sleeve.get());
                        }
                        if (left_sleeve.isPresent()) {
                            VectorUtil.moveVecToPart(leftVec, left_sleeve.get());
                        }
                        rootModel.animateBagu(animationController.getAnimationState(resourceLocation), animationDefinition, event.getAgeInTick());
                    }
                }
            }
        }
    }

    public static void animationWithHeadRotation(BagusModelEvent.PostAnimate event, AnimationDefinition animationDefinition, ResourceLocation resourceLocation) {
        Entity entity = event.getEntity();
        IRootModel rootModel = event.getRootModel();
        if (entity instanceof LivingEntity livingEntity) {
            if (event.isSupportedAnimateModel()) {
                BaguAnimationController animationController = AnimationUtil.getAnimationController(event.getEntity());

                if (animationController != null) {
                    if (animationController.getAnimationState(resourceLocation).isStarted()) {
                        Optional<ModelPart> headPart = rootModel.getBetterAnyDescendantWithName("head");
                        Optional<ModelPart> hatPart = rootModel.getBetterAnyDescendantWithName("hat");
                        Vector3f headVec = new Vector3f();

                        if (headPart.isPresent()) {
                            headVec = VectorUtil.movePartToVec(headPart.get());
                        }

                        rootModel.getBagusRoot().getAllParts().forEach(ModelPart::resetPose);
                        if (headPart.isPresent()) {
                            VectorUtil.moveVecToPart(headVec, headPart.get());
                        }
                        if (hatPart.isPresent()) {
                            VectorUtil.moveVecToPart(headVec, hatPart.get());
                        }
                        rootModel.animateBagu(animationController.getAnimationState(resourceLocation), animationDefinition, event.getAgeInTick());
                    }
                }
            }
        }
    }
}
