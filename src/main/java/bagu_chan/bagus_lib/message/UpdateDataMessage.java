package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.api.IData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class UpdateDataMessage {
    private final CompoundTag tag;

    public UpdateDataMessage(CompoundTag compoundTag) {
        this.tag = compoundTag;
    }

    public static void writeToPacket(UpdateDataMessage packet, FriendlyByteBuf buf) {
        buf.writeNbt(packet.tag);
    }

    public static UpdateDataMessage readFromPacket(FriendlyByteBuf buf) {
        return new UpdateDataMessage(buf.readNbt());
    }

    public static void handle(UpdateDataMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.SERVER)
            context.enqueueWork(() -> {
                Player player = ctx.get().getSender();
                if (player != null && player instanceof IData data) {
                    data.setData(message.tag);
                }
            });
        ctx.get().setPacketHandled(true);
    }
}