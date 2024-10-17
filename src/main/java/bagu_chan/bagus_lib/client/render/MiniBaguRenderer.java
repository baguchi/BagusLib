package bagu_chan.bagus_lib.client.render;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.ModModelLayers;
import bagu_chan.bagus_lib.client.layer.CustomArmorLayer;
import bagu_chan.bagus_lib.client.render.state.MiniBaguRenderState;
import bagu_chan.bagus_lib.entity.MiniBagu;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MiniBaguRenderer extends MobRenderer<MiniBagu, MiniBaguRenderState, MiniBaguModel<MiniBaguRenderState>> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "textures/entity/mini_bagu.png");

    public MiniBaguRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new MiniBaguModel(p_174304_.bakeLayer(ModModelLayers.MINI_BAGU)), 0.3F);
        this.addLayer(new CustomArmorLayer(this, p_174304_));
    }

    @Override
    public MiniBaguRenderState createRenderState() {
        return new MiniBaguRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(MiniBaguRenderState p_368654_) {
        return TEXTURE;
    }
}
