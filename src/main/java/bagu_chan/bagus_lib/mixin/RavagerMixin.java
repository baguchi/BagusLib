package bagu_chan.bagus_lib.mixin;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.client.camera.CameraEvent;
import bagu_chan.bagus_lib.client.camera.EntityConditionCameraHolder;
import bagu_chan.bagus_lib.util.GlobalVec3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ravager.class)
public abstract class RavagerMixin extends Raider {

    @Shadow
    private int roarTick;

    protected RavagerMixin(EntityType<? extends Raider> p_37839_, Level p_37840_) {
        super(p_37839_, p_37840_);
    }

    @Inject(method = "roar", at = @At("HEAD"))
    private void roar(CallbackInfo callbackInfo) {
        if (!this.level.isClientSide() && BagusConfigs.COMMON.enableCameraShakeForVanillaMobs.get()) {
            CameraEvent.addCameraHolderList(this.level, new EntityConditionCameraHolder<>(18, 20, GlobalVec3.of(this.level.dimension(), this.getEyePosition()), this).setPredicate(predicate -> this.roarTick > 1));
        }
    }
}
