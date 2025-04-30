package baguchi.bagus_lib.message;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.dialog.DialogType;
import baguchi.bagus_lib.register.ModDialogs;
import baguchi.bagus_lib.util.DialogHandler;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class DialogMessage<T extends DialogType> implements CustomPacketPayload, IPayloadHandler<DialogMessage> {

    public static final StreamCodec<FriendlyByteBuf, DialogMessage<?>> STREAM_CODEC = CustomPacketPayload.codec(
            DialogMessage::write, DialogMessage::new
    );
    public static final Type<DialogMessage<?>> TYPE = new Type<>(BagusLib.prefix("dialog"));

    private final String name;
    private final DialogType type;
    private final ResourceLocation dataLocation;

    public DialogMessage(String name, DialogType type) {
        this.name = name;
        this.type = type;
        dataLocation = ModDialogs.getRegistry().getKey(type.codec());
    }

    public DialogMessage(String name, ResourceLocation resourceLocation, FriendlyByteBuf type) {
        this.name = name;
        dataLocation = resourceLocation;
        this.type = type.readJsonWithCodec(ModDialogs.getRegistry().get(dataLocation).get().value().codec());
    }

    public DialogMessage(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readResourceLocation(), buf);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
        buf.writeResourceLocation(this.dataLocation);
        buf.writeJsonWithCodec((Codec<DialogType>) this.type.codec().codec(), this.type);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(DialogMessage message, IPayloadContext context) {
        context.enqueueWork(() -> {
            DialogType dialogType = message.type;
            if (Minecraft.getInstance().level != null) {
                dialogType.setLastDialogRenderTime(dialogType.getDialogRenderTime() + Minecraft.getInstance().level.getGameTime());
            }
            DialogHandler.INSTANCE.addOrReplaceDialogType(this.name, dialogType);
        });
    }
}