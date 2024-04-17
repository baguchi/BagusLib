package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.client.dialog.DialogType;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DialogMessage {
    private final String string;

    public DialogMessage(String string) {
        this.string = string;
    }

    public static DialogMessage readFromPacket(FriendlyByteBuf buf) {
        return new DialogMessage(buf.readUtf());
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeUtf(this.string);
    }

    public static void handle(DialogMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DialogType dialogType = new DialogType();
            dialogType.setDialogueBase(Component.literal(message.string));
            DialogHandler.INSTANCE.addOrReplaceDialogType("Command", dialogType);
        });
    }
}