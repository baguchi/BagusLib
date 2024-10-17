package baguchi.bagus_lib.mixin;

import baguchi.bagus_lib.BagusConfigs;
import baguchi.bagus_lib.client.camera.CameraCore;
import baguchi.bagus_lib.client.camera.holder.EntityCameraHolder;
import baguchi.bagus_lib.util.GlobalVec3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Ravager.class)
public abstract class RavagerMixin extends Raider {

    @Shadow
    private int roarTick;

    protected RavagerMixin(EntityType<? extends Ravager> p_37839_, Level p_37840_) {
        super(p_37839_, p_37840_);
    }

    @Inject(method = "roar", at = @At("HEAD"))
    private void roar(CallbackInfo callbackInfo) {
        Ravager ravager = (Ravager) ((Object) this);
        if (BagusConfigs.COMMON.enableCameraShakeForVanillaMobs.get()) {
            EntityCameraHolder<Ravager> entityConditionCameraHolder = new EntityCameraHolder<>(18, 40, 0.2F, GlobalVec3.of(this.level().dimension(), this.getEyePosition()), ravager);
            CameraCore.addCameraHolderList(this.level(), entityConditionCameraHolder);
        }
    }
}
