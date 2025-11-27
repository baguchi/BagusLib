package baguchi.bagus_lib.client.render;// Made with Blockbench 4.7.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import baguchi.bagus_lib.client.layer.IArmor;
import baguchi.bagus_lib.client.render.state.MiniBaguRenderState;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class MiniBaguModel<T extends MiniBaguRenderState> extends EntityModel<T> implements IArmor, HeadedModel {
    private final ModelPart root;
    private final ModelPart head;
    private final ModelPart tail;
    private final ModelPart earR;
    private final ModelPart earL;
    public MiniBaguModel(ModelPart root) {
        super(root);
        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
        this.tail = this.head.getChild("tail");
        this.earR = this.head.getChild("earR");
        this.earL = this.head.getChild("earL");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F))
                .texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition tail = head.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, -1.0F, 0.0F, 3.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 4.0F));

        PartDefinition earR = head.addOrReplaceChild("earR", CubeListBuilder.create().texOffs(0, 16).addBox(-5.0F, -1.0F, -2.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, -6.4F, 0.0F, 0.0F, 0.0F, -1.0472F));

        PartDefinition earL = head.addOrReplaceChild("earL", CubeListBuilder.create().texOffs(0, 16).mirror().addBox(0.0F, -1.0F, -2.0F, 5.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offsetAndRotation(4.0F, -6.4F, 0.0F, 0.0F, 0.0F, 1.0472F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity) {
        super.setupAnim(entity);
        this.head.yRot = Mth.cos(entity.walkAnimationPos * 0.6662F) * 0.4F * entity.walkAnimationSpeed;
        this.tail.yRot = Mth.cos(entity.ageInTicks * 0.35F + entity.walkAnimationPos * 0.35F) * 0.4F;

        float f = entity.walkAnimationPos;
        float f1 = entity.walkAnimationSpeed;
        float f2 = ((float) Math.PI / 6F);
        float f3 = entity.ageInTicks * 0.1F + f * 0.5F;
        float f4 = 0.08F + f1 * 0.4F;
        this.earL.zRot += -Mth.cos((double) (f3 * 1.2F)) * f4;
        this.earR.zRot += Mth.cos((double) f3) * f4;

    }

    @Override
    public void translateToHead(ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
        poseStack.translate(0, 24 / 16F, 0);
    }

    @Override
    public void translateToChest(ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
        //poseStack.translate(0, -(12F / 16F), 0);
        poseStack.scale(1.05F, 1.05F, 1.05F);
    }

    @Override
    public void translateToLeg(ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
        poseStack.scale(1.05F, 1.05F, 1.05F);
    }

    @Override

    public void translateToChestPat(ModelPart modelPart, PoseStack poseStack) {
        modelPart.translateAndRotate(poseStack);
        poseStack.scale(1.05F, 1.05F, 1.05F);
    }
    @Override
    public Iterable<ModelPart> headPartArmors() {
        return ImmutableList.of(this.head);
    }

    @Override
    public ModelPart getHead() {
        return this.head;
    }

    @Override
    public void translateToHead(PoseStack p_443201_) {
        HeadedModel.super.translateToHead(p_443201_);
    }
}