package bagu_chan.bagus_lib.util;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.api.IData;
import bagu_chan.bagus_lib.message.BagusPacketHandler;
import bagu_chan.bagus_lib.message.UpdateDataMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MiscUtils {
    public static final String BAGUS_COSMETIC_ID = "BaguCosmetic";

    @OnlyIn(Dist.CLIENT)
    public static void updateCosmetic() {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player instanceof IData data) {
            CompoundTag compoundTag = new CompoundTag();
            if (TierHelper.getTier(Minecraft.getInstance().player).getLevel() >= 2) {
                compoundTag.putBoolean(BAGUS_COSMETIC_ID, BagusConfigs.CLIENT.enableMiniBagu.get());
                data.setData(compoundTag);
                BagusPacketHandler.CHANNEL.sendToServer(new UpdateDataMessage(compoundTag));
            }
        }
    }
}