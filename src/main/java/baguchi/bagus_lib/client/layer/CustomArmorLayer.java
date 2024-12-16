package baguchi.bagus_lib.client.layer;

import baguchi.bagus_lib.api.IBagusExtraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.Util;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.item.equipment.trim.ArmorTrim;
import net.minecraft.world.item.equipment.trim.TrimMaterial;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.Optional;
import java.util.function.Function;


/*
 * https://github.com/AlexModGuy/AlexsMobs/blob/1.19.4/src/main/java/com/github/alexthe666/alexsmobs/client/render/layer/LayerKangarooArmor.java
 * Thanks Alex!
 */
public class CustomArmorLayer<S extends LivingEntityRenderState, M extends EntityModel<S> & IArmor, A extends EntityModel<S>> extends RenderLayer<S, M> {
    private final HumanoidModel defaultBipedModel;
    private final HumanoidModel innerModel;
    private final RenderLayerParent<S, M> renderer;
    private final TextureAtlas armorTrimAtlas;
    private final EquipmentAssetManager equipmentModelSet;
    private final Function<TrimSpriteKey, TextureAtlasSprite> trimSpriteLookup;


    public CustomArmorLayer(RenderLayerParent<S, M> render, EntityRendererProvider.Context context) {
        super(render);
        defaultBipedModel = new HumanoidModel(context.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR));
        this.innerModel = new HumanoidModel(context.bakeLayer(ModelLayers.ARMOR_STAND_INNER_ARMOR));
        this.renderer = render;
        this.armorTrimAtlas = context.getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET);
        this.equipmentModelSet = context.getEquipmentAssets();
        this.trimSpriteLookup = Util.memoize(p_386234_ -> context.getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET).getSprite(p_386234_.textureId()));

    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int light, S entity, float p_117353_, float p_117354_) {
        if (entity instanceof IBagusExtraRenderState bagusExtraRenderState) {
            poseStack.pushPose();
            ItemStack headItem = bagusExtraRenderState.getBagusLib$headItem();
            EquipmentClientInfo.LayerType equipmentmodel$layerType = usesInnerModel(EquipmentSlot.HEAD)
                    ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS
                    : EquipmentClientInfo.LayerType.HUMANOID;
            Model a = getArmorModelHook(headItem, equipmentmodel$layerType, this.defaultBipedModel);
            boolean flag1 = headItem.hasFoil();
            int clampedLight = light;
            if (headItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(headItem);
                int i = extensions.getDefaultDyeColor(headItem);
                renderHelmet(headItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
            } else {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(headItem);
                int i = extensions.getDefaultDyeColor(headItem);

                renderHelmet(headItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
            }
                /* else {
                getParentModel().headPartArmors().forEach(part -> {
                    this.getParentModel().translateToHead(part, poseStack);
                    poseStack.translate(0, -0.25, 0.0F);
                    poseStack.mulPose((new Quaternionf()).rotateX((float) Math.PI));
                    poseStack.mulPose((new Quaternionf()).rotateY((float) Math.PI));
                    poseStack.scale(0.625F, 0.625F, 0.625F);
                    Minecraft.getInstance().getItemRenderer().renderStatic(headItem, ItemDisplayContext.HEAD, light, OverlayTexture.NO_OVERLAY, poseStack, bufferIn, entity.level(), 0);
                });
            }*/
            poseStack.popPose();

            poseStack.pushPose();
            ItemStack chestItem = bagusExtraRenderState.getBagusLib$chestItem();
            equipmentmodel$layerType = usesInnerModel(EquipmentSlot.CHEST)
                    ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS
                    : EquipmentClientInfo.LayerType.HUMANOID;
            a = getArmorModelHook(chestItem, equipmentmodel$layerType, this.defaultBipedModel);

            flag1 = chestItem.hasFoil();
            clampedLight = light;
            if (chestItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(chestItem);
                int i = extensions.getDefaultDyeColor(chestItem);
                float f = (float) (i >> 16 & 255) / 255.0F;
                float f1 = (float) (i >> 8 & 255) / 255.0F;
                float f2 = (float) (i & 255) / 255.0F;
                renderChestplate(chestItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
            } else {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(chestItem);
                int i = extensions.getDefaultDyeColor(chestItem);

                renderChestplate(chestItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
            }
            poseStack.popPose();

            poseStack.pushPose();
            ItemStack legItem = bagusExtraRenderState.getBagusLib$legItem();
            equipmentmodel$layerType = usesInnerModel(EquipmentSlot.LEGS)
                    ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS
                    : EquipmentClientInfo.LayerType.HUMANOID;
            a = getArmorModelHook(legItem, equipmentmodel$layerType, this.innerModel);
            flag1 = legItem.hasFoil();
            clampedLight = light;
            if (legItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(legItem);
                int i = extensions.getDefaultDyeColor(legItem);
                float f = (float) (i >> 16 & 255) / 255.0F;
                float f1 = (float) (i >> 8 & 255) / 255.0F;
                float f2 = (float) (i & 255) / 255.0F;
                renderLeg(legItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
            } else {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(legItem);
                int i = extensions.getDefaultDyeColor(legItem);

                renderLeg(legItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
            }
            poseStack.popPose();

            poseStack.pushPose();
            ItemStack feetItem = bagusExtraRenderState.getBagusLib$feetItem();
            equipmentmodel$layerType = usesInnerModel(EquipmentSlot.FEET)
                    ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS
                    : EquipmentClientInfo.LayerType.HUMANOID;
            a = getArmorModelHook(feetItem, equipmentmodel$layerType, this.defaultBipedModel);
            boolean notAVanillaModel = a != defaultBipedModel;

            flag1 = feetItem.hasFoil();
            clampedLight = light;
            if (feetItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(feetItem);
                int i = extensions.getDefaultDyeColor(feetItem);
                float f = (float) (i >> 16 & 255) / 255.0F;
                float f1 = (float) (i >> 8 & 255) / 255.0F;
                float f2 = (float) (i & 255) / 255.0F;
                renderBoot(feetItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
            } else {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(feetItem);
                int i = extensions.getDefaultDyeColor(feetItem);
                renderBoot(feetItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
            }
            poseStack.popPose();
        }

    }

    private void renderTrim(ItemStack itemStack, PoseStack poseStack, MultiBufferSource bufferIn, EquipmentClientInfo.LayerType layerType, ResourceKey<EquipmentAsset> resourceLocation, ModelPart a, int i) {
        ArmorTrim armortrim = itemStack.get(DataComponents.TRIM);
        if (armortrim != null) {
            TextureAtlasSprite textureatlassprite = trimSpriteLookup.apply(new TrimSpriteKey(armortrim, layerType, resourceLocation));
            VertexConsumer vertexconsumer1 = textureatlassprite.wrap(bufferIn.getBuffer(Sheets.armorTrimsSheet(armortrim.pattern().value().decal())));
            a.render(poseStack, vertexconsumer1, i, OverlayTexture.NO_OVERLAY);
        }
    }

    private static boolean usesInnerModel(EquipmentSlot p_117129_) {
        return p_117129_ == EquipmentSlot.LEGS;
    }

    private void renderLeg(ItemStack legItem, S entity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, Model modelIn, int color) {
        Equippable equippable = legItem.get(DataComponents.EQUIPPABLE);
        if (equippable != null && !equippable.assetId().isEmpty()) {
            int idx = 0;
            for (EquipmentClientInfo.Layer layer : this.equipmentModelSet.get(equippable.assetId().get()).getLayers(EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS)) {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(legItem);

                int j = extensions.getArmorLayerTintColor(legItem, layer, idx, color);
                if (j != 0) {
                    ResourceLocation resourcelocation = net.neoforged.neoforge.client.ClientHooks.getArmorTexture(legItem, EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, layer, layer.getTextureLocation(EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS));
                    getParentModel().rightLegPartArmors().forEach(part -> {
                                poseStack.pushPose();
                        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(resourcelocation), false, glintIn);

                        getParentModel().translateToLeg(part, poseStack);
                        Optional<ModelPart> optional = modelIn.getAnyDescendantWithName("right_leg");
                        if (optional.isPresent()) {
                            optional.get().render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);
                            renderTrim(legItem, poseStack, bufferIn, EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, equippable.assetId().orElseThrow(), optional.get(), j);
                        }
                                poseStack.popPose();
                            }
                    );
                    getParentModel().leftLegPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(resourcelocation), false, glintIn);

                        getParentModel().translateToLeg(part, poseStack);

                        Optional<ModelPart> optional = modelIn.getAnyDescendantWithName("left_leg");
                        if (optional.isPresent()) {
                            optional.get().render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);
                            renderTrim(legItem, poseStack, bufferIn, EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, equippable.assetId().orElseThrow(), optional.get(), j);
                        }
                        poseStack.popPose();
                    });
                    getParentModel().bodyPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(resourcelocation), false, glintIn);

                        this.getParentModel().translateToChest(part, poseStack);
                        Optional<ModelPart> optional = modelIn.getAnyDescendantWithName("body");
                        if (optional.isPresent()) {
                            optional.get().render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);
                            renderTrim(legItem, poseStack, bufferIn, EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS, equippable.assetId().orElseThrow(), optional.get(), j);
                        }
                        poseStack.popPose();
                    });
                }
                idx++;
            }
        }
    }

    private void renderBoot(ItemStack feetItem, S entity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, Model modelIn, int color) {
        Equippable equippable = feetItem.get(DataComponents.EQUIPPABLE);
        if (equippable != null && !equippable.assetId().isEmpty()) {
            int idx = 0;
            for (EquipmentClientInfo.Layer layer : this.equipmentModelSet.get(equippable.assetId().get()).getLayers(EquipmentClientInfo.LayerType.HUMANOID)) {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(feetItem);

                int j = extensions.getArmorLayerTintColor(feetItem, layer, idx, color);
                if (j != 0) {
                    ResourceLocation resourcelocation = net.neoforged.neoforge.client.ClientHooks.getArmorTexture(feetItem, EquipmentClientInfo.LayerType.HUMANOID, layer, layer.getTextureLocation(EquipmentClientInfo.LayerType.HUMANOID));

                    getParentModel().rightLegPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(resourcelocation), false, glintIn);

                        getParentModel().translateToLeg(part, poseStack);
                        Optional<ModelPart> optional = modelIn.getAnyDescendantWithName("right_leg");
                        if (optional.isPresent()) {
                            optional.get().render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);
                            renderTrim(feetItem, poseStack, bufferIn, EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(), optional.get(), j);
                        }
                        poseStack.popPose();
                    });
                    getParentModel().leftLegPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(resourcelocation), false, glintIn);

                        getParentModel().translateToLeg(part, poseStack);
                        Optional<ModelPart> optional = modelIn.getAnyDescendantWithName("left_leg");
                        if (optional.isPresent()) {
                            optional.get().render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);
                            renderTrim(feetItem, poseStack, bufferIn, EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(), optional.get(), j);
                        }
                        poseStack.popPose();
                    });
                }
                idx++;
            }
        }
    }


    private void renderChestplate(ItemStack chestItem, S entity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, Model modelIn, int color) {
        Equippable equippable = chestItem.get(DataComponents.EQUIPPABLE);

        if (equippable != null && !equippable.assetId().isEmpty()) {
            int idx = 0;
            for (EquipmentClientInfo.Layer layer : this.equipmentModelSet.get(equippable.assetId().get()).getLayers(EquipmentClientInfo.LayerType.HUMANOID)) {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(chestItem);

                int j = extensions.getArmorLayerTintColor(chestItem, layer, idx, color);
                if (j != 0) {
                    ResourceLocation resourcelocation = net.neoforged.neoforge.client.ClientHooks.getArmorTexture(chestItem, EquipmentClientInfo.LayerType.HUMANOID, layer, layer.getTextureLocation(EquipmentClientInfo.LayerType.HUMANOID));

                    getParentModel().rightHandArmors().forEach(part -> {
                        poseStack.pushPose();
                        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(resourcelocation), false, glintIn);

                        getParentModel().translateToChestPat(part, poseStack);
                        Optional<ModelPart> optional = modelIn.getAnyDescendantWithName("right_arm");
                        if (optional.isPresent()) {
                            optional.get().render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);
                            renderTrim(chestItem, poseStack, bufferIn, EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(), optional.get(), j);
                        }
                        poseStack.popPose();
                    });
                    getParentModel().leftHandArmors().forEach(part -> {
                        poseStack.pushPose();
                        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(resourcelocation), false, glintIn);

                        getParentModel().translateToChestPat(part, poseStack);
                        Optional<ModelPart> optional = modelIn.getAnyDescendantWithName("left_arm");
                        if (optional.isPresent()) {
                            optional.get().render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);
                            renderTrim(chestItem, poseStack, bufferIn, EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(), optional.get(), j);
                        }
                        poseStack.popPose();
                    });
                    getParentModel().bodyPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(resourcelocation), false, glintIn);

                        this.getParentModel().translateToChest(part, poseStack);

                        Optional<ModelPart> optional = modelIn.getAnyDescendantWithName("body");
                        if (optional.isPresent()) {
                            optional.get().render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);
                            renderTrim(chestItem, poseStack, bufferIn, EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(), optional.get(), j);
                        }
                        poseStack.popPose();
                    });
                }
                idx++;
            }
        }
    }

    private void renderHelmet(ItemStack headItem, S entity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, Model modelIn, int color) {
        Equippable equippable = headItem.get(DataComponents.EQUIPPABLE);
        if (equippable != null && !equippable.assetId().isEmpty()) {
            int idx = 0;
            for (EquipmentClientInfo.Layer layer : this.equipmentModelSet.get(equippable.assetId().get()).getLayers(EquipmentClientInfo.LayerType.HUMANOID)) {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(headItem);

                int j = extensions.getArmorLayerTintColor(headItem, layer, idx, color);
                if (j != 0) {
                    //getParentModel().copyPropertiesTo(modelIn);
                    ResourceLocation resourcelocation = net.neoforged.neoforge.client.ClientHooks.getArmorTexture(headItem, EquipmentClientInfo.LayerType.HUMANOID, layer, layer.getTextureLocation(EquipmentClientInfo.LayerType.HUMANOID));
                    getParentModel().headPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(resourcelocation), false, glintIn);

                        this.getParentModel().translateToHead(part, poseStack);

                        Optional<ModelPart> optional = modelIn.getAnyDescendantWithName("head");
                        if (optional.isPresent()) {
                            optional.get().render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);
                            renderTrim(headItem, poseStack, bufferIn, EquipmentClientInfo.LayerType.HUMANOID, equippable.assetId().orElseThrow(), optional.get(), j);
                        }
                        poseStack.popPose();
                    });
                }
                idx++;
            }
        }
    }

    protected Model getArmorModelHook(ItemStack itemStack, EquipmentClientInfo.LayerType slot, Model model) {
        Model model2 = IClientItemExtensions.of(itemStack.getItem()).getGenericArmorModel(itemStack, slot, model);

        return model2;
    }

    @OnlyIn(Dist.CLIENT)
    record TrimSpriteKey(ArmorTrim trim, EquipmentClientInfo.LayerType layerType,
                         ResourceKey<EquipmentAsset> equipmentAssetId) {
        private static String getColorPaletteSuffix(Holder<TrimMaterial> p_387117_, ResourceKey<EquipmentAsset> p_386860_) {
            String s = p_387117_.value().overrideArmorAssets().get(p_386860_);
            return s != null ? s : p_387117_.value().assetName();
        }

        public ResourceLocation textureId() {
            ResourceLocation resourcelocation = this.trim.pattern().value().assetId();
            String s = getColorPaletteSuffix(this.trim.material(), this.equipmentAssetId);
            return resourcelocation.withPath(p_387008_ -> "trims/entity/" + this.layerType.getSerializedName() + "/" + p_387008_ + "_" + s);
        }
    }
}