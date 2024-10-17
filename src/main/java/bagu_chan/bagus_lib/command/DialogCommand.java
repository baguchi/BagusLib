package bagu_chan.bagus_lib.command;

import bagu_chan.bagus_lib.client.dialog.DialogType;
import bagu_chan.bagus_lib.message.DialogMessage;
import bagu_chan.bagus_lib.message.RemoveAllDialogMessage;
import bagu_chan.bagus_lib.register.ModDialogs;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.CompoundTagArgument;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Collection;
import java.util.Optional;

public class DialogCommand {

    private static final DynamicCommandExceptionType ERROR_INVALID_FEATURE = new DynamicCommandExceptionType(
            p_212392_ -> Component.translatable("commands.place.feature.invalid", p_212392_)
    );

    public static void register(CommandDispatcher<CommandSourceStack> p_138061_, CommandBuildContext p_214450_) {
        LiteralCommandNode<CommandSourceStack> literalcommandnode = p_138061_.register(
                Commands.literal("bagus_lib").requires(p_136627_ -> p_136627_.hasPermission(2)).then(Commands.literal("dialog")
                        .then(
                                Commands.argument("targets", EntityArgument.players()).then(Commands.argument("dialog_type", ResourceKeyArgument.key(ModDialogs.DIALOG_REGISTRY)).then(Commands.argument("tag", CompoundTagArgument.compoundTag()).executes(p_248155_ -> {
                                            ;
                                            Collection<ServerPlayer> collection = EntityArgument.getPlayers(p_248155_, "targets");

                                            if (!collection.isEmpty()) {
                                                sendDialogMessage(p_248155_.getSource(), collection, getDialogType(p_248155_, "dialog_type"), CompoundTagArgument.getCompoundTag(p_248155_, "tag"));
                                            }

                                            return collection.size();
                                        })
                                )
                                ))));
        LiteralCommandNode<CommandSourceStack> literalcommandnode4 = p_138061_.register(
                Commands.literal("bagus_lib").requires(p_136627_ -> p_136627_.hasPermission(2)).then(Commands.literal("remove_all_dialog")
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

    public static Holder.Reference<DialogType> getDialogType(CommandContext<CommandSourceStack> p_249310_, String p_250729_) throws CommandSyntaxException {
        return resolveKey(p_249310_, p_250729_, ModDialogs.DIALOG_REGISTRY, ERROR_INVALID_FEATURE);
    }

    private static <T> Registry<T> getRegistry(CommandContext<CommandSourceStack> p_212379_, ResourceKey<? extends Registry<T>> p_212380_) {
        return p_212379_.getSource().getServer().registryAccess().lookupOrThrow(p_212380_);
    }

    private static <T> ResourceKey<T> getRegistryKey(
            CommandContext<CommandSourceStack> p_212374_, String p_212375_, ResourceKey<Registry<T>> p_212376_, DynamicCommandExceptionType p_212377_
    ) throws CommandSyntaxException {
        ResourceKey<?> resourcekey = p_212374_.getArgument(p_212375_, ResourceKey.class);
        Optional<ResourceKey<T>> optional = resourcekey.cast(p_212376_);
        return optional.orElseThrow(() -> p_212377_.create(resourcekey));
    }

    private static <T> Holder.Reference<T> resolveKey(
            CommandContext<CommandSourceStack> p_248662_, String p_252172_, ResourceKey<Registry<T>> p_249701_, DynamicCommandExceptionType p_249790_
    ) throws CommandSyntaxException {
        ResourceKey<T> resourcekey = getRegistryKey(p_248662_, p_252172_, p_249701_, p_249790_);
        return getRegistry(p_248662_, p_249701_).get(resourcekey).orElseThrow(() -> p_249790_.create(resourcekey.location()));
    }

    private static void sendRemoveAllDialogMessage(CommandSourceStack p_250209_, Collection<ServerPlayer> p_252344_) {
        for (ServerPlayer serverplayer : p_252344_) {
            PacketDistributor.sendToPlayer(serverplayer, new RemoveAllDialogMessage());

        }

        p_250209_.sendSystemMessage(Component.translatable("command.bagus_lib.remove_all_dialog"));
    }

    private static void sendDialogMessage(CommandSourceStack p_250209_, Collection<ServerPlayer> p_252344_, Holder.Reference<DialogType> p_249416_, CompoundTag p_139379_) {
        for (ServerPlayer serverplayer : p_252344_) {
            PacketDistributor.sendToPlayer(serverplayer, new DialogMessage("Command", p_249416_.value(), p_139379_));

        }
        p_250209_.sendSystemMessage(Component.translatable("command.bagus_lib.dialog"));
    }
}
