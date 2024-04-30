package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.dialog.DialogType;
import bagu_chan.bagus_lib.register.ModDialogs;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class DialogMessage implements CustomPacketPayload, IPayloadHandler<DialogMessage> {

    public static final StreamCodec<FriendlyByteBuf, DialogMessage> STREAM_CODEC = CustomPacketPayload.codec(
            DialogMessage::write, DialogMessage::new
    );
    public static final CustomPacketPayload.Type<DialogMessage> TYPE = CustomPacketPayload.createType(BagusLib.prefix("dialog").toString());

    private final DialogType type;
    private final CompoundTag tag;

    public DialogMessage(DialogType type, CompoundTag tag) {
        this.type = type;
        this.tag = tag;
    }

    public DialogMessage(FriendlyByteBuf buf) {
        this(ModDialogs.getRegistry().get(buf.readResourceLocation()), buf.readNbt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(ModDialogs.getRegistry().getKey(this.type).toString());
        buf.writeNbt(this.tag);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(DialogMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            DialogType dialogType = message.type;
            dialogType.readTag(message.tag);
            DialogHandler.INSTANCE.addOrReplaceDialogType("Command", dialogType);
        });
    }
}