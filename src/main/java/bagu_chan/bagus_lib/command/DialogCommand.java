package bagu_chan.bagus_lib.command;

import bagu_chan.bagus_lib.message.DialogMessage;
import bagu_chan.bagus_lib.message.ImageDialogMessage;
import bagu_chan.bagus_lib.message.RemoveAllDialogMessage;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collection;

public class DialogCommand {
    public static void register(CommandDispatcher<CommandSourceStack> p_138061_, CommandBuildContext p_214450_) {
        LiteralCommandNode<CommandSourceStack> literalcommandnode = p_138061_.register(
                Commands.literal("bagus_dialog").requires(p_136627_ -> p_136627_.hasPermission(2)).then(Commands.literal("dialog")
                        .then(
                                Commands.argument("targets", EntityArgument.players()).then(Commands.argument("message", MessageArgument.message()).executes(p_248155_ -> {
                                            Collection<ServerPlayer> collection = EntityArgument.getPlayers(p_248155_, "targets");

                                            if (!collection.isEmpty()) {
                                                MessageArgument.resolveChatMessage(p_248155_, "message", p_248154_ -> sendDialogMessage(p_248155_.getSource(), collection, p_248154_));
                                            }

                                            return collection.size();
                                        })
                                )
                        )));
        /*LiteralCommandNode<CommandSourceStack> literalcommandnode2 = p_138061_.register(
                Commands.literal("bagus_dialog").requires(p_136627_ -> p_136627_.hasPermission(2)).then(Commands.literal("item_dialog").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("item", ItemArgument.item(p_214450_)).then(Commands.argument("message", MessageArgument.message()).executes(p_248155_ -> {
                    Collection<ServerPlayer> collection = EntityArgument.getPlayers(p_248155_, "targets");
                    ItemInput item = ItemArgument.getItem(p_248155_, "item");

                    if (!collection.isEmpty()) {
                        MessageArgument.resolveChatMessage(p_248155_, "message", p_248154_ -> sendDialogItemMessage(p_248155_.getSource(), collection, p_248154_, item));
                    }

                    return collection.size();
                }))))));*/
        LiteralCommandNode<CommandSourceStack> literalcommandnode3 = p_138061_.register(
                Commands.literal("bagus_dialog").requires(p_136627_ -> p_136627_.hasPermission(2)).then(Commands.literal("image_dialog").then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("image_path", ResourceLocationArgument.id()).then(Commands.argument("size_x", IntegerArgumentType.integer()).then(Commands.argument("size_y", IntegerArgumentType.integer()).then(Commands.argument("scale", FloatArgumentType.floatArg()).then(Commands.argument("message", MessageArgument.message()).executes(p_248155_ -> {
                    Collection<ServerPlayer> collection = EntityArgument.getPlayers(p_248155_, "targets");
                    ResourceLocation item = ResourceLocationArgument.getId(p_248155_, "image_path");

                    int sizeX = IntegerArgumentType.getInteger(p_248155_, "size_x");
                    int sizeY = IntegerArgumentType.getInteger(p_248155_, "size_y");

                    float scale = FloatArgumentType.getFloat(p_248155_, "scale");

                    if (!collection.isEmpty()) {
                        MessageArgument.resolveChatMessage(p_248155_, "message", p_248154_ -> sendDialogImageMessage(p_248155_.getSource(), collection, p_248154_, item, sizeX, sizeY, scale));
                    }

                    return collection.size();
                })))))))));
        LiteralCommandNode<CommandSourceStack> literalcommandnode4 = p_138061_.register(
                Commands.literal("bagus_dialog").requires(p_136627_ -> p_136627_.hasPermission(2)).then(Commands.literal("remove_all_dialog")
                        .then(
                                Commands.argument("targets", EntityArgument.players()).executes(p_248155_ -> {
                                    Collection<ServerPlayer> collection = EntityArgument.getPlayers(p_248155_, "targets");

                                    if (!collection.isEmpty()) {
                                        sendRemoveAllDialogMessage(p_248155_.getSource(), collection);
                                    }

                                    return collection.size();
                                })
                        )
                ));
    }

    private static void sendRemoveAllDialogMessage(CommandSourceStack p_250209_, Collection<ServerPlayer> p_252344_) {
        for (ServerPlayer serverplayer : p_252344_) {
            PacketDistributor.sendToPlayer(serverplayer, new RemoveAllDialogMessage());

        }

        p_250209_.sendSystemMessage(Component.translatable("command.bagus_lib.remove_all_dialog"));
    }

    private static void sendDialogMessage(CommandSourceStack p_250209_, Collection<ServerPlayer> p_252344_, PlayerChatMessage p_249416_) {
        OutgoingChatMessage outgoingchatmessage = OutgoingChatMessage.create(p_249416_);
        for (ServerPlayer serverplayer : p_252344_) {
            PacketDistributor.sendToPlayer(serverplayer, new DialogMessage(outgoingchatmessage.content().getString()));

        }
        p_250209_.sendSystemMessage(Component.translatable("command.bagus_lib.dialog"));
    }

    /*private static void sendDialogItemMessage(CommandSourceStack p_250209_, Collection<ServerPlayer> p_252344_, PlayerChatMessage p_249416_, ItemInput item) {
        OutgoingChatMessage outgoingchatmessage = OutgoingChatMessage.create(p_249416_);
        *//*for (ServerPlayer serverplayer : p_252344_) {
            try {
                //PacketDistributor.sendToPlayer(serverplayer,new ItemStackDialogMessage(outgoingchatmessage.content().getString(), item.createItemStack(1, false)));
            } catch (CommandSyntaxException e) {
                throw new RuntimeException(e);
            }

        }*//*

        p_250209_.sendSystemMessage(Component.translatable("command.bagus_lib.item_dialog_no_"));
    }*/

    private static void sendDialogImageMessage(CommandSourceStack p_250209_, Collection<ServerPlayer> p_252344_, PlayerChatMessage p_249416_, ResourceLocation resourceLocation, int sizeX, int sizeY, float scale) {
        OutgoingChatMessage outgoingchatmessage = OutgoingChatMessage.create(p_249416_);
        for (ServerPlayer serverplayer : p_252344_) {
            PacketDistributor.sendToPlayer(serverplayer, new ImageDialogMessage(outgoingchatmessage.content().getString(), resourceLocation, sizeX, sizeY, scale));

        }
        p_250209_.sendSystemMessage(Component.translatable("command.bagus_lib.image_dialog"));
    }
}
