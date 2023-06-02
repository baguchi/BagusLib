package bagu_chan.bagus_lib;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BagusConfigs {
    public static final Common COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
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
}
