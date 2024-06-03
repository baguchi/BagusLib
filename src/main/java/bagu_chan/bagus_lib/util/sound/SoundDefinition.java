package bagu_chan.bagus_lib.util.sound;

import com.google.common.collect.Maps;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@OnlyIn(Dist.CLIENT)
public record SoundDefinition(float lengthInSeconds, boolean looping, Map<String, List<SoundChannel>> soundEvents) {
    @OnlyIn(Dist.CLIENT)
    public static class Builder {
        private final float length;
        private final Map<String, List<SoundChannel>> soundByRoot = Maps.newHashMap();
        private boolean looping;

        public static SoundDefinition.Builder withLength(float p_232276_) {
            return new SoundDefinition.Builder(p_232276_);
        }

        private Builder(float p_232273_) {
            this.length = p_232273_;
        }

        public SoundDefinition.Builder looping() {
            this.looping = true;
            return this;
        }

        public SoundDefinition.Builder addSound(String p_232280_, SoundChannel p_232281_) {
            this.soundByRoot.computeIfAbsent(p_232280_, p_329694_ -> new ArrayList<>()).add(p_232281_);
            return this;
        }

        public SoundDefinition build() {
            return new SoundDefinition(this.length, this.looping, this.soundByRoot);
        }
    }
}
