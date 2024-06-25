package bagu_chan.bagus_lib.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.component.DyedItemColor;
import net.neoforged.neoforge.client.ClientHooks;
import org.joml.Quaternionf;


/*
 * https://github.com/AlexModGuy/AlexsMobs/blob/1.19.4/src/main/java/com/github/alexthe666/alexsmobs/client/render/layer/LayerKangarooArmor.java
 * Thanks Alex!
 */
public class CustomArmorLayer<T extends LivingEntity, M extends EntityModel<T> & IArmor> extends RenderLayer<T, M> {
    private final HumanoidModel defaultBipedModel;
    private final HumanoidModel innerModel;
    private RenderLayerParent<T, M> renderer;
    private final TextureAtlas armorTrimAtlas;

    public CustomArmorLayer(RenderLayerParent<T, M> render, EntityRendererProvider.Context context) {
        super(render);
        defaultBipedModel = new HumanoidModel(context.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR));
        this.innerModel = new HumanoidModel(context.bakeLayer(ModelLayers.ARMOR_STAND_INNER_ARMOR));
        this.renderer = render;
        this.armorTrimAtlas = context.getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    public CustomArmorLayer(RenderLayerParent<T, M> render, EntityModelSet modelSet, ModelManager modelManager) {
        super(render);
        defaultBipedModel = new HumanoidModel(modelSet.bakeLayer(ModelLayers.ARMOR_STAND_OUTER_ARMOR));
        this.innerModel = new HumanoidModel(modelSet.bakeLayer(ModelLayers.ARMOR_STAND_INNER_ARMOR));
        this.renderer = render;
        this.armorTrimAtlas = modelManager.getAtlas(Sheets.ARMOR_TRIMS_SHEET);
    }

    public static ResourceLocation getArmorResource(net.minecraft.world.entity.Entity entity, ItemStack stack, ArmorMaterial.Layer material, EquipmentSlot slot, boolean usesInner) {
        ArmorItem item = (ArmorItem) stack.getItem();
        String texture = item.getMaterial().getRegisteredName();
        String domain = "minecraft";
        int idx = texture.indexOf(':');
        if (idx != -1) {
            domain = texture.substring(0, idx);
            texture = texture.substring(idx + 1);
        }
        ResourceLocation s1 = ClientHooks.getArmorTexture(entity, stack, material, usesInner, slot);
        return s1;
    }

    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, LivingEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        {
            matrixStackIn.pushPose();
            ItemStack headItem = entity.getItemBySlot(EquipmentSlot.HEAD);
            if (headItem.getItem() instanceof ArmorItem) {
                ArmorItem armoritem = (ArmorItem) headItem.getItem();
                if (headItem.canEquip(EquipmentSlot.HEAD, entity)) {
                    for (ArmorMaterial.Layer armormaterial$layer : armoritem.getMaterial().value().layers()) {
                        boolean flag = this.usesInnerModel(armoritem.getEquipmentSlot());

                        HumanoidModel a = defaultBipedModel;
                        a = getArmorModelHook(entity, headItem, EquipmentSlot.HEAD, a);
                        boolean notAVanillaModel = a != defaultBipedModel;
                        this.setModelSlotVisible(a, EquipmentSlot.HEAD);
                        boolean flag1 = headItem.hasFoil();
                        int clampedLight = packedLightIn;
                        if (headItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                            int i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(headItem, -6265536));
                            renderHelmet(headItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, i, getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.HEAD, flag), notAVanillaModel);
                            renderHelmet(headItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, i, getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.HEAD, flag), notAVanillaModel);
                        } else {
                            renderHelmet(headItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1), getArmorResource(entity, headItem, armormaterial$layer, EquipmentSlot.HEAD, flag), notAVanillaModel);
                        }
                    }
                }
                } else {
                renderer.getModel().headPartArmors().forEach(part -> {
                    this.renderer.getModel().translateToHead(part, matrixStackIn);
                    matrixStackIn.translate(0, -0.25, 0.0F);
                    matrixStackIn.mulPose((new Quaternionf()).rotateX((float) Math.PI));
                    matrixStackIn.mulPose((new Quaternionf()).rotateY((float) Math.PI));
                    matrixStackIn.scale(0.625F, 0.625F, 0.625F);
                    Minecraft.getInstance().getItemRenderer().renderStatic(headItem, ItemDisplayContext.HEAD, packedLightIn, OverlayTexture.NO_OVERLAY, matrixStackIn, bufferIn, entity.level(), 0);
                });
            }
                matrixStackIn.popPose();
            }
            {
                matrixStackIn.pushPose();
                ItemStack chestItem = entity.getItemBySlot(EquipmentSlot.CHEST);
                if (chestItem.getItem() instanceof ArmorItem) {
                    ArmorItem armoritem = (ArmorItem) chestItem.getItem();
                    if (armoritem.getEquipmentSlot() == EquipmentSlot.CHEST) {
                        for (ArmorMaterial.Layer armormaterial$layer : armoritem.getMaterial().value().layers()) {
                            boolean flag = this.usesInnerModel(armoritem.getEquipmentSlot());
                            HumanoidModel a = defaultBipedModel;
                            a = getArmorModelHook(entity, chestItem, EquipmentSlot.CHEST, a);
                            boolean notAVanillaModel = a != defaultBipedModel;
                            this.setModelSlotVisible(a, EquipmentSlot.CHEST);

                            boolean flag1 = chestItem.hasFoil();
                            int clampedLight = packedLightIn;
                            if (chestItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                                int i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(chestItem, -6265536));
                                float f = (float) (i >> 16 & 255) / 255.0F;
                                float f1 = (float) (i >> 8 & 255) / 255.0F;
                                float f2 = (float) (i & 255) / 255.0F;
                                renderChestplate(chestItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, i, getArmorResource(entity, chestItem, armormaterial$layer, EquipmentSlot.CHEST, flag), notAVanillaModel);
                                renderChestplate(chestItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, i, getArmorResource(entity, chestItem, armormaterial$layer, EquipmentSlot.CHEST, flag), notAVanillaModel);
                            } else {
                                renderChestplate(chestItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1), getArmorResource(entity, chestItem, armormaterial$layer, EquipmentSlot.CHEST, flag), notAVanillaModel);
                            }
                        }
                    }
                }
                matrixStackIn.popPose();
            }
        {
            matrixStackIn.pushPose();
            ItemStack legItem = entity.getItemBySlot(EquipmentSlot.LEGS);
            if (legItem.getItem() instanceof ArmorItem) {
                ArmorItem armoritem = (ArmorItem) legItem.getItem();
                if (armoritem.getEquipmentSlot() == EquipmentSlot.LEGS) {
                    for (ArmorMaterial.Layer armormaterial$layer : armoritem.getMaterial().value().layers()) {
                        boolean flag = this.usesInnerModel(armoritem.getEquipmentSlot());

                        HumanoidModel a = this.innerModel;
                        a = getArmorModelHook(entity, legItem, EquipmentSlot.LEGS, a);
                        boolean notAVanillaModel = a != defaultBipedModel;
                        this.setModelSlotVisible(a, EquipmentSlot.LEGS);

                        boolean flag1 = legItem.hasFoil();
                        int clampedLight = packedLightIn;
                        if (legItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                            int i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(legItem, -6265536));
                            float f = (float) (i >> 16 & 255) / 255.0F;
                            float f1 = (float) (i >> 8 & 255) / 255.0F;
                            float f2 = (float) (i & 255) / 255.0F;
                            renderLeg(legItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, i, getArmorResource(entity, legItem, armormaterial$layer, EquipmentSlot.LEGS, flag), notAVanillaModel);
                            renderLeg(legItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, i, getArmorResource(entity, legItem, armormaterial$layer, EquipmentSlot.LEGS, flag), notAVanillaModel);
                        } else {
                            renderLeg(legItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1), getArmorResource(entity, legItem, armormaterial$layer, EquipmentSlot.LEGS, flag), notAVanillaModel);
                        }
                    }
                }
            }
            matrixStackIn.popPose();
        }
        {
            matrixStackIn.pushPose();
            ItemStack feetItem = entity.getItemBySlot(EquipmentSlot.FEET);
            if (feetItem.getItem() instanceof ArmorItem) {
                ArmorItem armoritem = (ArmorItem) feetItem.getItem();
                if (armoritem.getEquipmentSlot() == EquipmentSlot.FEET) {
                    for (ArmorMaterial.Layer armormaterial$layer : armoritem.getMaterial().value().layers()) {
                        boolean flag = this.usesInnerModel(armoritem.getEquipmentSlot());

                        HumanoidModel a = defaultBipedModel;
                        a = getArmorModelHook(entity, feetItem, EquipmentSlot.FEET, a);
                        boolean notAVanillaModel = a != defaultBipedModel;
                        this.setModelSlotVisible(a, EquipmentSlot.FEET);

                        boolean flag1 = feetItem.hasFoil();
                        int clampedLight = packedLightIn;
                        if (feetItem.is(ItemTags.DYEABLE)) { // Allow this for anything, not only cloth
                            int i = FastColor.ARGB32.opaque(DyedItemColor.getOrDefault(feetItem, -6265536));
                            float f = (float) (i >> 16 & 255) / 255.0F;
                            float f1 = (float) (i >> 8 & 255) / 255.0F;
                            float f2 = (float) (i & 255) / 255.0F;
                            renderBoot(feetItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, i, getArmorResource(entity, feetItem, armormaterial$layer, EquipmentSlot.FEET, flag), notAVanillaModel);
                            renderBoot(feetItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, i, getArmorResource(entity, feetItem, armormaterial$layer, EquipmentSlot.FEET, flag), notAVanillaModel);
                        } else {
                            renderBoot(feetItem, entity, matrixStackIn, bufferIn, clampedLight, flag1, a, FastColor.ARGB32.colorFromFloat(1, 1, 1, 1), getArmorResource(entity, feetItem, armormaterial$layer, EquipmentSlot.FEET, flag), notAVanillaModel);
                        }
                    }
                }
            }
            matrixStackIn.popPose();
        }

    }

    private static boolean usesInnerModel(EquipmentSlot p_117129_) {
        return p_117129_ == EquipmentSlot.LEGS;
    }

    private HumanoidModel getArmorModel(EquipmentSlot p_117079_) {
        return (usesInnerModel(p_117079_) ? this.innerModel : this.defaultBipedModel);
    }

    private void renderTrim(ModelPart part, Holder<ArmorMaterial> p_267946_, PoseStack p_268019_, MultiBufferSource p_268023_, int p_268190_, ArmorTrim p_267984_, boolean p_267965_, HumanoidModel p_267949_, boolean p_268259_, int color) {
        TextureAtlasSprite textureatlassprite = this.armorTrimAtlas.getSprite(p_268259_ ? p_267984_.innerTexture(p_267946_) : p_267984_.outerTexture(p_267946_));
        VertexConsumer vertexconsumer = textureatlassprite.wrap(ItemRenderer.getFoilBufferDirect(p_268023_, Sheets.armorTrimsSheet(p_267984_.pattern().value().decal()), true, p_267965_));
        part.render(p_268019_, vertexconsumer, p_268190_, OverlayTexture.NO_OVERLAY, color);
    }

    private void renderTrim(ModelPart part, ItemStack item, LivingEntity entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, EquipmentSlot equipmentSlot, HumanoidModel modelIn) {
        if (item.getItem() instanceof ArmorItem armorItem) {
            ArmorTrim armortrim = item.get(DataComponents.TRIM);
            if (armortrim != null) {
                this.renderTrim(part, armorItem.getMaterial(), matrixStackIn, bufferIn, packedLightIn, armortrim, glintIn, modelIn, this.usesInnerModel(equipmentSlot), FastColor.ARGB32.colorFromFloat(1, 1, 1, 1));
            }
        }
    }

    private void renderLeg(ItemStack legItem, LivingEntity entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color, ResourceLocation armorResource, boolean notAVanillaModel) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorResource), false, glintIn);
        renderer.getModel().copyPropertiesTo(modelIn);
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
        renderer.getModel().rightLegPartArmors().forEach(part -> {
                    matrixStackIn.pushPose();
                    renderer.getModel().translateToLeg(part, matrixStackIn);

            modelIn.rightLeg.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
                    renderTrim(modelIn.rightLeg, legItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.LEGS, modelIn);

                    matrixStackIn.popPose();
                }
        );
        renderer.getModel().leftLegPartArmors().forEach(part -> {
            matrixStackIn.pushPose();
            renderer.getModel().translateToLeg(part, matrixStackIn);

            modelIn.leftLeg.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            renderTrim(modelIn.leftLeg, legItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.LEGS, modelIn);

            matrixStackIn.popPose();
        });
        renderer.getModel().bodyPartArmors().forEach(part -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToChest(part, matrixStackIn);

            modelIn.body.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            renderTrim(modelIn.body, legItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.LEGS, modelIn);

            matrixStackIn.popPose();
        });
    }

    private void renderBoot(ItemStack feetItem, LivingEntity entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color, ResourceLocation armorResource, boolean notAVanillaModel) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorResource), false, glintIn);
        renderer.getModel().copyPropertiesTo(modelIn);
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
        renderer.getModel().rightLegPartArmors().forEach(part -> {
            matrixStackIn.pushPose();
            renderer.getModel().translateToLeg(part, matrixStackIn);
            modelIn.rightLeg.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            renderTrim(modelIn.rightLeg, feetItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.FEET, modelIn);

            matrixStackIn.popPose();
        });
        renderer.getModel().leftLegPartArmors().forEach(part -> {
            matrixStackIn.pushPose();
            renderer.getModel().translateToLeg(part, matrixStackIn);
            modelIn.leftLeg.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            renderTrim(modelIn.leftLeg, feetItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.FEET, modelIn);

            matrixStackIn.popPose();
        });

    }


    private void renderChestplate(ItemStack chestItem, LivingEntity entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color, ResourceLocation armorResource, boolean notAVanillaModel) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorResource), false, glintIn);
        renderer.getModel().copyPropertiesTo(modelIn);
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
        renderer.getModel().rightHandArmors().forEach(part -> {
            matrixStackIn.pushPose();
            renderer.getModel().translateToChestPat(part, matrixStackIn);
            modelIn.rightArm.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            renderTrim(modelIn.rightArm, chestItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.CHEST, modelIn);

            matrixStackIn.popPose();
        });
        renderer.getModel().leftHandArmors().forEach(part -> {
            matrixStackIn.pushPose();
            renderer.getModel().translateToChestPat(part, matrixStackIn);
            modelIn.leftArm.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            renderTrim(modelIn.leftArm, chestItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.CHEST, modelIn);

            matrixStackIn.popPose();
        });
        renderer.getModel().bodyPartArmors().forEach(part -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToChest(part, matrixStackIn);

            modelIn.body.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            renderTrim(modelIn.body, chestItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.CHEST, modelIn);

            matrixStackIn.popPose();
        });
    }

    private void renderHelmet(ItemStack headItem, LivingEntity entity, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, boolean glintIn, HumanoidModel modelIn, int color, ResourceLocation armorResource, boolean notAVanillaModel) {
        VertexConsumer ivertexbuilder = ItemRenderer.getFoilBuffer(bufferIn, RenderType.entityCutoutNoCull(armorResource), false, glintIn);
        renderer.getModel().copyPropertiesTo(modelIn);
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
        renderer.getModel().headPartArmors().forEach(part -> {
            matrixStackIn.pushPose();
            this.renderer.getModel().translateToHead(part, matrixStackIn);
            modelIn.head.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, color);
            renderTrim(modelIn.head, headItem, entity, matrixStackIn, bufferIn, packedLightIn, glintIn, EquipmentSlot.HEAD, modelIn);
            matrixStackIn.popPose();
        });

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


    protected HumanoidModel<?> getArmorModelHook(LivingEntity entity, ItemStack itemStack, EquipmentSlot slot, HumanoidModel model) {
        Model basicModel = ClientHooks.getArmorModel(entity, itemStack, slot, model);
        return basicModel instanceof HumanoidModel ? (HumanoidModel<?>) basicModel : model;
    }
}