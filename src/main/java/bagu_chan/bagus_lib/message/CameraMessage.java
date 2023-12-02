package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.client.camera.CameraCore;
import bagu_chan.bagus_lib.client.camera.holder.CameraHolder;
import bagu_chan.bagus_lib.util.GlobalVec3;
import bagu_chan.bagus_lib.util.GlobalVec3ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.NetworkEvent;

public class CameraMessage {
    private final int duration;
    private final int distance;
    private final float amount;
    private final GlobalVec3 globalPos;

    public CameraMessage(int distance, int duration, float amount, GlobalVec3 globalPos) {
        this.distance = distance;
        this.duration = duration;

        this.amount = amount;
        this.globalPos = globalPos;
    }

    public static void writeToPacket(CameraMessage packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.distance);
        buf.writeInt(packet.duration);

        buf.writeFloat(packet.amount);
        GlobalVec3ByteBuf.writeGlobalPos(buf, packet.globalPos);
    }

    public static CameraMessage readFromPacket(FriendlyByteBuf buf) {
        return new CameraMessage(buf.readInt(), buf.readInt(), buf.readFloat(), GlobalVec3ByteBuf.readGlobalPos(buf));
    }

    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            CameraCore.addCameraHolderList(level, new CameraHolder(distance, duration, amount, globalPos));
        });
        context.setPacketHandled(true);
    }
}