package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.client.camera.CameraEvent;
import bagu_chan.bagus_lib.client.camera.CameraHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CameraMessage {
    private final int duration;
    private final int amount;
    private final GlobalPos globalPos;

    public CameraMessage(int duration, int amount, GlobalPos globalPos) {
        this.duration = duration;
        this.amount = amount;
        this.globalPos = globalPos;
    }

    public static void writeToPacket(CameraMessage packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.duration);
        buf.writeInt(packet.amount);
        buf.writeGlobalPos(packet.globalPos);
    }

    public static CameraMessage readFromPacket(FriendlyByteBuf buf) {
        return new CameraMessage(buf.readInt(), buf.readInt(), buf.readGlobalPos());
    }

    public static void handle(CameraMessage message, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        if (context.getDirection().getReceptionSide() == LogicalSide.CLIENT)
            context.enqueueWork(() -> {
                Level level = Minecraft.getInstance().level;
                if (level != null) {
                    CameraEvent.addCameraHolderList(level, new CameraHolder(message.amount, message.duration, message.globalPos));
                }
            });
        ctx.get().setPacketHandled(true);
    }
}