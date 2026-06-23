package baguchi.bagus_lib.packet;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class RemoveAllDialogPacket implements CustomPacketPayload, IPayloadHandler<RemoveAllDialogPacket> {

    public static final StreamCodec<FriendlyByteBuf, RemoveAllDialogPacket> STREAM_CODEC = CustomPacketPayload.codec(
            RemoveAllDialogPacket::write, RemoveAllDialogPacket::new
    );
    public static final CustomPacketPayload.Type<RemoveAllDialogPacket> TYPE = new CustomPacketPayload.Type<>(BagusLib.prefix("remove_all_dialog"));


    public RemoveAllDialogPacket() {
    }

    public RemoveAllDialogPacket(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(RemoveAllDialogPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            DialogHandler.INSTANCE.removeAllDialogType();
        });
    }
}