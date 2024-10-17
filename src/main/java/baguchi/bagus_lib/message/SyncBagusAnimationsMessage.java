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

public class SyncBagusAnimationsMessage implements CustomPacketPayload, IPayloadHandler<SyncBagusAnimationsMessage> {

    public static final StreamCodec<FriendlyByteBuf, SyncBagusAnimationsMessage> STREAM_CODEC = CustomPacketPayload.codec(
            SyncBagusAnimationsMessage::write, SyncBagusAnimationsMessage::new
    );
    public static final CustomPacketPayload.Type<SyncBagusAnimationsMessage> TYPE = new CustomPacketPayload.Type<>(BagusLib.prefix("syc_anim"));

    private final int entityId;

    private final ResourceLocation resourceLocation;

    public SyncBagusAnimationsMessage(int entityId, ResourceLocation resourceLocation) {
        this.entityId = entityId;
        this.resourceLocation = resourceLocation;
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeResourceLocation(this.resourceLocation);
    }

    public SyncBagusAnimationsMessage(FriendlyByteBuf buf) {
        this(buf.readInt(), buf.readResourceLocation());
    }

    public void handle(SyncBagusAnimationsMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(message.entityId);
            AnimationUtil.handleAnimationPacket(entity, message.resourceLocation);
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}