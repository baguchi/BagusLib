package baguchi.bagus_lib.packet;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.camera.CameraCore;
import baguchi.bagus_lib.client.camera.shake.CameraShake;
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

    private final CameraShake cameraShake;

    public CameraPacket(CameraShake cameraShake) {
        this.cameraShake = cameraShake;
    }

    public CameraPacket(FriendlyByteBuf buf) {
        this(buf.readLenientJsonWithCodec(CameraShake.CODEC.codec()));
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeJsonWithCodec(CameraShake.CODEC.codec(), this.cameraShake);
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
            CameraCore.addCameraHolderList(level, message.cameraShake);
        });
    }
}