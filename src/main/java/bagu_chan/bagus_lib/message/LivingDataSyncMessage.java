package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.api.IBaguData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.NetworkEvent;

/**
 * This is use update data for non player.
 * If you want update player data see {@link PlayerDataSyncMessage}
 * *
 */
public class LivingDataSyncMessage {
    private final CompoundTag tag;
    private int entityId;

    public LivingDataSyncMessage(CompoundTag tag, Entity entity) {
        this.tag = tag;
        this.entityId = entity.getId();
    }

    public LivingDataSyncMessage(CompoundTag tag, int entityId) {
        this.tag = tag;
        this.entityId = entityId;
    }

    public static void writeToPacket(LivingDataSyncMessage packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.tag);
        buf.writeInt(packet.entityId);
    }

    public static LivingDataSyncMessage readFromPacket(FriendlyByteBuf buf) {
        return new LivingDataSyncMessage(buf.readNbt(), buf.readInt());
    }

    public void handle(NetworkEvent.Context context) {
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level().getEntity(entityId);
                if (entity != null && entity instanceof IBaguData data) {
                    data.setData(tag);
                }
            });
        }
        context.setPacketHandled(true);
    }
}