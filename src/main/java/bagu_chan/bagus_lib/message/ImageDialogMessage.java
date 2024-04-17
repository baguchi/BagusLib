package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.client.dialog.ImageDialogType;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ImageDialogMessage {
    private final String string;
    private final ResourceLocation imagePath;
    private final int sizeX;
    private final int sizeY;
    private final float scale;


    public ImageDialogMessage(String string, ResourceLocation imagePath, int sizeX, int sizeY, float scale) {
        this.string = string;
        this.imagePath = imagePath;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.scale = scale;
    }

    public static ImageDialogMessage readFromPacket(FriendlyByteBuf buf) {
        return new ImageDialogMessage(buf.readUtf(), buf.readResourceLocation(), buf.readInt(), buf.readInt(), buf.readFloat());
    }

    public void writeToPacket(FriendlyByteBuf buf) {
        buf.writeUtf(this.string);
        buf.writeResourceLocation(this.imagePath);
        buf.writeInt(this.sizeX);
        buf.writeInt(this.sizeY);
        buf.writeFloat(this.scale);
    }

    public static void handle(ImageDialogMessage message, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ImageDialogType dialogType = new ImageDialogType();
            dialogType.setSize(message.sizeX, message.sizeY);
            dialogType.setScale(message.scale, message.scale);
            dialogType.setResourceLocation(message.imagePath);
            dialogType.setDialogueBase(Component.literal(message.string));
            DialogHandler.INSTANCE.addOrReplaceDialogType("Command", dialogType);
        });
    }
}