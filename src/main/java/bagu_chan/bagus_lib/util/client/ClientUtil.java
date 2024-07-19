package bagu_chan.bagus_lib.util.client;

import bagu_chan.bagus_lib.api.IBaguAnimate;
import bagu_chan.bagus_lib.message.BagusPacketHandler;
import bagu_chan.bagus_lib.message.SyncBagusAnimationsMessage;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.PacketDistributor;

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
        BagusPacketHandler.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new SyncBagusAnimationsMessage(entity.getId(), index));
    }
}
