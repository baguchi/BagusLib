package baguchi.bagus_lib.packet;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.camera.CameraCore;
import baguchi.bagus_lib.client.camera.holder.EntityCameraHolder;
import baguchi.bagus_lib.util.GlobalVec3;
import baguchi.bagus_lib.util.GlobalVec3ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class EntityCameraPacket implements CustomPacketPayload, IPayloadHandler<EntityCameraPacket> {

    public static final StreamCodec<FriendlyByteBuf, EntityCameraPacket> STREAM_CODEC = CustomPacketPayload.codec(
            EntityCameraPacket::write, EntityCameraPacket::new
    );
    public static final CustomPacketPayload.Type<EntityCameraPacket> TYPE = new CustomPacketPayload.Type<>(BagusLib.prefix("entity_camera"));

    private final int entityId;

    private final int distance;
    private final int duration;
    private final float amount;
    private final GlobalVec3 globalPos;

    public EntityCameraPacket(int entityId, int distance, int duration, float amount, GlobalVec3 globalPos) {
        this.entityId = entityId;
        this.distance = distance;
        this.duration = duration;

        this.amount = amount;
        this.globalPos = globalPos;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.distance);
        buf.writeInt(this.duration);
        buf.writeFloat(this.amount);
        GlobalVec3ByteBuf.writeGlobalPos(buf, this.globalPos);
    }

    public EntityCameraPacket(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), GlobalVec3ByteBuf.readGlobalPos(buf));
    }

    public void handle(EntityCameraPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(message.entityId);
            CameraCore.addCameraHolderList(level, new EntityCameraHolder(message.distance, message.duration, message.amount, message.globalPos, entity));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}