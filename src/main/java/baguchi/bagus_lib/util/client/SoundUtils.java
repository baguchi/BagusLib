package baguchi.bagus_lib.util.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SoundUtils {

    public static void playClientSound(Holder<SoundEvent> soundEvent) {
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(soundEvent.value(), 1.0F, 1.0F));
    }
}
