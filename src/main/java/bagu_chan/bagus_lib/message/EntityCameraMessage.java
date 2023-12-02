package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.client.camera.CameraCore;
import bagu_chan.bagus_lib.client.camera.holder.EntityCameraHolder;
import bagu_chan.bagus_lib.util.GlobalVec3;
import bagu_chan.bagus_lib.util.GlobalVec3ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.NetworkEvent;

public class EntityCameraMessage {
    private final int entityId;
    private final int duration;
    private final int distance;
    private final float amount;
    private final GlobalVec3 globalPos;

    public EntityCameraMessage(int entityId, int duration, int distance, float amount, GlobalVec3 globalPos) {
        this.entityId = entityId;
        this.duration = duration;
        this.distance = distance;
        this.amount = amount;
        this.globalPos = globalPos;
    }

    public static void writeToPacket(EntityCameraMessage packet, FriendlyByteBuf buf) {
        buf.writeInt(packet.entityId);
        buf.writeInt(packet.duration);
        buf.writeInt(packet.distance);
        buf.writeFloat(packet.amount);
        GlobalVec3ByteBuf.writeGlobalPos(buf, packet.globalPos);
    }

    public static EntityCameraMessage readFromPacket(FriendlyByteBuf buf) {
        return new EntityCameraMessage(buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), GlobalVec3ByteBuf.readGlobalPos(buf));
    }

    public void handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(entityId);
            CameraCore.addCameraHolderList(level, new EntityCameraHolder(distance, duration, amount, globalPos, entity));
        });
        context.setPacketHandled(true);
    }
}