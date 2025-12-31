package baguchi.bagus_lib.client.layer;

import baguchi.bagus_lib.api.IBagusExtraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.client.resources.model.MaterialSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.AtlasIds;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.neoforged.neoforge.client.ClientHooks;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


/*
 * https://github.com/AlexModGuy/AlexsMobs/blob/1.19.4/src/main/java/com/github/alexthe666/alexsmobs/client/render/layer/LayerKangarooArmor.java
 * Thanks Alex!
 */
public class CustomArmorLayer<S extends LivingEntityRenderState, M extends EntityModel<S> & IArmor, A extends EntityModel<S>> extends RenderLayer<S, M> {
    private final ArmorModelSet<PlayerModel> armorModelSet;
    private final RenderLayerParent<S, M> renderer;
    private final EquipmentAssetManager equipmentAssets;
    private final MaterialSet materials;
    private final Function<LayerTextureKey, Identifier> layerTextureLookup;
    private final Function<TrimSpriteKey, TextureAtlasSprite> trimSpriteLookup;


    public CustomArmorLayer(RenderLayerParent<S, M> render, EntityRendererProvider.Context context) {
        super(render);
        armorModelSet = ArmorModelSet.bake(
                ModelLayers.PLAYER_ARMOR,
                context.getModelSet(),
                p_446041_ -> new PlayerModel(p_446041_, false)
        );
        this.renderer = render;
        this.equipmentAssets = context.getEquipmentAssets();
        this.materials = context.getMaterials();
        this.layerTextureLookup = Util.memoize((p_386235_) -> p_386235_.layer.getTextureLocation(p_386235_.layerType));
        this.trimSpriteLookup = Util.memoize((p_399319_) -> Minecraft.getInstance().getAtlasManager().getAtlasOrThrow(AtlasIds.ARMOR_TRIMS).getSprite(p_399319_.spriteId()));

    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLightIn, S entity, float p_435802_, float p_434554_) {
        if (entity instanceof IBagusExtraRenderState bagusExtraRenderState) {
            this.renderHelmet(bagusExtraRenderState.getBagusLib$headItem(), poseStack, submitNodeCollector, packedLightIn);
            this.renderChestplate(bagusExtraRenderState.getBagusLib$chestItem(), poseStack, submitNodeCollector, packedLightIn);
            this.renderLeggings(bagusExtraRenderState.getBagusLib$legItem(), poseStack, submitNodeCollector, packedLightIn);
            this.renderBoots(bagusExtraRenderState.getBagusLib$feetItem(), poseStack, submitNodeCollector, packedLightIn);
        }
    }

    private void renderHelmet(ItemStack stack, PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn) {
        PlayerModel model = this.getArmorModel(EquipmentSlot.HEAD);
        modifiredArmorModelHook(model);
        getParentModel().headPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToHead(part, poseStack);
            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.HEAD, packedLightIn, model, "head");
            poseStack.popPose();
        });
    }

    private void renderBoots(ItemStack stack, PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn) {
        PlayerModel model = this.getArmorModel(EquipmentSlot.FEET);
        modifiredArmorModelHook(model);
        getParentModel().rightLegPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToLeg(part, poseStack);

            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.FEET, packedLightIn, model, "right_leg");
            poseStack.popPose();
        });

        getParentModel().leftLegPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToLeg(part, poseStack);

            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.FEET, packedLightIn, model, "left_leg");
            poseStack.popPose();
        });
    }


    private void renderLeggings(ItemStack stack, PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn) {
        PlayerModel model = this.getArmorModel(EquipmentSlot.LEGS);
        modifiredArmorModelHook(model);
        getParentModel().bodyPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToChest(part, poseStack);

            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.LEGS, packedLightIn, model, "body");
            poseStack.popPose();
        });

        getParentModel().rightLegPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToLeg(part, poseStack);

            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.LEGS, packedLightIn, model, "right_leg");
            poseStack.popPose();
        });

        getParentModel().leftLegPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToLeg(part, poseStack);

            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.LEGS, packedLightIn, model, "left_leg");
            poseStack.popPose();
        });
    }

    private void renderChestplate(ItemStack stack, PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn) {
        PlayerModel model = this.getArmorModel(EquipmentSlot.CHEST);

        modifiredArmorModelHook(model);

        getParentModel().bodyPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToChest(part, poseStack);
            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.CHEST, packedLightIn, model, "body");
            poseStack.popPose();
        });


        getParentModel().rightHandArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToChestPat(part, poseStack);
            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.CHEST, packedLightIn, model, "right_arm");
            poseStack.popPose();
        });

        getParentModel().leftHandArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToChestPat(part, poseStack);
            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.CHEST, packedLightIn, model, "left_arm");
            poseStack.popPose();
        });
    }

    private void renderArmorPiece(PoseStack p_117119_, SubmitNodeCollector p_433453_, ItemStack p_362532_, EquipmentSlot p_117122_, int light, PlayerModel armorModel, String renderPart) {
        Equippable equippable = p_362532_.get(DataComponents.EQUIPPABLE);
        if (equippable != null && shouldRender(equippable, p_117122_)) {

            AvatarRenderState avatarRenderState = new AvatarRenderState();

            EquipmentClientInfo.LayerType equipmentclientinfo$layertype = usesInnerModel(p_117122_) ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS : EquipmentClientInfo.LayerType.HUMANOID;
            this.renderLayers(equipmentclientinfo$layertype, equippable.assetId().orElseThrow(), armorModel, avatarRenderState, p_362532_, p_117119_, p_433453_, light, avatarRenderState.outlineColor, renderPart);
        }
    }

    public <S> void renderLayers(EquipmentClientInfo.LayerType p_388694_, ResourceKey<EquipmentAsset> p_386937_, Model<? super S> p_371498_, S p_435837_, ItemStack p_371902_, PoseStack p_371937_, SubmitNodeCollector p_434165_, int light, int outlineColor, String renderPart) {
        this.renderLayers(p_388694_, p_386937_, p_371498_, p_435837_, p_371902_, p_371937_, p_434165_, light, null, outlineColor, 1, renderPart);
    }

    public <S> void renderLayers(EquipmentClientInfo.LayerType p_387484_, ResourceKey<EquipmentAsset> p_387603_, Model<? super S> p_371731_, S p_435806_, ItemStack p_371670_, PoseStack p_371767_, SubmitNodeCollector p_435795_, int light, @Nullable Identifier p_371639_, int outlineColor, int p_436591_, String renderPart) {
        ModelPart part = p_371731_.root().createPartLookup().apply(renderPart);

        IClientItemExtensions extensions = IClientItemExtensions.of(p_371670_);
        p_371731_ = extensions.getGenericArmorModel(p_371670_, p_387484_, p_371731_);
        List<EquipmentClientInfo.Layer> list = this.equipmentAssets.get(p_387603_).getLayers(p_387484_);
        if (!list.isEmpty()) {
            int i = extensions.getDefaultDyeColor(p_371670_);
            boolean flag = p_371670_.hasFoil();
            int j = p_436591_;
            int idx = 0;

            for (EquipmentClientInfo.Layer equipmentclientinfo$layer : list) {
                int k = extensions.getArmorLayerTintColor(p_371670_, equipmentclientinfo$layer, idx, i);
                if (k != 0) {
                    Identifier resourcelocation = equipmentclientinfo$layer.usePlayerTexture() && p_371639_ != null ? p_371639_ : this.layerTextureLookup.apply(new LayerTextureKey(p_387484_, equipmentclientinfo$layer));
                    resourcelocation = ClientHooks.getArmorTexture(p_371670_, p_387484_, equipmentclientinfo$layer, resourcelocation);
                    p_435795_.order(j++).submitModelPart(part, p_371767_, RenderTypes.armorCutoutNoCull(resourcelocation), light, outlineColor, null, k, null);
                    if (flag) {
                        p_435795_.order(j++).submitModelPart(part, p_371767_, RenderTypes.armorEntityGlint(), light, outlineColor, null, k, null);
                    }

                    flag = false;
                }

                ++idx;
            }

            ArmorTrim armortrim = p_371670_.get(DataComponents.TRIM);
            if (armortrim != null) {
                TextureAtlasSprite textureatlassprite = this.trimSpriteLookup.apply(new TrimSpriteKey(armortrim, p_387484_, p_387603_));
                RenderType rendertype = Sheets.armorTrimsSheet(armortrim.pattern().value().decal());
                p_435795_.order(j++).submitModelPart(part, p_371767_, rendertype, light, outlineColor, textureatlassprite, -1, null);
            }
        }

    }

    public static int getColorForLayer(EquipmentClientInfo.Layer p_386482_, int p_371443_) {
        Optional<EquipmentClientInfo.Dyeable> optional = p_386482_.dyeable();
        if (optional.isPresent()) {
            int i = optional.get().colorWhenUndyed().map(ARGB::opaque).orElse(0);
            return p_371443_ != 0 ? p_371443_ : i;
        } else {
            return -1;
        }
    }

    public record LayerTextureKey(EquipmentClientInfo.LayerType layerType, EquipmentClientInfo.Layer layer) {
    }

    record TrimSpriteKey(ArmorTrim trim, EquipmentClientInfo.LayerType layerType,
                         ResourceKey<EquipmentAsset> equipmentAssetId) {
        public Identifier spriteId() {
            return this.trim.layerAssetId(this.layerType.trimAssetPrefix(), this.equipmentAssetId);
        }
    }

    private static boolean shouldRender(Equippable p_371295_, EquipmentSlot p_371795_) {
        return p_371295_.assetId().isPresent() && p_371295_.slot() == p_371795_;
    }


    private PlayerModel getArmorModel(EquipmentSlot p_117079_) {
        return this.armorModelSet.get(p_117079_);
    }

    private boolean usesInnerModel(EquipmentSlot p_117129_) {
        return p_117129_ == EquipmentSlot.LEGS;
    }


    protected void modifiredArmorModelHook(PlayerModel model) {
        resetModelPart(model.head);
        resetModelPart(model.body);
        resetModelPart(model.rightArm);
        resetModelPart(model.leftArm);
        resetModelPart(model.rightLeg);
        resetModelPart(model.leftLeg);
    }

    private void resetModelPart(ModelPart part) {
        part.x = 0;
        part.y = 0;
        part.z = 0;
        part.xRot = 0;
        part.yRot = 0;
        part.zRot = 0;
    }
}