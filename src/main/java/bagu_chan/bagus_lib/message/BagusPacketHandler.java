package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class BagusPacketHandler {


    public static final String NETWORK_PROTOCOL = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(BagusLib.MODID, "channel"))
            .networkProtocolVersion(() -> NETWORK_PROTOCOL)
            .clientAcceptedVersions(NETWORK_PROTOCOL::equals)
            .serverAcceptedVersions(NETWORK_PROTOCOL::equals)
            .simpleChannel();

    public static void setupMessages() {
        CHANNEL.messageBuilder(CameraMessage.class, 0)
                .encoder(CameraMessage::writeToPacket).decoder(CameraMessage::readFromPacket)
                .consumerMainThread(CameraMessage::handle)
                .add();
        CHANNEL.messageBuilder(UpdateDataMessage.class, 1)
                .encoder(UpdateDataMessage::writeToPacket).decoder(UpdateDataMessage::readFromPacket)
                .consumerMainThread(UpdateDataMessage::handle)
                .add();
        CHANNEL.messageBuilder(EntityCameraMessage.class, 2)
                .encoder(EntityCameraMessage::writeToPacket).decoder(EntityCameraMessage::readFromPacket)
                .consumerMainThread(EntityCameraMessage::handle)
                .add();
        CHANNEL.messageBuilder(SyncEntityPacketToServer.class, 3)
                .encoder(SyncEntityPacketToServer::writeToPacket).decoder(SyncEntityPacketToServer::readFromPacket)
                .consumerMainThread(SyncEntityPacketToServer::handle)
                .add();
        CHANNEL.messageBuilder(DialogMessage.class, 4)
                .encoder(DialogMessage::writeToPacket).decoder(DialogMessage::readFromPacket)
                .consumerMainThread(DialogMessage::handle)
                .add();
        CHANNEL.messageBuilder(ImageDialogMessage.class, 5)
                .encoder(ImageDialogMessage::writeToPacket).decoder(ImageDialogMessage::readFromPacket)
                .consumerMainThread(ImageDialogMessage::handle)
                .add();
        CHANNEL.messageBuilder(ItemStackDialogMessage.class, 6)
                .encoder(ItemStackDialogMessage::writeToPacket).decoder(ItemStackDialogMessage::readFromPacket)
                .consumerMainThread(ItemStackDialogMessage::handle)
                .add();
        CHANNEL.messageBuilder(RemoveAllDialogMessage.class, 7)
                .encoder(RemoveAllDialogMessage::writeToPacket).decoder(RemoveAllDialogMessage::readFromPacket)
                .consumerMainThread(RemoveAllDialogMessage::handle)
                .add();
        CHANNEL.messageBuilder(SyncBagusAnimationsMessage.class, 8)
                .encoder(SyncBagusAnimationsMessage::writeToPacket).decoder(SyncBagusAnimationsMessage::readFromPacket)
                .consumerMainThread(SyncBagusAnimationsMessage::handle)
                .add();
        CHANNEL.messageBuilder(SyncBagusAnimationsStopMessage.class, 9)
                .encoder(SyncBagusAnimationsStopMessage::writeToPacket).decoder(SyncBagusAnimationsStopMessage::readFromPacket)
                .consumerMainThread(SyncBagusAnimationsStopMessage::handle)
                .add();
        CHANNEL.messageBuilder(SyncBagusAnimationsStopAllMessage.class, 10)
                .encoder(SyncBagusAnimationsStopAllMessage::writeToPacket).decoder(SyncBagusAnimationsStopAllMessage::readFromPacket)
                .consumerMainThread(SyncBagusAnimationsStopAllMessage::handle)
                .add();
    }
}