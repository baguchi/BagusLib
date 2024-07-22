package bagu_chan.bagus_lib.util.client;

import bagu_chan.bagus_lib.animation.BaguAnimationController;
import bagu_chan.bagus_lib.api.IBaguAnimate;
import bagu_chan.bagus_lib.message.BagusPacketHandler;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsMessage;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsStopAllMessage;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsStopMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

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
        BagusPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SyncBagusAnimationsMessage(entity.getId(), resourceLocation));
    }

    public static void sendStopAnimation(Entity entity, ResourceLocation resourceLocation) {
        BagusPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SyncBagusAnimationsStopMessage(entity.getId(), resourceLocation));
    }

    public static void sendStopAllAnimation(Entity entity) {
        BagusPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SyncBagusAnimationsStopAllMessage(entity.getId()));
    }
}
