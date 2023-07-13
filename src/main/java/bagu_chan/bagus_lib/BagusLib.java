package bagu_chan.bagus_lib;

import bagu_chan.bagus_lib.message.BagusPacketHandler;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Locale;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BagusLib.MODID)
public class BagusLib {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "bagus_lib";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public BagusLib() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading

        ModEntities.ENTITIES_REGISTRY.register(modEventBus);
        ModSensors.SENSOR_TYPES.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BagusConfigs.COMMON_SPEC);

    }


    public static ResourceLocation prefix(String name) {
        return new ResourceLocation(MODID, name.toLowerCase(Locale.ROOT));
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        BagusPacketHandler.setupMessages();
    }
}
