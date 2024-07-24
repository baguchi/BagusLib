package bagu_chan.bagus_lib.util.client;

import bagu_chan.bagus_lib.animation.BaguAnimationController;
import bagu_chan.bagus_lib.api.IBaguAnimate;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsMessage;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsStopAllMessage;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsStopMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

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
}
