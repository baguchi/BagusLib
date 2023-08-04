package bagu_chan.bagus_lib.mixin;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.client.camera.CameraEvent;
import bagu_chan.bagus_lib.client.camera.EntityConditionCameraHolder;
import bagu_chan.bagus_lib.util.GlobalVec3;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Warden.class)
public abstract class WardenMixin extends Monster {

    protected WardenMixin(EntityType<? extends Warden> p_37839_, Level p_37840_) {
        super(p_37839_, p_37840_);
    }

    @Inject(method = "onSyncedDataUpdated", at = @At("HEAD"))
    public void onSyncedDataUpdated(EntityDataAccessor<?> p_219422_, CallbackInfo callbackInfo) {
        if (DATA_POSE.equals(p_219422_)) {
            if (this.level.isClientSide() && BagusConfigs.COMMON.enableCameraShakeForVanillaMobs.get()) {
                switch (this.getPose()) {
                    case EMERGING:
                        CameraEvent.addCameraHolderList(this.level, new EntityConditionCameraHolder<>(20, 200, 0.01F, GlobalVec3.of(this.level.dimension(), this.position()), this).setPredicate(wardenMixin -> wardenMixin.getPose() == Pose.EMERGING));
                        break;
                    case DIGGING:
                        CameraEvent.addCameraHolderList(this.level, new EntityConditionCameraHolder<>(20, 200, 0.01F, GlobalVec3.of(this.level.dimension(), this.position()), this).setPredicate(wardenMixin -> wardenMixin.getPose() == Pose.DIGGING));
                        break;
                }
            }
        }
    }
}
