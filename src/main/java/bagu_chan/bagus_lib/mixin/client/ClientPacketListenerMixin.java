package bagu_chan.bagus_lib.mixin.client;

import bagu_chan.bagus_lib.api.IBaguPacket;
import bagu_chan.bagus_lib.message.BagusPacketHandler;
import bagu_chan.bagus_lib.message.SyncEntityPacketToServer;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(value = ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Inject(method = "handleAddEntity(Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;putNonPlayerEntity(ILnet/minecraft/world/entity/Entity;)V"),
            locals = LocalCapture.CAPTURE_FAILSOFT,
            require = 0
    )
    private void syncEntity(ClientboundAddEntityPacket p_104958_, CallbackInfo ci, EntityType entitytype, Entity entity, int i) {
        if (entity instanceof IBaguPacket && entity.level().isClientSide()) {
            BagusPacketHandler.CHANNEL.sendToServer(new SyncEntityPacketToServer(entity.getUUID()));
        }
    }
}