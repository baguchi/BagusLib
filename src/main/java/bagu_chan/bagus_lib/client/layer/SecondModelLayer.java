package bagu_chan.bagus_lib.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;

public class SecondModelLayer<T extends Mob, M extends EntityModel<T>>
        extends RenderLayer<T, M> {
    private final ResourceLocation location;
    private final M layerModel;

    public SecondModelLayer(RenderLayerParent<T, M> renderLayerParent, ResourceLocation location, M model) {
        super(renderLayerParent);
        this.location = location;
        this.layerModel = model;
    }

    @Override
    public void render(
            PoseStack p_116924_,
            MultiBufferSource p_116925_,
            int p_116926_,
            T p_116927_,
            float p_116928_,
            float p_116929_,
            float p_116930_,
            float p_116931_,
            float p_116932_,
            float p_116933_
    ) {
        coloredCutoutModelCopyLayerRender(
                this.getParentModel(),
                this.layerModel,
                this.location,
                p_116924_,
                p_116925_,
                p_116926_,
                p_116927_,
                p_116928_,
                p_116929_,
                p_116931_,
                p_116932_,
                p_116933_,
                p_116930_,
                -1
        );
    }
}

