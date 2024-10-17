package baguchi.bagus_lib.message;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.dialog.DialogType;
import baguchi.bagus_lib.register.ModDialogs;
import baguchi.bagus_lib.util.DialogHandler;
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
    public static final CustomPacketPayload.Type<DialogMessage> TYPE = new CustomPacketPayload.Type<>(BagusLib.prefix("dialog"));

    private final String name;
    private final DialogType type;
    private final CompoundTag tag;

    public DialogMessage(String name, DialogType type, CompoundTag tag) {
        this.name = name;
        this.type = type;
        this.tag = tag;
    }

    public DialogMessage(FriendlyByteBuf buf) {
        this(buf.readUtf(), ModDialogs.getRegistry().getValue(buf.readResourceLocation()), buf.readNbt());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
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
            dialogType.readTag(message.tag.copy());
            DialogHandler.INSTANCE.addOrReplaceDialogType(this.name, dialogType.getClone());
        });
    }
}