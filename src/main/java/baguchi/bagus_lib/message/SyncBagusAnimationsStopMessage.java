package baguchi.bagus_lib.message;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.util.client.AnimationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class SyncBagusAnimationsStopMessage implements CustomPacketPayload, IPayloadHandler<SyncBagusAnimationsStopMessage> {

    public static final StreamCodec<FriendlyByteBuf, SyncBagusAnimationsStopMessage> STREAM_CODEC = CustomPacketPayload.codec(
            SyncBagusAnimationsStopMessage::write, SyncBagusAnimationsStopMessage::new
    );
    public static final Type<SyncBagusAnimationsStopMessage> TYPE = new Type<>(BagusLib.prefix("syc_anim_stop"));

    private final int entityId;

    private final ResourceLocation resourceLocation;

    public SyncBagusAnimationsStopMessage(int entityId, ResourceLocation resourceLocation) {
        this.entityId = entityId;
        this.resourceLocation = resourceLocation;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeResourceLocation(this.resourceLocation);
    }

    public SyncBagusAnimationsStopMessage(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readResourceLocation());
    }

    public void handle(SyncBagusAnimationsStopMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(message.entityId);
            AnimationUtil.handleStopAnimationPacket(entity, message.resourceLocation);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}