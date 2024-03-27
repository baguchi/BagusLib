package bagu_chan.bagus_lib.mixin.client;

import bagu_chan.bagus_lib.api.client.IRootModel;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.QuadrupedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.Entity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(QuadrupedModel.class)
public abstract class QuadrupedModelMixin<T extends Entity> extends AgeableListModel<T> implements IRootModel {

    @Unique
    private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();
    @Unique
    public ModelPart quadRootPart;

    @Inject(at = @At(value = "TAIL"), method = "<init>")
    protected void init(ModelPart p_170857_, boolean p_170858_, float p_170859_, float p_170860_, float p_170861_, float p_170862_, int p_170863_, CallbackInfo ci) {
        quadRootPart = p_170857_;
    }

    @Override
    public ModelPart getBagusRoot() {
        return this.quadRootPart;
    }

    @Override
    public Vector3f getCacheVec() {
        return ANIMATION_VECTOR_CACHE;
    }
}
