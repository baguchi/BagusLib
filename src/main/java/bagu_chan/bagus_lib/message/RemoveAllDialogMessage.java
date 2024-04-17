package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class RemoveAllDialogMessage implements CustomPacketPayload {

    public static final ResourceLocation ID = BagusLib.prefix("remove_all_dialog");

    public RemoveAllDialogMessage() {
    }

    public RemoveAllDialogMessage(FriendlyByteBuf buf) {
        this();
    }

    public void write(FriendlyByteBuf buf) {
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(RemoveAllDialogMessage message, PlayPayloadContext context) {
        context.workHandler().execute(() -> {
            DialogHandler.INSTANCE.removeAllDialogType();
        });
    }
}