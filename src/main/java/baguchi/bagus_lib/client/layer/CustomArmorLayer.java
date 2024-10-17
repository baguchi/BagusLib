package baguchi.bagus_lib.client.layer;

import baguchi.bagus_lib.api.IBagusExtraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
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
import net.minecraft.client.resources.model.EquipmentModelSet;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.EquipmentModel;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.neoforge.client.ClientHooks;


/*
 * https://github.com/AlexModGuy/AlexsMobs/blob/1.19.4/src/main/java/com/github/alexthe666/alexsmobs/client/render/layer/LayerKangarooArmor.java
 * Thanks Alex!
 */
public class CustomArmorLayer<S extends LivingEntityRenderState, M extends EntityModel<S> & IArmor, A extends EntityModel<S>> extends RenderLayer<S, M> {
    private final HumanoidModel defaultBipedModel;
    private final HumanoidModel innerModel;
    private RenderLayerParent<S, M> renderer;
    private final TextureAtlas armorTrimAtlas;
    private final EquipmentModelSet equipmentModelSet;

    public CustomArmorLayer(RenderLayerParent<S, M> render, EntityRendererProvider.Context context) {
        super(render);
        defaultBipedModel = new HumanoidModel(context.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR));
        this.innerModel = new HumanoidModel(context.bakeLayer(ModelLayers.ARMOR_STAND_INNER_ARMOR));
        this.renderer = render;
        this.armorTrimAtlas = context.getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET);
        this.equipmentModelSet = context.getEquipmentModels();
    }

    public CustomArmorLayer(RenderLayerParent<S, M> render, EntityModelSet modelSet, ModelManager modelManager, EquipmentModelSet equipmentModelSet) {
        super(render);
        defaultBipedModel = new HumanoidModel(modelSet.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR));
        this.innerModel = new HumanoidModel(modelSet.bakeLayer(ModelLayers.ARMOR_STAND_INNER_ARMOR));
        this.renderer = render;
        this.armorTrimAtlas = modelManager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
        this.equipmentModelSet = equipmentModelSet;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferIn, int light, S entity, float p_117353_, float p_117354_) {
        if (entity instanceof IBagusExtraRenderState bagusExtraRenderState) {
            poseStack.pushPose();
            ItemStack headItem = entity.headItem;
            if (headItem.getItem() instanceof ArmorItem) {
                ArmorItem armoritem = (ArmorItem) headItem.getItem();
                EquipmentModel.LayerType equipmentmodel$layerType = this.usesInnerModel(EquipmentSlot.HEAD)
                        ? EquipmentModel.LayerType.HUMANOID_LEGGINGS
                        : EquipmentModel.LayerType.HUMANOID;
                HumanoidModel a = defaultBipedModel;
                a = getArmorModelHook(headItem, equipmentmodel$layerType, a);
                boolean notAVanillaModel = a != defaultBipedModel;
                this.setModelSlotVisible(a, EquipmentSlot.HEAD);
                boolean flag1 = headItem.hasFoil();
                int clampedLight = light;
                if (headItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                    net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(headItem);
                    int i = extensions.getDefaultDyeColor(headItem);
                    renderHelmet(headItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                    renderHelmet(headItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                } else {
                    net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(headItem);
                    int i = extensions.getDefaultDyeColor(headItem);

                    renderHelmet(headItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                    }

            }/* else {
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
            if (chestItem.getItem() instanceof ArmorItem) {
                ArmorItem armoritem = (ArmorItem) chestItem.getItem();
                if (armoritem.getEquipmentSlot(chestItem) == EquipmentSlot.CHEST) {
                    EquipmentModel.LayerType equipmentmodel$layerType = this.usesInnerModel(EquipmentSlot.CHEST)
                            ? EquipmentModel.LayerType.HUMANOID_LEGGINGS
                            : EquipmentModel.LayerType.HUMANOID;
                    boolean flag = this.usesInnerModel(armoritem.getEquipmentSlot(chestItem));
                    HumanoidModel a = defaultBipedModel;
                    a = getArmorModelHook(chestItem, equipmentmodel$layerType, a);
                    boolean notAVanillaModel = a != defaultBipedModel;
                    this.setModelSlotVisible(a, EquipmentSlot.CHEST);

                    boolean flag1 = chestItem.hasFoil();
                    int clampedLight = light;
                    if (chestItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                        net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(chestItem);
                        int i = extensions.getDefaultDyeColor(chestItem);
                        float f = (float) (i >> 16 & 255) / 255.0F;
                        float f1 = (float) (i >> 8 & 255) / 255.0F;
                        float f2 = (float) (i & 255) / 255.0F;
                        renderChestplate(chestItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                        renderChestplate(chestItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                    } else {
                        net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(chestItem);
                        int i = extensions.getDefaultDyeColor(chestItem);

                        renderChestplate(chestItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                    }
                }
            }
            poseStack.popPose();

            poseStack.pushPose();
            ItemStack legItem = bagusExtraRenderState.getBagusLib$legItem();
            if (legItem.getItem() instanceof ArmorItem) {
                ArmorItem armoritem = (ArmorItem) legItem.getItem();
                if (armoritem.getEquipmentSlot(legItem) == EquipmentSlot.LEGS) {
                    EquipmentModel.LayerType equipmentmodel$layerType = this.usesInnerModel(EquipmentSlot.LEGS)
                            ? EquipmentModel.LayerType.HUMANOID_LEGGINGS
                            : EquipmentModel.LayerType.HUMANOID;
                    boolean flag = this.usesInnerModel(armoritem.getEquipmentSlot(legItem));

                    HumanoidModel a = this.innerModel;
                    a = getArmorModelHook(legItem, equipmentmodel$layerType, a);
                    boolean notAVanillaModel = a != defaultBipedModel;
                    this.setModelSlotVisible(a, EquipmentSlot.LEGS);

                    boolean flag1 = legItem.hasFoil();
                    int clampedLight = light;
                    if (legItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                        net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(legItem);
                        int i = extensions.getDefaultDyeColor(legItem);
                        float f = (float) (i >> 16 & 255) / 255.0F;
                        float f1 = (float) (i >> 8 & 255) / 255.0F;
                        float f2 = (float) (i & 255) / 255.0F;
                        renderLeg(legItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                        renderLeg(legItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                    } else {
                        net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(legItem);
                        int i = extensions.getDefaultDyeColor(legItem);

                        renderLeg(legItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                    }
                }
            }
            poseStack.popPose();

            poseStack.pushPose();
            ItemStack feetItem = bagusExtraRenderState.getBagusLib$feetItem();
            if (feetItem.getItem() instanceof ArmorItem) {
                ArmorItem armoritem = (ArmorItem) feetItem.getItem();
                if (armoritem.getEquipmentSlot(feetItem) == EquipmentSlot.FEET) {
                    EquipmentModel.LayerType equipmentmodel$layerType = this.usesInnerModel(EquipmentSlot.FEET)
                            ? EquipmentModel.LayerType.HUMANOID_LEGGINGS
                            : EquipmentModel.LayerType.HUMANOID;
                    boolean flag = this.usesInnerModel(armoritem.getEquipmentSlot(feetItem));

                    HumanoidModel a = defaultBipedModel;
                    a = getArmorModelHook(feetItem, equipmentmodel$layerType, a);
                    boolean notAVanillaModel = a != defaultBipedModel;
                    this.setModelSlotVisible(a, EquipmentSlot.FEET);

                    boolean flag1 = feetItem.hasFoil();
                    int clampedLight = light;
                    if (feetItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                        net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(feetItem);
                        int i = extensions.getDefaultDyeColor(feetItem);
                        float f = (float) (i >> 16 & 255) / 255.0F;
                        float f1 = (float) (i >> 8 & 255) / 255.0F;
                        float f2 = (float) (i & 255) / 255.0F;
                        renderBoot(feetItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                        renderBoot(feetItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                    } else {
                        net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(feetItem);
                        int i = extensions.getDefaultDyeColor(feetItem);
                        renderBoot(feetItem, entity, poseStack, bufferIn, clampedLight, flag1, a, i);
                    }
                }
            }
            poseStack.popPose();
        }

    }

    private static boolean usesInnerModel(EquipmentSlot p_117129_) {
        return p_117129_ == EquipmentSlot.LEGS;
    }

    private void renderLeg(ItemStack legItem, S entity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color) {
        Equippable equippable = legItem.get(DataComponents.EQUIPPABLE);
        if (equippable != null && !equippable.model().isEmpty()) {
            int idx = 0;
            for (EquipmentModel.Layer layer : this.equipmentModelSet.get(equippable.model().get()).layers().get(EquipmentModel.LayerType.HUMANOID)) {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(legItem);

                int j = extensions.getArmorLayerTintColor(legItem, layer, idx, color);
                if (j != 0) {
                    VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(layer.getTextureLocation(EquipmentModel.LayerType.HUMANOID)), false, glintIn);

                    modelIn.body.xRot = 0F;
                    modelIn.body.yRot = 0;
                    modelIn.body.zRot = 0;
                    modelIn.body.x = 0;
                    modelIn.body.y = 0F;
                    modelIn.body.z = 0F;
                    modelIn.rightLeg.x = 0F;
                    modelIn.rightLeg.xRot = 0F;
                    modelIn.rightLeg.yRot = 0F;
                    modelIn.rightLeg.zRot = 0F;
                    modelIn.leftLeg.x = 0F;
                    modelIn.leftLeg.xRot = 0F;
                    modelIn.leftLeg.yRot = 0F;
                    modelIn.leftLeg.zRot = 0F;
                    modelIn.leftLeg.y = 0F;
                    modelIn.rightLeg.y = 0F;
                    modelIn.leftLeg.z = 0F;
                    modelIn.rightLeg.z = 0F;
                    getParentModel().rightLegPartArmors().forEach(part -> {
                                poseStack.pushPose();
                                getParentModel().translateToLeg(part, poseStack);

                                modelIn.rightLeg.render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);

                                poseStack.popPose();
                            }
                    );
                    getParentModel().leftLegPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        getParentModel().translateToLeg(part, poseStack);

                        modelIn.leftLeg.render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);

                        poseStack.popPose();
                    });
                    getParentModel().bodyPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        this.getParentModel().translateToChest(part, poseStack);

                        modelIn.body.render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);

                        poseStack.popPose();
                    });
                }
                idx++;
            }
        }
    }

    private void renderBoot(ItemStack feetItem, S entity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color) {
        Equippable equippable = feetItem.get(DataComponents.EQUIPPABLE);
        if (equippable != null && !equippable.model().isEmpty()) {
            int idx = 0;
            for (EquipmentModel.Layer layer : this.equipmentModelSet.get(equippable.model().get()).layers().get(EquipmentModel.LayerType.HUMANOID)) {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(feetItem);

                int j = extensions.getArmorLayerTintColor(feetItem, layer, idx, color);
                if (j != 0) {
                    VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(layer.getTextureLocation(EquipmentModel.LayerType.HUMANOID)), false, glintIn);

                    modelIn.rightLeg.x = 0F;
                    modelIn.rightLeg.xRot = 0F;
                    modelIn.rightLeg.yRot = 0F;
                    modelIn.rightLeg.zRot = 0F;
                    modelIn.leftLeg.x = 0F;
                    modelIn.leftLeg.xRot = 0F;
                    modelIn.leftLeg.yRot = 0F;
                    modelIn.leftLeg.zRot = 0F;
                    modelIn.leftLeg.y = 0F;
                    modelIn.rightLeg.y = 0F;
                    modelIn.leftLeg.z = 0F;
                    modelIn.rightLeg.z = 0F;
                    getParentModel().rightLegPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        getParentModel().translateToLeg(part, poseStack);
                        modelIn.rightLeg.render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);

                        poseStack.popPose();
                    });
                    getParentModel().leftLegPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        getParentModel().translateToLeg(part, poseStack);
                        modelIn.leftLeg.render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);

                        poseStack.popPose();
                    });
                }
                idx++;
            }
        }
    }


    private void renderChestplate(ItemStack chestItem, S entity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color) {
        Equippable equippable = chestItem.get(DataComponents.EQUIPPABLE);

        if (equippable != null && !equippable.model().isEmpty()) {
            int idx = 0;
            for (EquipmentModel.Layer layer : this.equipmentModelSet.get(equippable.model().get()).layers().get(EquipmentModel.LayerType.HUMANOID)) {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(chestItem);

                int j = extensions.getArmorLayerTintColor(chestItem, layer, idx, color);
                if (j != 0) {
                    VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(layer.getTextureLocation(EquipmentModel.LayerType.HUMANOID)), false, glintIn);

                    modelIn.body.xRot = 0F;
                    modelIn.body.yRot = 0;
                    modelIn.body.zRot = 0;
                    modelIn.body.x = 0;
                    modelIn.body.y = 0F;
                    modelIn.body.z = 0F;
                    modelIn.rightArm.x = 0F;
                    modelIn.rightArm.y = 0F;
                    modelIn.rightArm.z = 0F;
                    modelIn.rightArm.xRot = 0F;
                    modelIn.rightArm.yRot = 0F;
                    modelIn.rightArm.zRot = 0F;
                    modelIn.leftArm.x = 0F;
                    modelIn.leftArm.y = 0F;
                    modelIn.leftArm.z = 0F;
                    modelIn.leftArm.xRot = 0F;
                    modelIn.leftArm.yRot = 0F;
                    modelIn.leftArm.zRot = 0F;
                    modelIn.leftArm.y = 0F;
                    modelIn.rightArm.y = 0F;
                    modelIn.leftArm.z = 0F;
                    modelIn.rightArm.z = 0F;
                    getParentModel().rightHandArmors().forEach(part -> {
                        poseStack.pushPose();
                        getParentModel().translateToChestPat(part, poseStack);
                        modelIn.rightArm.render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);

                        poseStack.popPose();
                    });
                    getParentModel().leftHandArmors().forEach(part -> {
                        poseStack.pushPose();
                        getParentModel().translateToChestPat(part, poseStack);
                        modelIn.leftArm.render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);

                        poseStack.popPose();
                    });
                    getParentModel().bodyPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        this.getParentModel().translateToChest(part, poseStack);

                        modelIn.body.render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);

                        poseStack.popPose();
                    });
                }
                idx++;
            }
        }
    }

    private void renderHelmet(ItemStack headItem, S entity, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color) {
        Equippable equippable = headItem.get(DataComponents.EQUIPPABLE);
        if (equippable != null && !equippable.model().isEmpty()) {
            int idx = 0;
            for (EquipmentModel.Layer layer : this.equipmentModelSet.get(equippable.model().get()).layers().get(EquipmentModel.LayerType.HUMANOID)) {
                net.neoforged.neoforge.client.extensions.common.IClientItemExtensions extensions = net.neoforged.neoforge.client.extensions.common.IClientItemExtensions.of(headItem);

                int j = extensions.getArmorLayerTintColor(headItem, layer, idx, color);
                if (j != 0) {
                    //getParentModel().copyPropertiesTo(modelIn);
                    VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(layer.getTextureLocation(EquipmentModel.LayerType.HUMANOID)), false, glintIn);

                    modelIn.head.xRot = 0F;
                    modelIn.head.yRot = 0F;
                    modelIn.head.zRot = 0F;
                    modelIn.hat.xRot = 0F;
                    modelIn.hat.yRot = 0F;
                    modelIn.hat.zRot = 0F;
                    modelIn.head.x = 0F;
                    modelIn.head.y = 0F;
                    modelIn.head.z = 0F;
                    modelIn.hat.x = 0F;
                    modelIn.hat.y = 0F;
                    modelIn.hat.z = 0F;
                    getParentModel().headPartArmors().forEach(part -> {
                        poseStack.pushPose();
                        this.getParentModel().translateToHead(part, poseStack);
                        modelIn.head.render(poseStack, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, j);
                        poseStack.popPose();
                    });
                }
                idx++;
            }
        }
    }


    protected void setModelSlotVisible(HumanoidModel p_188359_1_, EquipmentSlot slotIn) {
        this.setModelVisible(p_188359_1_);
        switch (slotIn) {
            case HEAD:
                p_188359_1_.head.visible = true;
                p_188359_1_.hat.visible = true;
                break;
            case CHEST:
                p_188359_1_.body.visible = true;
                p_188359_1_.rightArm.visible = true;
                p_188359_1_.leftArm.visible = true;
                break;
            case LEGS:
                p_188359_1_.body.visible = true;
                p_188359_1_.rightLeg.visible = true;
                p_188359_1_.leftLeg.visible = true;
                break;
            case FEET:
                p_188359_1_.rightLeg.visible = true;
                p_188359_1_.leftLeg.visible = true;
        }
    }

    protected void setModelVisible(HumanoidModel model) {
        model.setAllVisible(false);
    }


    protected HumanoidModel<?> getArmorModelHook(ItemStack itemStack, EquipmentModel.LayerType slot, HumanoidModel model) {
        Model basicModel = ClientHooks.getArmorModel(itemStack, slot, model);
        return basicModel instanceof HumanoidModel ? (HumanoidModel<?>) basicModel : model;
    }
}