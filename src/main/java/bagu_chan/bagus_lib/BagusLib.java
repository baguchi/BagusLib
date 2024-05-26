package bagu_chan.bagus_lib;

import bagu_chan.bagus_lib.command.DialogCommand;
import bagu_chan.bagus_lib.message.*;
import bagu_chan.bagus_lib.register.*;
import bagu_chan.bagus_lib.util.reward.TierHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(BagusLib.MODID)
public class BagusLib {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "bagus_lib";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public BagusLib(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading

        ModEntities.ENTITIES_REGISTRY.register(modEventBus);
        ModStructureProcessorTypes.PROCESSOR_TYPE.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModSensors.SENSOR_TYPES.register(modEventBus);
        ModDialogs.DIALOG.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupPackets);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        modContainer.registerConfig(ModConfig.Type.COMMON, BagusConfigs.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, BagusConfigs.CLIENT_SPEC);
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        TierHelper.addSuporterContents();
    }

    public void setupPackets(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID).versioned("1.0.0").optional();
        registrar.playBidirectional(CameraMessage.TYPE, CameraMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(EntityCameraMessage.TYPE, EntityCameraMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(PlayerDataSyncMessage.TYPE, PlayerDataSyncMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(SyncEntityPacketToServer.TYPE, SyncEntityPacketToServer.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(DialogMessage.TYPE, DialogMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
        registrar.playBidirectional(RemoveAllDialogMessage.TYPE, RemoveAllDialogMessage.STREAM_CODEC, (handler, payload) -> handler.handle(handler, payload));
    }

    private void registerCommands(RegisterCommandsEvent evt) {
        DialogCommand.register(evt.getDispatcher(), evt.getBuildContext());
    }
}
