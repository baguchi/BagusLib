package bagu_chan.bagus_lib.mixin;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.client.camera.CameraEvent;
import bagu_chan.bagus_lib.client.camera.CameraHolder;
import bagu_chan.bagus_lib.util.GlobalVec3;
import net.minecraft.world.entity.EntityType;
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

    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    public void handleEntityEvent(byte p_219360_, CallbackInfo callbackInfo) {
        if (p_219360_ == 4) {
            if (BagusConfigs.COMMON.enableCameraShakeForVanillaMobs.get()) {
                CameraEvent.addCameraHolderList(this.level, new CameraHolder(36, 80, GlobalVec3.of(this.level.dimension(), this.getEyePosition())));
            }
        }
    }
}
