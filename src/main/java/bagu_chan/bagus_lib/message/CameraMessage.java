package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.client.camera.CameraEvent;
import bagu_chan.bagus_lib.client.camera.CameraHolder;
import bagu_chan.bagus_lib.util.GlobalVec3;
import bagu_chan.bagus_lib.util.GlobalVec3ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CameraMessage {
    private final int duration;
    private final int amount;
    private final GlobalVec3 globalPos;

    public CameraMessage(int duration, int amount, GlobalVec3 globalPos) {
        this.duration = duration;
        this.amount = amount;
        this.globalPos = globalPos;
    }

    public static void writeToPacket(CameraMessage packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.duration);
        buf.writeInt(packet.amount);
        GlobalVec3ByteBuf.writeGlobalPos(buf, packet.globalPos);
    }

    public static CameraMessage readFromPacket(FriendlyByteBuf buf) {
        return new CameraMessage(buf.readInt(), buf.readInt(), GlobalVec3ByteBuf.readGlobalPos(buf));
    }

    public static void handle(CameraMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            context.enqueueWork(() -> {
                Level level = context.getSender().level;
                if (level != null) {
                    CameraEvent.addCameraHolderList(level, new CameraHolder(message.amount, message.duration, message.globalPos));
                }
            });
        }
        ctx.get().setPacketHandled(true);
    }
}