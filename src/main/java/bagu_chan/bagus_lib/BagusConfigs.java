package bagu_chan.bagus_lib;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BagusConfigs {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Client CLIENT;
    public static final ForgeConfigSpec CLIENT_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
        Pair<Client, ForgeConfigSpec> specPair2 = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair2.getRight();
        CLIENT = specPair2.getLeft();
    }

    public static class Common {
        public final ForgeConfigSpec.BooleanValue enableCameraShakeForVanillaMobs;

        public Common(ForgeConfigSpec.Builder builder) {
            enableCameraShakeForVanillaMobs = builder
                    .comment("Enable the camera shake for vanilla mobs. [true / false]")
                    .translation(BagusLib.MODID + ".config.shakeCameraForVanilla")
                    .define("Enable Shake Camera for Vanilla", true);
        }
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue enableMiniBagu;
        public final ForgeConfigSpec.BooleanValue coolerEndPoem;

        public Client(ForgeConfigSpec.Builder builder) {
            enableMiniBagu = builder
                    .comment("Enable the Mini Bagu cosmetic. [true / false]")
                    .define("Enable Mini Bagu cosmetic", false);
            coolerEndPoem = builder
                    .comment("Enable Cooler EndPoem Feature...(Take a look the After beat the End) [true / false]")
                    .define("Enable Cooler EndPoem", true);
        }
    }
}
