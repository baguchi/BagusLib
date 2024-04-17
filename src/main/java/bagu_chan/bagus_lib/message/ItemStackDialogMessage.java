package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.dialog.ItemDialogType;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ItemStackDialogMessage implements CustomPacketPayload {

    public static final ResourceLocation ID = BagusLib.prefix("item_stack_dialog");

    private final String string;
    private final ItemStack itemStack;


    public ItemStackDialogMessage(String string, ItemStack itemStack) {
        this.string = string;
        this.itemStack = itemStack;
    }

    public ItemStackDialogMessage(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readItem());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.string);
        buf.writeItem(this.itemStack);
    }

    @Override
    public ResourceLocation id() {
        return ID;
    }

    public static void handle(ItemStackDialogMessage message, PlayPayloadContext context) {
        context.workHandler().execute(() -> {
            ItemDialogType dialogType = new ItemDialogType();
            dialogType.setItemStack(message.itemStack);
            dialogType.setScale(4, 4);
            dialogType.setDialogueBase(Component.literal(message.string));
            DialogHandler.INSTANCE.addOrReplaceDialogType("Command", dialogType);
        });
    }
}