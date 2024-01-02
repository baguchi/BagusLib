package bagu_chan.bagus_lib.util;

import bagu_chan.bagus_lib.api.IBaguData;
import bagu_chan.bagus_lib.message.PlayerDataSyncMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

public class MiscUtils {
    public static final String BAGUS_COSMETIC_ID = "BaguCosmetic";

    @OnlyIn(Dist.CLIENT)
    public static void updateCosmetic(String cosmeticId, boolean enable) {
        if (Minecraft.getInstance().player != null && Minecraft.getInstance().player instanceof IBaguData data && Minecraft.getInstance().level != null) {
            CompoundTag compoundTag = data.getData() != null ? data.getData() : new CompoundTag();

            enable = TierHelper.getTier(Minecraft.getInstance().player).getLevel() >= 1 && enable;

            compoundTag.putBoolean(cosmeticId, enable);
            data.setData(compoundTag);
            if (Minecraft.getInstance().getConnection() != null) {
                PacketDistributor.SERVER.noArg().send(new PlayerDataSyncMessage(compoundTag, (Minecraft.getInstance().player).getId()));
            }

        }
    }
}