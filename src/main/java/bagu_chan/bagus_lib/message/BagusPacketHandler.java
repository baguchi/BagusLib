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
        CHANNEL.registerMessage(4, DialogMessage.class,
                DialogMessage::writeToPacket, DialogMessage::readFromPacket, DialogMessage::handle);
        CHANNEL.registerMessage(5, ImageDialogMessage.class,
                ImageDialogMessage::writeToPacket, ImageDialogMessage::readFromPacket, ImageDialogMessage::handle);
        CHANNEL.registerMessage(6, ItemStackDialogMessage.class,
                ItemStackDialogMessage::writeToPacket, ItemStackDialogMessage::readFromPacket, ItemStackDialogMessage::handle);
        CHANNEL.registerMessage(7, RemoveAllDialogMessage.class,
                RemoveAllDialogMessage::writeToPacket, RemoveAllDialogMessage::readFromPacket, RemoveAllDialogMessage::handle);

    }
}