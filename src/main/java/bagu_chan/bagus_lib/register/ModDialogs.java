package bagu_chan.bagus_lib.register;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.dialog.DialogType;
import bagu_chan.bagus_lib.client.dialog.ImageDialogType;
import bagu_chan.bagus_lib.client.dialog.ItemDialogType;
import bagu_chan.bagus_lib.client.dialog.WinDialogType;
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
    public static final ResourceKey<Registry<DialogType>> DIALOG_REGISTRY = createRegistryKey(ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "dialog"));

    public static final DeferredRegister<DialogType> DIALOG = DeferredRegister.create(DIALOG_REGISTRY, BagusLib.MODID);
    public static final DeferredHolder<DialogType, DialogType> DIALOGS = DIALOG.register("dialog", DialogType::new);
    public static final DeferredHolder<DialogType, DialogType> IMAGE_DIALOG = DIALOG.register("image_dialog", ImageDialogType::new);
    public static final DeferredHolder<DialogType, DialogType> ITEM_DIALOG = DIALOG.register("item_dialog", ItemDialogType::new);
    public static final DeferredHolder<DialogType, DialogType> WIN_DIALOG = DIALOG.register("win_dialog", WinDialogType::new);


    private static Registry<DialogType> registry;

    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        registry = event.create(new RegistryBuilder<>(DIALOG_REGISTRY).sync(true));
    }

    public static Registry<DialogType> getRegistry() {
        if (registry == null) {
            throw new IllegalStateException("Registry not yet initialized");
        }
        return registry;
    }
}