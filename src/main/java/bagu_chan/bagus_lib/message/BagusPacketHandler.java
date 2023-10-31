package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;

public class BagusPacketHandler {


    public static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(
            BagusLib.prefix("channel")
    ).networkProtocolVersion(() -> "2").clientAcceptedVersions("2"::equals).serverAcceptedVersions("2"::equals).simpleChannel();

    public static void setupMessages() {
        CHANNEL.messageBuilder(CameraMessage.class, 0).decoder(CameraMessage::readFromPacket).encoder(CameraMessage::writeToPacket).consumerMainThread(CameraMessage::handle).add();
        CHANNEL.messageBuilder(UpdateDataMessage.class, 1).decoder(UpdateDataMessage::readFromPacket).encoder(UpdateDataMessage::writeToPacket).consumerNetworkThread(UpdateDataMessage::handle).add();
        CHANNEL.messageBuilder(EntityCameraMessage.class, 2).decoder(EntityCameraMessage::readFromPacket).encoder(EntityCameraMessage::writeToPacket).consumerMainThread(EntityCameraMessage::handle).add();
    }
}