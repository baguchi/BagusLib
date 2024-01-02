package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.camera.CameraCore;
import bagu_chan.bagus_lib.client.camera.holder.EntityCameraHolder;
import bagu_chan.bagus_lib.util.GlobalVec3;
import bagu_chan.bagus_lib.util.GlobalVec3ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class EntityCameraMessage implements CustomPacketPayload {
    public static final ResourceLocation ID = BagusLib.prefix("entity_camera");

    private final int entityId;

    private final int distance;
    private final int duration;
    private final float amount;
    private final GlobalVec3 globalPos;

    public EntityCameraMessage(int entityId, int distance, int duration, float amount, GlobalVec3 globalPos) {
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

    public EntityCameraMessage(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt(), buf.readInt(), buf.readFloat(), GlobalVec3ByteBuf.readGlobalPos(buf));
    }

    public static void handle(EntityCameraMessage message, PlayPayloadContext context) {
        context.workHandler().execute(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(message.entityId);
            CameraCore.addCameraHolderList(level, new EntityCameraHolder(message.distance, message.duration, message.amount, message.globalPos, entity));
        });
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }
}