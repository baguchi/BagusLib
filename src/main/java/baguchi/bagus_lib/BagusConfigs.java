package baguchi.bagus_lib;

import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BagusConfigs {
    public static final Common COMMON;
    public static final ModConfigSpec COMMON_SPEC;
    static {
        Pair<Common, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON_SPEC = specPair.getRight();
        COMMON = specPair.getLeft();
    }

    public static class Common {
        public final ModConfigSpec.BooleanValue enableCameraShakeForVanillaMobs;
        public final ModConfigSpec.BooleanValue aprilFool;
        public final ModConfigSpec.BooleanValue alwayAplilFool;
        public final ModConfigSpec.BooleanValue playableFirstPerson;
        public Common(ModConfigSpec.Builder builder) {
            enableCameraShakeForVanillaMobs = builder
                    .comment("Enable the camera shake for vanilla mobs. [true / false]")
                    .translation(BagusLib.MODID + ".config.shakeCameraForVanilla")
                    .define("Enable Shake Camera for Vanilla", true);
            aprilFool = builder
                    .comment("Enable April Feature. [true / false]")
                    .translation(BagusLib.MODID + ".config.aprilFeature")
                    .define("Enable AprilFool Joke", true);
            alwayAplilFool = builder
                    .comment("Enable April Feature Alway. [true / false]")
                    .translation(BagusLib.MODID + ".config.aprilFeatureAlway")
                    .define("Enable AprilFool Joke Alway", false);
            playableFirstPerson = builder
                    .comment("Enable First Person Animation(When you using model change mod. should be turn off). [true / false]")
                    .translation(BagusLib.MODID + ".config.playableFirstPerson")
                    .define("Enable First Person Animation", true);

        }
    }
}
