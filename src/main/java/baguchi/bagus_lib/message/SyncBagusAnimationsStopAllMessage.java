package baguchi.bagus_lib.message;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.util.client.AnimationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class SyncBagusAnimationsStopAllMessage implements CustomPacketPayload, IPayloadHandler<SyncBagusAnimationsStopAllMessage> {

    public static final StreamCodec<FriendlyByteBuf, SyncBagusAnimationsStopAllMessage> STREAM_CODEC = CustomPacketPayload.codec(
            SyncBagusAnimationsStopAllMessage::write, SyncBagusAnimationsStopAllMessage::new
    );
    public static final Type<SyncBagusAnimationsStopAllMessage> TYPE = new Type<>(BagusLib.prefix("syc_anim_stop_all"));

    private final int entityId;

    public SyncBagusAnimationsStopAllMessage(int entityId) {
        this.entityId = entityId;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    public SyncBagusAnimationsStopAllMessage(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void handle(SyncBagusAnimationsStopAllMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(message.entityId);
            AnimationUtil.handleStopAllAnimationPacket(entity);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}