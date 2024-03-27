package bagu_chan.bagus_lib.mixin.client;

import bagu_chan.bagus_lib.api.client.IRootModel;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(HierarchicalModel.class)
public abstract class HierarchicalModelMixin implements IRootModel {
    @Shadow
    public abstract ModelPart root();

    @Shadow
    @Final
    private static Vector3f ANIMATION_VECTOR_CACHE;

    @Override
    public ModelPart getBagusRoot() {
        return this.root();
    }

    @Override
    public Vector3f getCacheVec() {
        return ANIMATION_VECTOR_CACHE;
    }
}
