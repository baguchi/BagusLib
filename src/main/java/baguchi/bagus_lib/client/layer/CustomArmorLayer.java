package baguchi.bagus_lib.client.layer;

import baguchi.bagus_lib.api.IBagusExtraRenderState;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.ArmorModelSet;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;

import java.util.function.Function;


/*
 * https://github.com/AlexModGuy/AlexsMobs/blob/1.19.4/src/main/java/com/github/alexthe666/alexsmobs/client/render/layer/LayerKangarooArmor.java
 * Thanks Alex!
 */
public class CustomArmorLayer<S extends LivingEntityRenderState, M extends EntityModel<S> & IArmor, A extends EntityModel<S>> extends RenderLayer<S, M> {
    private final ArmorModelSet<PlayerModel> armorModelSet;
    private final RenderLayerParent<S, M> renderer;
    private final EquipmentLayerRenderer equipmentRenderer;


    public CustomArmorLayer(RenderLayerParent<S, M> render, EntityRendererProvider.Context context) {
        super(render);
        armorModelSet = ArmorModelSet.bake(
                ModelLayers.PLAYER_ARMOR,
                context.getModelSet(),
                p_446041_ -> new PlayerModel(p_446041_, false)
        );
        this.renderer = render;
        this.equipmentRenderer = context.getEquipmentRenderer();
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
        PlayerModel headPart = getArmorModelHook(this.getArmorModel(EquipmentSlot.HEAD), "head");

        getParentModel().headPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToHead(part, poseStack);
            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.HEAD, packedLightIn, headPart);
            poseStack.popPose();
        });
    }

    private void renderBoots(ItemStack stack, PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn) {
        PlayerModel rightLegPart = getArmorModelHook(this.getArmorModel(EquipmentSlot.FEET), "right_arm");
        PlayerModel leftLegPart = getArmorModelHook(this.getArmorModel(EquipmentSlot.FEET), "left_arm");

        getParentModel().rightLegPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToLeg(part, poseStack);

            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.FEET, packedLightIn, rightLegPart);
            poseStack.popPose();
        });
        getParentModel().leftLegPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToLeg(part, poseStack);

            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.FEET, packedLightIn, leftLegPart);
            poseStack.popPose();
        });
    }


    private void renderLeggings(ItemStack stack, PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn) {
        PlayerModel bodyPart = getArmorModelHook(this.getArmorModel(EquipmentSlot.LEGS), "body");
        PlayerModel rightLegPart = getArmorModelHook(this.getArmorModel(EquipmentSlot.LEGS), "right_arm");
        PlayerModel leftLegPart = getArmorModelHook(this.getArmorModel(EquipmentSlot.LEGS), "left_arm");

        getParentModel().bodyPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToChest(part, poseStack);

            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.LEGS, packedLightIn, bodyPart);
            poseStack.popPose();
        });

        getParentModel().rightLegPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToLeg(part, poseStack);

            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.LEGS, packedLightIn, rightLegPart);
            poseStack.popPose();
        });
        getParentModel().leftLegPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToLeg(part, poseStack);

            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.LEGS, packedLightIn, leftLegPart);
            poseStack.popPose();
        });
    }

    private void renderChestplate(ItemStack stack, PoseStack poseStack, SubmitNodeCollector bufferIn, int packedLightIn) {
        PlayerModel bodyPart = getArmorModelHook(this.getArmorModel(EquipmentSlot.CHEST), "body");
        PlayerModel rightArmPart = getArmorModelHook(this.getArmorModel(EquipmentSlot.CHEST), "right_arm");
        PlayerModel leftArmPart = getArmorModelHook(this.getArmorModel(EquipmentSlot.CHEST), "left_arm");

        getParentModel().bodyPartArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToChest(part, poseStack);
            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.CHEST, packedLightIn, bodyPart);
            poseStack.popPose();
        });

        getParentModel().rightHandArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToChestPat(part, poseStack);
            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.CHEST, packedLightIn, rightArmPart);
            poseStack.popPose();
        });
        getParentModel().leftHandArmors().forEach(part -> {
            poseStack.pushPose();
            getParentModel().translateToChestPat(part, poseStack);
            renderArmorPiece(poseStack, bufferIn, stack, EquipmentSlot.CHEST, packedLightIn, leftArmPart);
            poseStack.popPose();
        });
    }

    private void renderArmorPiece(PoseStack p_117119_, SubmitNodeCollector p_433453_, ItemStack p_362532_, EquipmentSlot p_117122_, int p_117123_, PlayerModel armorModel) {
        Equippable equippable = p_362532_.get(DataComponents.EQUIPPABLE);
        if (equippable != null && shouldRender(equippable, p_117122_)) {

            AvatarRenderState avatarRenderState = new AvatarRenderState();

            EquipmentClientInfo.LayerType equipmentclientinfo$layertype = usesInnerModel(p_117122_) ? EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS : EquipmentClientInfo.LayerType.HUMANOID;
            this.equipmentRenderer.renderLayers(equipmentclientinfo$layertype, equippable.assetId().orElseThrow(), armorModel, avatarRenderState, p_362532_, p_117119_, p_433453_, p_117123_, avatarRenderState.outlineColor);
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


    protected PlayerModel getArmorModelHook(PlayerModel model, String usingPart) {
        Function<String, ModelPart> function = model.root().createPartLookup();

        //reset the model
        resetModelPart(function.apply("right_leg"));
        resetModelPart(function.apply("left_leg"));
        resetModelPart(function.apply("right_arm"));
        resetModelPart(function.apply("left_arm"));
        resetModelPart(function.apply("head"));
        resetModelPart(function.apply("body"));

        function.apply(usingPart).visible = true;
        return model;
    }

    private void resetModelPart(ModelPart part) {

        ModelPart modelPart = part;
        modelPart.x = 0;
        modelPart.y = 0;
        modelPart.z = 0;
        modelPart.xRot = 0;
        modelPart.yRot = 0;
        modelPart.zRot = 0;
        modelPart.visible = false;
    }
}