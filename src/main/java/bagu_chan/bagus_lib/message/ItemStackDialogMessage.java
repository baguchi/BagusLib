package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.client.dialog.ItemDialogType;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ItemStackDialogMessage {
    private final String string;
    private final ItemStack itemStack;


    public ItemStackDialogMessage(String string, ItemStack itemStack) {
        this.string = string;
        this.itemStack = itemStack;
    }

    public static ItemStackDialogMessage readFromPacket(FriendlyByteBuf buf) {
        return new ItemStackDialogMessage(buf.readUtf(), buf.readItem());
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeUtf(this.string);
        buf.writeItem(this.itemStack);
    }

    public static void handle(ItemStackDialogMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ItemDialogType dialogType = new ItemDialogType();
            dialogType.setItemStack(message.itemStack);
            dialogType.setScale(4, 4);
            dialogType.setDialogueBase(Component.literal(message.string));
            DialogHandler.INSTANCE.addOrReplaceDialogType("Command", dialogType);
        });
    }
}