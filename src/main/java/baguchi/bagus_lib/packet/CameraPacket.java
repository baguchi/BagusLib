package baguchi.bagus_lib.packet;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.camera.CameraCore;
import baguchi.bagus_lib.client.camera.holder.CameraHolder;
import baguchi.bagus_lib.util.GlobalVec3;
import baguchi.bagus_lib.util.GlobalVec3ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class CameraPacket implements CustomPacketPayload, IPayloadHandler<CameraPacket> {
    public static final StreamCodec<FriendlyByteBuf, CameraPacket> STREAM_CODEC = CustomPacketPayload.codec(
            CameraPacket::write, CameraPacket::new
    );
    public static final CustomPacketPayload.Type<CameraPacket> TYPE = new CustomPacketPayload.Type<>(BagusLib.prefix("camera"));

    private final int duration;
    private final int distance;
    private final float amount;
    private final GlobalVec3 globalPos;

    public CameraPacket(int distance, int duration, float amount, GlobalVec3 globalPos) {
        this.distance = distance;
        this.duration = duration;

        this.amount = amount;
        this.globalPos = globalPos;
    }

    public CameraPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readFloat(), GlobalVec3ByteBuf.readGlobalPos(buf));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.distance);
        buf.writeInt(this.duration);

        buf.writeFloat(this.amount);
        GlobalVec3ByteBuf.writeGlobalPos(buf, this.globalPos);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handle(CameraPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            CameraCore.addCameraHolderList(level, new CameraHolder(message.distance, message.duration, message.amount, message.globalPos));
        });
    }
}