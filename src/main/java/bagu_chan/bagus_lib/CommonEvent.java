package bagu_chan.bagus_lib;

import bagu_chan.bagus_lib.util.MiscUtils;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@Mod.EventBusSubscriber(modid = BagusLib.MODID)
public class CommonEvent {
    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getEntity().level().isClientSide()) {
            if (Minecraft.getInstance().player == event.getEntity()) {
                MiscUtils.updateCosmetic(MiscUtils.BAGUS_COSMETIC_ID, BagusConfigs.CLIENT.enableMiniBagu.get());
            }
        }
    }
}
