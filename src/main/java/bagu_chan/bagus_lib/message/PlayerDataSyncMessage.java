package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.api.IBaguData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;


/**
 * This is use update data for player.
 * *
 */
public class PlayerDataSyncMessage implements CustomPacketPayload, IPayloadHandler<PlayerDataSyncMessage> {

    public static final StreamCodec<FriendlyByteBuf, PlayerDataSyncMessage> STREAM_CODEC = CustomPacketPayload.codec(
            PlayerDataSyncMessage::write, PlayerDataSyncMessage::new
    );
    public static final CustomPacketPayload.Type<PlayerDataSyncMessage> TYPE = new CustomPacketPayload.Type<>(BagusLib.prefix("player_data"));


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

    public PlayerDataSyncMessage(FriendlyByteBuf buf) {
        this(buf.readNbt(), buf.readInt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeNbt(this.tag);
        buf.writeInt(this.entityId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(PlayerDataSyncMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            if (player != null && player instanceof IBaguData data) {
                data.setBagusData(message.tag);
                }
            });
    }
}