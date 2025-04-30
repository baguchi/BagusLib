package bagu_chan.bagus_lib.register;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.dialog.DialogType;
import bagu_chan.bagus_lib.client.dialog.ImageDialogType;
import bagu_chan.bagus_lib.client.dialog.ItemDialogType;
import bagu_chan.bagus_lib.client.dialog.WinDialogType;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

import static net.minecraft.resources.ResourceKey.createRegistryKey;

@EventBusSubscriber(modid = BagusLib.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModDialogs {
    public static final ResourceKey<Registry<MapCodec<? extends DialogType>>> DIALOG_REGISTRY = createRegistryKey(ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "dialog_type"));

    public static final DeferredRegister<MapCodec<? extends DialogType>> DIALOG_TYPE = DeferredRegister.create(DIALOG_REGISTRY, BagusLib.MODID);
    public static final DeferredHolder<MapCodec<? extends DialogType>, MapCodec<? extends DialogType>> DIALOGS = DIALOG_TYPE.register("dialog", () -> DialogType.CODEC);
    public static final DeferredHolder<MapCodec<? extends DialogType>, MapCodec<? extends DialogType>> IMAGE_DIALOG = DIALOG_TYPE.register("image_dialog", () -> ImageDialogType.CODEC);
    public static final DeferredHolder<MapCodec<? extends DialogType>, MapCodec<? extends DialogType>> ITEM_DIALOG = DIALOG_TYPE.register("item_dialog", () -> ItemDialogType.CODEC);
    public static final DeferredHolder<MapCodec<? extends DialogType>, MapCodec<? extends DialogType>> WIN_DIALOG = DIALOG_TYPE.register("win_dialog", () -> WinDialogType.CODEC);


    private static Registry<MapCodec<? extends DialogType>> registry;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        registry = event.create(new RegistryBuilder<>(DIALOG_REGISTRY).sync(true));
    }

    public static Registry<MapCodec<? extends DialogType>> getRegistry() {
        if (registry == null) {
            throw new IllegalStateException("Registry not yet initialized");
        }
        return registry;
    }
}