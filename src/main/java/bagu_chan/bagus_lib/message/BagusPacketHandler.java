package bagu_chan.bagus_lib.message;

import bagu_chan.bagus_lib.BagusLib;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class BagusPacketHandler {


    public static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            BagusLib.prefix("channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void setupMessages() {
        CHANNEL.registerMessage(0, CameraMessage.class,
                CameraMessage::writeToPacket, CameraMessage::readFromPacket, CameraMessage::handle);
        CHANNEL.registerMessage(1, UpdateDataMessage.class,
                UpdateDataMessage::writeToPacket, UpdateDataMessage::readFromPacket, UpdateDataMessage::handle);
        CHANNEL.registerMessage(2, EntityCameraMessage.class,
                EntityCameraMessage::writeToPacket, EntityCameraMessage::readFromPacket, EntityCameraMessage::handle);
        CHANNEL.registerMessage(3, SyncEntityPacketToServer.class,
                SyncEntityPacketToServer::writeToPacket, SyncEntityPacketToServer::readFromPacket, SyncEntityPacketToServer::handle);

    }
}