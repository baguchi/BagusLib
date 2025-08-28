package baguchi.bagus_lib;

import baguchi.bagus_lib.client.dialog.DialogType;
import baguchi.bagus_lib.command.DialogCommand;
import baguchi.bagus_lib.item.ModItems;
import baguchi.bagus_lib.message.*;
import baguchi.bagus_lib.register.*;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BagusLib.MODID)
public class BagusLib {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "bagus_lib";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public BagusLib(IEventBus modEventBus, Dist dist, ModContainer modContainer) {
        // Register the commonSetup method for modloading

        if (dist.isClient()) {
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        }

        ModItems.ITEM_REGISTRY.register(modEventBus);
        ModEntities.ENTITIES_REGISTRY.register(modEventBus);
        ModStructureProcessorTypes.PROCESSOR_TYPE.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModSensors.SENSOR_TYPES.register(modEventBus);
        ModDialogs.DIALOG_TYPE.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupPackets);
        modEventBus.addListener(this::dataSetup);

        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        modContainer.registerConfig(ModConfig.Type.COMMON, BagusConfigs.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, BagusConfigs.CLIENT_SPEC);
    }

    private void dataSetup(final DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(DialogRegister.REGISTRY_KEY, DialogType.DIRECT_CODEC, DialogType.DIRECT_CODEC);
    }


    public static ResourceLocation prefix(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name.toLowerCase(Locale.ROOT));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    public void setupPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).versioned("1.0.0").optional();
        registrar.playToClient(CameraMessage.TYPE, CameraMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playToClient(EntityCameraMessage.TYPE, EntityCameraMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playToServer(SyncEntityPacketToServer.TYPE, SyncEntityPacketToServer.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playToClient(DialogMessage.TYPE, DialogMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playToClient(RemoveAllDialogMessage.TYPE, RemoveAllDialogMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playToClient(SyncBagusAnimationsMessage.TYPE, SyncBagusAnimationsMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playToClient(SyncBagusAnimationsStopMessage.TYPE, SyncBagusAnimationsStopMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playToClient(SyncBagusAnimationsStopAllMessage.TYPE, SyncBagusAnimationsStopAllMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
    }

    private void registerCommands(RegisterCommandsEvent evt) {
        DialogCommand.register(evt.getDispatcher(), evt.getBuildContext());
    }
}
