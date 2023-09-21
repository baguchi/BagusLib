package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.client.camera.CameraEvent;
import bagu_chan.bagus_lib.client.camera.CameraHolder;
import bagu_chan.bagus_lib.util.GlobalVec3;
import bagu_chan.bagus_lib.util.GlobalVec3ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CameraMessage {
    private final int duration;
    private final int distance;
    private final float amount;
    private final GlobalVec3 globalPos;

    public CameraMessage(int duration, int distance, float amount, GlobalVec3 globalPos) {
        this.duration = duration;
        this.distance = distance;
        this.amount = amount;
        this.globalPos = globalPos;
    }

    public static void writeToPacket(CameraMessage packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.duration);
        buf.writeInt(packet.distance);
        buf.writeFloat(packet.amount);
        GlobalVec3ByteBuf.writeGlobalPos(buf, packet.globalPos);
    }

    public static CameraMessage readFromPacket(FriendlyByteBuf buf) {
        return new CameraMessage(buf.readInt(), buf.readInt(), buf.readFloat(), GlobalVec3ByteBuf.readGlobalPos(buf));
    }

    public static void handle(CameraMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.getLevel();
            if (level == null) {
                return;
            }
            CameraEvent.addCameraHolderList(level, new CameraHolder(message.distance, message.duration, message.amount, message.globalPos));
        });
        ctx.get().setPacketHandled(true);
    }
}