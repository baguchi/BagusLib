package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.util.client.ClientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class SyncBagusAnimationsMessage implements CustomPacketPayload, IPayloadHandler<SyncBagusAnimationsMessage> {

    public static final StreamCodec<FriendlyByteBuf, SyncBagusAnimationsMessage> STREAM_CODEC = CustomPacketPayload.codec(
            SyncBagusAnimationsMessage::write, SyncBagusAnimationsMessage::new
    );
    public static final CustomPacketPayload.Type<SyncBagusAnimationsMessage> TYPE = new CustomPacketPayload.Type<>(BagusLib.prefix("entity_camera"));

    private final int entityId;

    private final int index;

    public SyncBagusAnimationsMessage(int entityId, int index) {
        this.entityId = entityId;
        this.index = index;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeInt(this.index);
    }

    public SyncBagusAnimationsMessage(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readInt());
    }

    public void handle(SyncBagusAnimationsMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(message.entityId);
            ClientUtil.handleAnimationPacket(entity, message.index);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}