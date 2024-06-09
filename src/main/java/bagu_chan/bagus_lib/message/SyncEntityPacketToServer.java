package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.api.IBaguPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

import java.util.UUID;


/*
 * client on entity spawn sends packet to server asking for data. Server gets data and sends packet to client. Client stores data.
 * So I created this to make that process easier
 *
 */
public class SyncEntityPacketToServer implements CustomPacketPayload, IPayloadHandler<SyncEntityPacketToServer> {

    public static final StreamCodec<FriendlyByteBuf, SyncEntityPacketToServer> STREAM_CODEC = CustomPacketPayload.codec(
            SyncEntityPacketToServer::write, SyncEntityPacketToServer::new
    );
    public static final CustomPacketPayload.Type<SyncEntityPacketToServer> TYPE = new CustomPacketPayload.Type<>(BagusLib.prefix("sync_entity"));


    private final UUID uuid;

    public SyncEntityPacketToServer(UUID uuid) {
        this.uuid = uuid;
    }

    public SyncEntityPacketToServer(FriendlyByteBuf buf) {
        this(buf.readUUID());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(this.uuid);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(SyncEntityPacketToServer message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Entity entity = ((ServerLevel) player.level()).getEntity(message.uuid);
                if (entity instanceof IBaguPacket baguPacket) {
                    baguPacket.resync(entity);
                }

        });
    }
}