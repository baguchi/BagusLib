package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class RemoveAllDialogMessage implements CustomPacketPayload, IPayloadHandler<RemoveAllDialogMessage> {

    public static final StreamCodec<FriendlyByteBuf, RemoveAllDialogMessage> STREAM_CODEC = CustomPacketPayload.codec(
            RemoveAllDialogMessage::write, RemoveAllDialogMessage::new
    );
    public static final CustomPacketPayload.Type<RemoveAllDialogMessage> TYPE = CustomPacketPayload.createType(BagusLib.prefix("remove_all_dialog").toString());



    public RemoveAllDialogMessage() {
    }

    public RemoveAllDialogMessage(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(RemoveAllDialogMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            DialogHandler.INSTANCE.removeAllDialogType();
        });
    }
}