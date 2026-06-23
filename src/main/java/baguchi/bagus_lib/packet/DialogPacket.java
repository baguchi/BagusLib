package baguchi.bagus_lib.packet;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.dialog.DialogType;
import baguchi.bagus_lib.register.ModDialogs;
import baguchi.bagus_lib.util.DialogHandler;
import com.mojang.serialization.Codec;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.handling.IPayloadHandler;

public class DialogPacket<T extends DialogType> implements CustomPacketPayload, IPayloadHandler<DialogPacket> {

    public static final StreamCodec<FriendlyByteBuf, DialogPacket<?>> STREAM_CODEC = CustomPacketPayload.codec(
            DialogPacket::write, DialogPacket::new
    );
    public static final Type<DialogPacket<?>> TYPE = new Type<>(BagusLib.prefix("dialog"));

    private final String name;
    private final DialogType type;
    private final Identifier dataLocation;

    public DialogPacket(String name, DialogType type) {
        this.name = name;
        this.type = type;
        dataLocation = ModDialogs.getRegistry().getKey(type.codec());
    }

    public DialogPacket(String name, Identifier resourceLocation, FriendlyByteBuf type) {
        this.name = name;
        dataLocation = resourceLocation;
        this.type = type.readLenientJsonWithCodec(ModDialogs.getRegistry().get(dataLocation).get().value().codec());
    }

    public DialogPacket(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readIdentifier(), buf);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUtf(this.name);
        buf.writeIdentifier(this.dataLocation);
        buf.writeJsonWithCodec((Codec<DialogType>) this.type.codec().codec(), this.type);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public void handle(DialogPacket message, IPayloadContext context) {
        context.enqueueWork(() -> {
            DialogType dialogType = message.type;
            if (Minecraft.getInstance().level != null) {
                dialogType.setLastDialogRenderTime(dialogType.getNextDialogOption().dialogRenderTime() + Minecraft.getInstance().level.getGameTime());
            }
            DialogHandler.INSTANCE.addOrReplaceDialogType(this.name, dialogType);
        });
    }
}