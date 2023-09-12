package bagu_chan.bagus_lib.mixin;

import bagu_chan.bagus_lib.api.IBaguData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements IBaguData {
    private static final EntityDataAccessor<CompoundTag> DATA = SynchedEntityData.defineId(LivingEntity.class, EntityDataSerializers.COMPOUND_TAG);

    public LivingEntityMixin(EntityType<?> p_19870_, Level p_19871_) {
        super(p_19870_, p_19871_);
    }

    @Inject(at = @At("TAIL"), method = "defineSynchedData")
    private void registerData(CallbackInfo ci) {
        entityData.define(DATA, new CompoundTag());
    }


    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    protected void addAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        CompoundTag data = this.getData();
        if (data != null) {
            compoundTag.put("BaguData", data);
        }
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    protected void readAdditionalSaveData(CompoundTag compoundTag, CallbackInfo ci) {
        if (compoundTag.contains("BaguData")) {
            this.setData(compoundTag.getCompound("BaguData"));
        }
    }

    @Override
    public void setData(CompoundTag compoundTag) {
        this.entityData.set(DATA, compoundTag);
    }

    @Override
    public CompoundTag getData() {
        return this.entityData.get(DATA);
    }
}
