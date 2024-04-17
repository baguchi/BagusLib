package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.dialog.DialogType;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class DialogMessage implements CustomPacketPayload {

    public static final ResourceLocation ID = BagusLib.prefix("dialog");

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
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(DialogMessage message, PlayPayloadContext context) {
        context.workHandler().execute(() -> {
            DialogType dialogType = new DialogType();
            dialogType.setDialogueBase(Component.literal(message.string));
            DialogHandler.INSTANCE.addOrReplaceDialogType("Command", dialogType);
        });
    }
}