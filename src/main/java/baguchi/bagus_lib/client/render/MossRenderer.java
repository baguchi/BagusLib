package baguchi.bagus_lib.client.render;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.ModModelLayers;
import baguchi.bagus_lib.entity.Moss;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class MossRenderer extends MobRenderer<Moss, LivingEntityRenderState, MossModel<LivingEntityRenderState>> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "textures/entity/moss.png");

    public MossRenderer(EntityRendererProvider.Context p_174304_) {
        super(p_174304_, new MossModel<>(p_174304_.bakeLayer(ModModelLayers.MOSS)), 0.5F);
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState p_368654_) {
        return TEXTURE;
    }
}
