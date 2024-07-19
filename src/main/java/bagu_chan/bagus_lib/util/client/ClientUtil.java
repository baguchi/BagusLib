package bagu_chan.bagus_lib.util.client;

import bagu_chan.bagus_lib.api.IBaguAnimate;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsMessage;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClientUtil {
    @OnlyIn(Dist.CLIENT)
    public static void handleAnimationPacket(Entity entity, int index) {
        if (entity instanceof IBaguAnimate baguAnimate) {
            if (entity != null) {
                if (index == -1) {
                    baguAnimate.getBaguController().stopAllAnimation();
                } else {
                    baguAnimate.getBaguController().startAnimation(index);
                }
            }
        }
    }

    public static void sendAnimation(Entity entity, int index) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new SyncBagusAnimationsMessage(entity.getId(), index));
    }
}
