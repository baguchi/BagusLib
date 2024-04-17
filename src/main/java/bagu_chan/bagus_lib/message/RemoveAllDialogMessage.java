package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RemoveAllDialogMessage {
    public RemoveAllDialogMessage() {
    }

    public static RemoveAllDialogMessage readFromPacket(FriendlyByteBuf buf) {
        return new RemoveAllDialogMessage();
    }

    public void writeToPacket(FriendlyByteBuf buf) {
    }


    public static void handle(RemoveAllDialogMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DialogHandler.INSTANCE.removeAllDialogType();
        });
    }
}