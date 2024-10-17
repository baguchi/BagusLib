package bagu_chan.bagus_lib.client.layer;

import bagu_chan.bagus_lib.api.IBaguData;
import bagu_chan.bagus_lib.client.ModModelLayers;
import bagu_chan.bagus_lib.client.render.MiniBaguModel;
import bagu_chan.bagus_lib.client.render.MiniBaguRenderer;
import bagu_chan.bagus_lib.util.MiscUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;


/*
 * https://github.com/AlexModGuy/AlexsMobs/blob/1.19.4/src/main/java/com/github/alexthe666/alexsmobs/client/render/layer/LayerKangarooArmor.java
 * Thanks Alex!
 */
public class BagusLayer<T extends LivingEntityRenderState, M extends EntityModel<T> & IArmor> extends RenderLayer<T, M> {

    private final MiniBaguModel bagumodel;
    private RenderLayerParent<T, M> renderer;

    public BagusLayer(RenderLayerParent<T, M> render, EntityRendererProvider.Context context) {
        super(render);
        bagumodel = new MiniBaguModel(context.bakeLayer(ModModelLayers.MINI_BAGU));
        this.renderer = render;
    }

    public BagusLayer(RenderLayerParent<T, M> render, EntityModelSet modelSet, ModelManager modelManager) {
        super(render);
        bagumodel = new MiniBaguModel(modelSet.bakeLayer(ModModelLayers.MINI_BAGU));
        this.renderer = render;
    }


    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, T entity, float p_117353_, float p_117354_) {
        {
            if (entity instanceof IBaguData data) {
                CompoundTag tag = data.getBagusData();
                if (tag.contains(MiscUtils.BAGUS_COSMETIC_ID) && tag.getBoolean(MiscUtils.BAGUS_COSMETIC_ID)) {
                    poseStack.pushPose();

                    renderHelmet(entity, poseStack, bufferIn, packedLightIn, true, bagumodel, 1.0F, 1.0F, 1.0F, MiniBaguRenderer.TEXTURE);

                    poseStack.popPose();
                }
            }
        }
    }

    private void renderHelmet(T entity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, MiniBaguModel modelIn, float red, float green, float blue, ResourceLocation armorResource) {
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutoutNoCull(armorResource));
        renderer.getModel().headPartArmors().forEach(part -> {
            poseStack.pushPose();

            part.translateAndRotate(poseStack);
            poseStack.translate(0, -1.9F, 0);
            modelIn.renderToBuffer(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        });

    }
}