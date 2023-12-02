package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.api.IBaguData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.fml.LogicalSide;
import net.neoforged.neoforge.network.NetworkEvent;

/**
 * This is use update data for player.
 * If you want update non player entity data see {@link LivingDataSyncMessage}
 * *
 */
public class PlayerDataSyncMessage {
    private final CompoundTag tag;
    private int entityId;

    public PlayerDataSyncMessage(CompoundTag tag, Entity entity) {
        this.tag = tag;
        this.entityId = entity.getId();
    }

    public PlayerDataSyncMessage(CompoundTag tag, int entityId) {
        this.tag = tag;
        this.entityId = entityId;
    }

    public static void writeToPacket(PlayerDataSyncMessage packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.tag);
        buf.writeInt(packet.entityId);
    }

    public static PlayerDataSyncMessage readFromPacket(FriendlyByteBuf buf) {
        return new PlayerDataSyncMessage(buf.readNbt(), buf.readInt());
    }

    public void handle(NetworkEvent.Context context) {
        if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            context.enqueueWork(() -> {
                Player player = context.getSender();
                if (player != null && player instanceof IBaguData data) {
                    data.setData(tag);
                }
            });
        } else if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level().getEntity(entityId);
                if (entity instanceof Player && entity instanceof IBaguData data) {
                    data.setData(tag);
                }
            });
        }
        context.setPacketHandled(true);
    }
}