package bagu_chan.bagus_lib.client;// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import bagu_chan.bagus_lib.client.layer.IArmor;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

public class MiniBaguModel<T extends Entity> extends HierarchicalModel<T> implements IArmor {
    private final ModelPart root;
    private final ModelPart head;

    public MiniBaguModel(ModelPart root) {
        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail = head.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -3.0F, 0.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.yRot = Mth.cos(limbSwing * 0.6662F) * 0.8F * limbSwingAmount;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public ModelPart root() {
        return this.root;
    }

    @Override
    public void translateToHead(ModelPart part, PoseStack poseStack) {
        this.root.translateAndRotate(poseStack);
        part.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChest(ModelPart part, PoseStack poseStack) {

    }

    @Override
    public void translateToLeg(ModelPart part, PoseStack poseStack) {

    }

    @Override
    public void translateToChestPat(ModelPart part, PoseStack poseStack) {

    }

    @Override
    public Iterable<ModelPart> rightHandArmors() {
        return null;
    }

    @Override
    public Iterable<ModelPart> leftHandArmors() {
        return null;
    }

    @Override
    public Iterable<ModelPart> rightLegPartArmors() {
        return null;
    }

    @Override
    public Iterable<ModelPart> leftLegPartArmors() {
        return null;
    }

    @Override
    public Iterable<ModelPart> bodyPartArmors() {
        return null;
    }

    @Override
    public Iterable<ModelPart> headPartArmors() {
        return ImmutableList.of(this.head);
    }
}