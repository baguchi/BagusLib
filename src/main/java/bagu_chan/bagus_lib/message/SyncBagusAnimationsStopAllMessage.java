package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.util.client.AnimationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncBagusAnimationsStopAllMessage {
    private final int entityId;

    public SyncBagusAnimationsStopAllMessage(int entityId) {
        this.entityId = entityId;
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    public static SyncBagusAnimationsStopAllMessage readFromPacket(FriendlyByteBuf buf) {
        return new SyncBagusAnimationsStopAllMessage(buf.readInt());
    }

    public static void handle(SyncBagusAnimationsStopAllMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(message.entityId);
            AnimationUtil.handleStopAllAnimationPacket(entity);
        });
    }
}