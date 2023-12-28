package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.api.IBaguPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

/*
 * client on entity spawn sends packet to server asking for data. Server gets data and sends packet to client. Client stores data.
 * So I created this to make that process easier
 *
 */
public class SyncEntityPacketToServer {
    private final UUID uuid;

    public SyncEntityPacketToServer(UUID uuid) {
        this.uuid = uuid;
    }

    public static void writeToPacket(SyncEntityPacketToServer packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.uuid);
    }

    public static SyncEntityPacketToServer readFromPacket(FriendlyByteBuf buf) {
        return new SyncEntityPacketToServer(buf.readUUID());
    }

    public static void handle(SyncEntityPacketToServer message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.SERVER) {
            context.enqueueWork(() -> {
                Player player = context.getSender();
                Entity entity = ((ServerLevel) player.level).getEntity(message.uuid);
                if (entity instanceof IBaguPacket baguPacket) {
                    baguPacket.resync(entity, entity.getId());
                }
            });
        }
        context.setPacketHandled(true);
    }
}