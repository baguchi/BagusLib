package baguchi.bagus_lib.packet;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.util.client.BagusAnimationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class SyncBagusAnimationsStopAllPacket implements CustomPacketPayload, IPayloadHandler<SyncBagusAnimationsStopAllPacket> {

    public static final StreamCodec<FriendlyByteBuf, SyncBagusAnimationsStopAllPacket> STREAM_CODEC = CustomPacketPayload.codec(
            SyncBagusAnimationsStopAllPacket::write, SyncBagusAnimationsStopAllPacket::new
    );
    public static final Type<SyncBagusAnimationsStopAllPacket> TYPE = new Type<>(BagusLib.prefix("syc_anim_stop_all"));

    private final int entityId;

    public SyncBagusAnimationsStopAllPacket(int entityId) {
        this.entityId = entityId;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
    }

    public SyncBagusAnimationsStopAllPacket(FriendlyByteBuf buf) {
        this(buf.readInt());
    }

    public void handle(SyncBagusAnimationsStopAllPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(message.entityId);
            BagusAnimationUtil.handleStopAllAnimationClient(entity);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}