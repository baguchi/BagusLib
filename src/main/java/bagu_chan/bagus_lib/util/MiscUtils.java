package bagu_chan.bagus_lib.util;

import bagu_chan.bagus_lib.api.IBaguData;
import bagu_chan.bagus_lib.message.BagusPacketHandler;
import bagu_chan.bagus_lib.message.UpdateDataMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class MiscUtils {
    public static final String BAGUS_COSMETIC_ID = "BaguCosmetic";

    @OnlyIn(Dist.CLIENT)
    public static void updateCosmetic(String cosmeticId, boolean enable) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player instanceof IBaguData data && Minecraft.getInstance().level != null) {
            CompoundTag compoundTag = data.getData() != null ? data.getData() : new CompoundTag();
            if (TierHelper.getTier(Minecraft.getInstance().player).getLevel() >= 1) {
                compoundTag.putBoolean(cosmeticId, enable);
                data.setData(compoundTag);
                if (Minecraft.getInstance().getConnection() != null) {
                    BagusPacketHandler.CHANNEL.send(new UpdateDataMessage(compoundTag, (Minecraft.getInstance().player).getId()), Minecraft.getInstance().getConnection().getConnection());
                }
            }
        }
    }
}