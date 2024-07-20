package bagu_chan.bagus_lib.mixin;

import bagu_chan.bagus_lib.animation.BaguAnimationController;
import bagu_chan.bagus_lib.api.IBaguAnimate;
import bagu_chan.bagus_lib.event.RegisterBagusAnimationEvents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin implements IBaguAnimate {
    @Unique
    public final BaguAnimationController BAGU_ANIMATION_CONTROLLER = new BaguAnimationController(((Entity) (Object) this));

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(EntityType p_19870_, Level p_19871_, CallbackInfo ci) {
        RegisterBagusAnimationEvents events = NeoForge.EVENT_BUS.post(new RegisterBagusAnimationEvents(((Entity) (Object) this)));
        for (ResourceLocation resourceLocations : events.getAnimationStateMap().keySet()) {
            BAGU_ANIMATION_CONTROLLER.addAnimation(resourceLocations);
        }
    }

    @Override
    public BaguAnimationController getBaguController() {
        return BAGU_ANIMATION_CONTROLLER;
    }
}
