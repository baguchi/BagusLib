package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.api.IBaguData;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateLivingDataMessage {
    private final CompoundTag tag;
    private int entityId;

    public UpdateLivingDataMessage(CompoundTag tag, Entity entity) {
        this.tag = tag;
        this.entityId = entity.getId();
    }

    public UpdateLivingDataMessage(CompoundTag tag, int entityId) {
        this.tag = tag;
        this.entityId = entityId;
    }

    public static void writeToPacket(UpdateLivingDataMessage packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.tag);
        buf.writeInt(packet.entityId);
    }

    public static UpdateLivingDataMessage readFromPacket(FriendlyByteBuf buf) {
        return new UpdateLivingDataMessage(buf.readNbt(), buf.readInt());
    }

    public static void handle(UpdateLivingDataMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Entity entity = Minecraft.getInstance().player.level().getEntity(message.entityId);
                if (entity instanceof IBaguData data) {
                    data.setData(message.tag);
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}