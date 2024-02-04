package bagu_chan.bagus_lib.api;

import net.minecraft.nbt.CompoundTag;

public interface IBaguData {
    void setBagusData(CompoundTag compoundTag);

    CompoundTag getBagusData();
}
