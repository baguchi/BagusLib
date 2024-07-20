package bagu_chan.bagus_lib.util.client;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.animation.BaguAnimationController;
import bagu_chan.bagus_lib.api.IBaguAnimate;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

public class AnimationUtil {
    public static final ResourceLocation NOPE = ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "nope");
    @OnlyIn(Dist.CLIENT)
    public static void handleAnimationPacket(Entity entity, ResourceLocation resourceLocation) {
        if (entity instanceof IBaguAnimate baguAnimate) {
            if (entity != null) {
                if (resourceLocation == NOPE) {
                    baguAnimate.getBaguController().stopAllAnimation();
                } else {
                    baguAnimate.getBaguController().startAnimation(resourceLocation);
                }
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
}
