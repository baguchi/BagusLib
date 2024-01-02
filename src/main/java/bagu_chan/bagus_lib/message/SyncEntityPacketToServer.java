package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.api.IBaguPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;
import java.util.UUID;


/*
 * client on entity spawn sends packet to server asking for data. Server gets data and sends packet to client. Client stores data.
 * So I created this to make that process easier
 *
 */
public class SyncEntityPacketToServer implements CustomPacketPayload {

    public static final ResourceLocation ID = BagusLib.prefix("sync_packet");
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
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(SyncEntityPacketToServer message, PlayPayloadContext context) {
        context.workHandler().execute(() -> {
            Optional<Player> player = context.player();
            if (player.isPresent()) {
                Entity entity = ((ServerLevel) player.get().level()).getEntity(message.uuid);
                if (entity instanceof IBaguPacket baguPacket) {
                    baguPacket.resync(entity, entity.getId());
                }
            }
        });
    }
}