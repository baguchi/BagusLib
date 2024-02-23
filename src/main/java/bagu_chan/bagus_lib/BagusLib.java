package bagu_chan.bagus_lib;

import bagu_chan.bagus_lib.message.CameraMessage;
import bagu_chan.bagus_lib.message.EntityCameraMessage;
import bagu_chan.bagus_lib.message.PlayerDataSyncMessage;
import bagu_chan.bagus_lib.message.SyncEntityPacketToServer;
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
    }
}
