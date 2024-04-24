package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.dialog.ImageDialogType;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class ImageDialogMessage implements CustomPacketPayload, IPayloadHandler<ImageDialogMessage> {

    public static final StreamCodec<FriendlyByteBuf, ImageDialogMessage> STREAM_CODEC = CustomPacketPayload.codec(
            ImageDialogMessage::write, ImageDialogMessage::new
    );
    public static final CustomPacketPayload.Type<ImageDialogMessage> TYPE = CustomPacketPayload.createType(BagusLib.prefix("image_dialog").toString());

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

    public ImageDialogMessage(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readResourceLocation(), buf.readInt(), buf.readInt(), buf.readFloat());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.string);
        buf.writeResourceLocation(this.imagePath);
        buf.writeInt(this.sizeX);
        buf.writeInt(this.sizeY);
        buf.writeFloat(this.scale);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(ImageDialogMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            ImageDialogType dialogType = new ImageDialogType();
            dialogType.setSize(message.sizeX, message.sizeY);
            dialogType.setScale(message.scale, message.scale);
            dialogType.setResourceLocation(message.imagePath);
            dialogType.setDialogueBase(Component.literal(message.string));
            DialogHandler.INSTANCE.addOrReplaceDialogType("Command", dialogType);
        });
    }
}