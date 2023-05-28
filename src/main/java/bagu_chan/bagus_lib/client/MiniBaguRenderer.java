package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.layer.CustomArmorLayer;
import bagu_chan.bagus_lib.entity.MiniBagu;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MiniBaguRenderer extends MobRenderer<MiniBagu, MiniBaguModel<MiniBagu>> {
    public static final ResourceLocation TEXTURE = new ResourceLocation(BagusLib.MODID, "textures/entity/mini_bagu.png");

    public MiniBaguRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new MiniBaguModel<>(p_174304_.bakeLayer(ModModelLayers.MINI_BAGU)), 0.3F);
        this.addLayer(new CustomArmorLayer<>(this, p_174304_));
    }

    @Override
    public ResourceLocation getTextureLocation(MiniBagu p_114482_) {
        return TEXTURE;
    }
}
