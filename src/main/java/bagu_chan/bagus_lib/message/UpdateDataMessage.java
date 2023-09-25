package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.api.IBaguData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.LogicalSide;

public class UpdateDataMessage {
    private final CompoundTag tag;
    private int entityId;

    public UpdateDataMessage(CompoundTag tag, Entity entity) {
        this.tag = tag;
        this.entityId = entity.getId();
    }

    public UpdateDataMessage(CompoundTag tag, int entityId) {
        this.tag = tag;
        this.entityId = entityId;
    }

    public static void writeToPacket(UpdateDataMessage packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.tag);
        buf.writeInt(packet.entityId);
    }

    public static UpdateDataMessage readFromPacket(FriendlyByteBuf buf) {
        return new UpdateDataMessage(buf.readNbt(), buf.readInt());
    }

    public void handle(CustomPayloadEvent.Context context) {
        if (context.getDirection().getReceptionSide() == LogicalSide.SERVER)
            context.enqueueWork(() -> {
                Player player = context.getSender();
                if (player != null && player instanceof IBaguData data) {
                    data.setData(tag);
                }
            });
        context.setPacketHandled(true);
    }
}