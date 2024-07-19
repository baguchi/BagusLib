package bagu_chan.bagus_lib.message;


import bagu_chan.bagus_lib.util.client.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncBagusAnimationsMessage {
    private final int entityId;

    private final int index;

    public SyncBagusAnimationsMessage(int entityId, int index) {
        this.entityId = entityId;
        this.index = index;
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.index);
    }

    public static SyncBagusAnimationsMessage readFromPacket(FriendlyByteBuf buf) {
        return new SyncBagusAnimationsMessage(buf.readInt(), buf.readInt());
    }

    public static void handle(SyncBagusAnimationsMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(message.entityId);
            ClientUtil.handleAnimationPacket(entity, message.index);
        });
    }
}