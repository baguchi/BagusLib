package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.dialog.DialogType;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class DialogMessage implements CustomPacketPayload, IPayloadHandler<DialogMessage> {

    public static final StreamCodec<FriendlyByteBuf, DialogMessage> STREAM_CODEC = CustomPacketPayload.codec(
            DialogMessage::write, DialogMessage::new
    );
    public static final CustomPacketPayload.Type<DialogMessage> TYPE = CustomPacketPayload.createType(BagusLib.prefix("dialog").toString());

    private final String string;

    public DialogMessage(String string) {
        this.string = string;
    }

    public DialogMessage(FriendlyByteBuf buf) {
        this(buf.readUtf());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.string);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(DialogMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            DialogType dialogType = new DialogType();
            dialogType.setDialogueBase(Component.literal(message.string));
            DialogHandler.INSTANCE.addOrReplaceDialogType("Command", dialogType);
        });
    }
}