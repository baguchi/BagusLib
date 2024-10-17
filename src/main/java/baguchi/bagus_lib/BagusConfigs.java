package baguchi.bagus_lib;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BagusConfigs {
    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;
    public static final Client CLIENT;
    public static final ModConfigSpec CLIENT_SPEC;

    static {
        Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
        Pair<Client, ModConfigSpec> specPair2 = new ModConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair2.getRight();
        CLIENT = specPair2.getLeft();
    }

    public static class Common {
        public final ModConfigSpec.BooleanValue enableCameraShakeForVanillaMobs;
        public final ModConfigSpec.BooleanValue aprilFool;
        public Common(ModConfigSpec.Builder builder) {
            enableCameraShakeForVanillaMobs = builder
                    .comment("Enable the camera shake for vanilla mobs. [true / false]")
                    .translation(BagusLib.MODID + ".config.shakeCameraForVanilla")
                    .define("Enable Shake Camera for Vanilla", true);
            aprilFool = builder
                    .comment("Enable April Feature. [true / false]")
                    .translation(BagusLib.MODID + ".config.aprilFeature")
                    .define("Enable AprilFool Joke", true);

        }
    }

    public static class Client {
        public final ModConfigSpec.BooleanValue enableMiniBagu;
        public final ModConfigSpec.BooleanValue coolerEndPoem;

        public Client(ModConfigSpec.Builder builder) {
            enableMiniBagu = builder
                    .comment("Enable the Mini Bagu cosmetic. [true / false]")
                    .define("Enable Mini Bagu cosmetic(Only The Patreon CrystalFox Tier)", false);
            coolerEndPoem = builder
                    .comment("Enable Cooler EndPoem Feature...(Take a look the After beat the End) [true / false]")
                    .translation(BagusLib.MODID + ".config.betterEndPoem")
                    .define("Enable Cooler EndPoem", true);
        }
    }
}
