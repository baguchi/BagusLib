package bagu_chan.bagus_lib;

import bagu_chan.bagus_lib.command.DialogCommand;
import bagu_chan.bagus_lib.message.*;
import bagu_chan.bagus_lib.register.ModEntities;
import bagu_chan.bagus_lib.register.ModLootModifiers;
import bagu_chan.bagus_lib.register.ModSensors;
import bagu_chan.bagus_lib.register.ModStructureProcessorTypes;
import bagu_chan.bagus_lib.util.reward.TierHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BagusLib.MODID)
public class BagusLib {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "bagus_lib";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public BagusLib(IEventBus modEventBus) {
        // Register the commonSetup method for modloading

        ModEntities.ENTITIES_REGISTRY.register(modEventBus);
        ModStructureProcessorTypes.PROCESSOR_TYPE.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModSensors.SENSOR_TYPES.register(modEventBus);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::setupPackets);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BagusConfigs.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BagusConfigs.CLIENT_SPEC);
    }

    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        TierHelper.addSuporterContents();
    }

    public void setupPackets(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(MODID).versioned("1.0.0").optional();
        registrar.play(CameraMessage.ID, CameraMessage::new, payload -> payload.client(CameraMessage::handle));
        registrar.play(EntityCameraMessage.ID, EntityCameraMessage::new, payload -> payload.client(EntityCameraMessage::handle));
        registrar.play(PlayerDataSyncMessage.ID, PlayerDataSyncMessage::new, payload -> payload.server(PlayerDataSyncMessage::handle));
        registrar.play(SyncEntityPacketToServer.ID, SyncEntityPacketToServer::new, payload -> payload.server(SyncEntityPacketToServer::handle));
        registrar.play(DialogMessage.ID, DialogMessage::new, payload -> payload.client(DialogMessage::handle));
        registrar.play(ImageDialogMessage.ID, ImageDialogMessage::new, payload -> payload.client(ImageDialogMessage::handle));
        registrar.play(ItemStackDialogMessage.ID, ItemStackDialogMessage::new, payload -> payload.client(ItemStackDialogMessage::handle));
        registrar.play(RemoveAllDialogMessage.ID, RemoveAllDialogMessage::new, payload -> payload.client(RemoveAllDialogMessage::handle));
    }

    private void registerCommands(RegisterCommandsEvent evt) {
        DialogCommand.register(evt.getDispatcher(), evt.getBuildContext());
    }
}
