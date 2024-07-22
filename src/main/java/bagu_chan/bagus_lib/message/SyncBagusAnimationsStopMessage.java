package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.util.client.AnimationUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncBagusAnimationsStopMessage {
    private final int entityId;

    private final ResourceLocation name;

    public SyncBagusAnimationsStopMessage(int entityId, ResourceLocation name) {
        this.entityId = entityId;
        this.name = name;
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeResourceLocation(this.name);
    }

    public static SyncBagusAnimationsStopMessage readFromPacket(FriendlyByteBuf buf) {
        return new SyncBagusAnimationsStopMessage(buf.readInt(), buf.readResourceLocation());
    }

    public static void handle(SyncBagusAnimationsStopMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            Level level = Minecraft.getInstance().player.level();
            if (level == null) {
                return;
            }
            Entity entity = level.getEntity(message.entityId);
            AnimationUtil.handleStopAnimationPacket(entity, message.name);
        });
    }
}