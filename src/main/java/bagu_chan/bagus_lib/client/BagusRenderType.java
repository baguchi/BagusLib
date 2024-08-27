package bagu_chan.bagus_lib.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

public class BagusRenderType extends RenderType {
    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ENTITY_ANIMATE_SHADER = new RenderStateShard.ShaderStateShard(
            BagusShaders::getRenderTypeAnimateEntityShader
    );
    public static final RenderStateShard.ShaderStateShard RENDERTYPE_ANIMATE_EYE_SHADER = new RenderStateShard.ShaderStateShard(
            BagusShaders::getRenderTypeAnimateEntityShader
    );

    public BagusRenderType(String p_173178_, VertexFormat p_173179_, VertexFormat.Mode p_173180_, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable p_173184_, Runnable p_173185_) {
        super(p_173178_, p_173179_, p_173180_, p_173181_, p_173182_, p_173183_, p_173184_, p_173185_);
    }

    /*
     *  Texture MUST LOCATE TO "<modid>:entity/hoge"! DON'T add png when USE THOSE RENDERTYPE!
     */
    public static RenderType animationEye(ResourceLocation location) {

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(location);
        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();
        float uDif = uMax - uMin;
        float vDif = vMax - vMin;
        return create(
                "bagus_lib:animation_eyes",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                1536, true, false,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ANIMATE_EYE_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(TextureAtlas.LOCATION_BLOCKS, false, false))
                        .setTexturingState(new OffsetScaleTexturingStateShard(sprite.getU(uDif), sprite.getV(vDif), 0, 0))
                        .setTransparencyState(ADDITIVE_TRANSPARENCY)
                        .setWriteMaskState(COLOR_WRITE)
                        .createCompositeState(false)
        );
    }

    /*
     *  Texture MUST LOCATE TO "<modid>:entity/hoge"! DON'T add png when USE THOSE RENDERTYPE!
     */
    public static RenderType entityAnimation(ResourceLocation location) {
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(location);
        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();
        float uDif = uMax - uMin;
        float vDif = vMax - vMin;
        return create(
                "bagus_lib:entity_animation",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                1536,
                true, false,
                RenderType.CompositeState.builder()
                        .setShaderState(RENDERTYPE_ENTITY_ANIMATE_SHADER)
                        .setTextureState(new RenderStateShard.TextureStateShard(sprite.atlasLocation(), false, false))
                        .setTexturingState(new OffsetScaleTexturingStateShard(sprite.getU(0F), sprite.getV(0F), uDif, vDif))
                        .setTransparencyState(NO_TRANSPARENCY)
                        .setCullState(NO_CULL)
                        .setLightmapState(LIGHTMAP)
                        .setOverlayState(OVERLAY)
                        .createCompositeState(false)
        );
    }

    @OnlyIn(Dist.CLIENT)
    public static final class OffsetScaleTexturingStateShard extends RenderStateShard.TexturingStateShard {
        public OffsetScaleTexturingStateShard(float x, float y, float uScale, float vScale) {
            super(
                    "offset_scale_texturing",
                    () -> RenderSystem.setTextureMatrix(new Matrix4f().translation(x, y, 0.0F).scale(uScale, vScale, 1.0F)),
                    () -> RenderSystem.resetTextureMatrix()
            );
        }
    }
}
