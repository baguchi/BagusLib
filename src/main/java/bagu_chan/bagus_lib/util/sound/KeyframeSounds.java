package bagu_chan.bagus_lib.util.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class KeyframeSounds {
    public static void playSound(Entity entity, SoundDefinition p_232321_, long p_232322_) {
        float f = getElapsedSeconds(p_232321_, p_232322_);

        for (Map.Entry<String, List<SoundChannel>> entry : p_232321_.soundEvents().entrySet()) {
            List<SoundChannel> list = entry.getValue();

            list.forEach(p_288241_ -> {
                SoundKeyframe[] akeyframe = p_288241_.keyframes();
                int i2 = Math.max(0, Mth.binarySearch(0, akeyframe.length, p_232315_ -> f <= akeyframe[p_232315_].timestamp()) - 1);
                int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, p_232315_ -> f <= akeyframe[p_232315_].timestamp()) - 1);
                int j = Math.min(akeyframe.length - 1, i + 1);
                SoundKeyframe keyframe = akeyframe[i];

                boolean flag = keyframe.timestamp() > f;
                SoundKeyframe keyframe1 = akeyframe[flag ? 0 : j];
                float f3 = Mth.abs(keyframe1.timestamp() - f);

                if (f3 <= (1 / 20F) && f3 >= -(1 / 20F)) {
                    Minecraft.getInstance().level.playSeededSound(Minecraft.getInstance().player, entity.getX(), entity.getY(), entity.getZ(), akeyframe[i].sounds(), entity.getSoundSource(), akeyframe[i].volume(), akeyframe[i].pitch(), Minecraft.getInstance().level.random.nextLong());
                }
            });
        }
    }

    private static float getElapsedSeconds(SoundDefinition p_232317_, long p_232318_) {
        float f = (float) p_232318_ / 1000.0F;
        return p_232317_.looping() ? f % p_232317_.lengthInSeconds() : f;
    }

}
