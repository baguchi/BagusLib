package bagu_chan.bagus_lib;

import bagu_chan.bagus_lib.message.UpdateDataMessage;
import bagu_chan.bagus_lib.register.ModEntities;
import bagu_chan.bagus_lib.register.ModSensors;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BagusLib.MODID)
public class BagusLib {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "bagus_lib";
    public static final String NETWORK_PROTOCOL = "2";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "net"))
            .networkProtocolVersion(() -> NETWORK_PROTOCOL)
            .clientAcceptedVersions(NETWORK_PROTOCOL::equals)
            .serverAcceptedVersions(NETWORK_PROTOCOL::equals)
            .simpleChannel();

    public BagusLib() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        ModEntities.ENTITIES_REGISTRY.register(modEventBus);
        ModSensors.SENSOR_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BagusConfigs.COMMON_SPEC);

    }

    private void setupMessages() {
        CHANNEL.messageBuilder(UpdateDataMessage.class, 0)
                .encoder(UpdateDataMessage::writeToPacket).decoder(UpdateDataMessage::readFromPacket)
                .consumerMainThread(UpdateDataMessage::handle)
                .add();
    }


    private void commonSetup(final FMLCommonSetupEvent event) {
        this.setupMessages();
    }
}
