package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.api.IBaguData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

import java.util.Optional;

/**
 * This is use update data for player.
 * *
 */
public class PlayerDataSyncMessage implements CustomPacketPayload {
    public static final ResourceLocation ID = BagusLib.prefix("player_data");

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
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(PlayerDataSyncMessage message, PlayPayloadContext context) {
        context.workHandler().execute(() -> {
            Optional<Player> player = context.player();
            if (player.isPresent() && player.get() instanceof IBaguData data) {
                data.setData(message.tag);
                }
            });
    }
}