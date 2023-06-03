package bagu_chan.bagus_lib.util;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.api.IData;
import bagu_chan.bagus_lib.message.UpdateDataMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MiscUtils {
    @OnlyIn(Dist.CLIENT)
    public static void updateCosmetic() {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player instanceof IData data) {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putBoolean("Bagu", BagusConfigs.CLIENT.enableMiniBagu.get());
            data.setData(compoundTag);
            BagusLib.CHANNEL.sendToServer(new UpdateDataMessage(compoundTag));

        }
    }
}
