package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class BagusPacketHandler {


    public static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel CHANNEL = ChannelBuilder.named(
            BagusLib.prefix("channel")
    ).networkProtocolVersion(2).clientAcceptedVersions(Channel.VersionTest.exact(2)).serverAcceptedVersions(Channel.VersionTest.exact(2)).simpleChannel();

    public static void setupMessages() {
        CHANNEL.messageBuilder(CameraMessage.class, 0).decoder(CameraMessage::readFromPacket).encoder(CameraMessage::writeToPacket).consumerMainThread(CameraMessage::handle).add();
        CHANNEL.messageBuilder(UpdateDataMessage.class, 1).decoder(UpdateDataMessage::readFromPacket).encoder(UpdateDataMessage::writeToPacket).consumerNetworkThread(UpdateDataMessage::handle).add();
        CHANNEL.messageBuilder(EntityCameraMessage.class, 2).decoder(EntityCameraMessage::readFromPacket).encoder(EntityCameraMessage::writeToPacket).consumerMainThread(EntityCameraMessage::handle).add();
    }
}