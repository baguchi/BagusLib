package baguchi.bagus_lib.client.render;// Made with Blockbench 4.12.6
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import baguchi.bagus_lib.BagusLib;
import net.minecraft.client.animation.KeyframeAnimation;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.entity.animation.json.AnimationHolder;

public class MossModel<T extends LivingEntityRenderState> extends EntityModel<T> {
    public static final AnimationHolder WALK_ANIMATION = Model.getAnimation(ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "moss/walk"));

    private final KeyframeAnimation walkAnimation;

    private final ModelPart root;
    private final ModelPart leg_r;
    private final ModelPart leg_r2;
    private final ModelPart leg_l;
    private final ModelPart leg_l2;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart mouth;
    private final ModelPart jaw;
    private final ModelPart ear_r;
    private final ModelPart ear_l;
    private final ModelPart tail;
    private final ModelPart tail2;
    private final ModelPart leaf;

    public MossModel(ModelPart root) {
        super(root);
        this.walkAnimation = WALK_ANIMATION.get().bake(root);
        this.root = root.getChild("root");
        this.leg_r = this.root.getChild("leg_r");
        this.leg_r2 = this.leg_r.getChild("leg_r2");
        this.leg_l = this.root.getChild("leg_l");
        this.leg_l2 = this.leg_l.getChild("leg_l2");
        this.body = this.root.getChild("body");
        this.head = this.body.getChild("head");
        this.mouth = this.head.getChild("mouth");
        this.jaw = this.mouth.getChild("jaw");
        this.ear_r = this.head.getChild("ear_r");
        this.ear_l = this.head.getChild("ear_l");
        this.tail = this.body.getChild("tail");
        this.tail2 = this.tail.getChild("tail2");
        this.leaf = this.tail2.getChild("leaf");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition leg_r = root.addOrReplaceChild("leg_r", CubeListBuilder.create().texOffs(0, 36).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -4.0F, 4.0F));

        PartDefinition leg_r2 = leg_r.addOrReplaceChild("leg_r2", CubeListBuilder.create().texOffs(8, 36).addBox(-2.0F, 0.0F, -4.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition leg_l = root.addOrReplaceChild("leg_l", CubeListBuilder.create().texOffs(0, 36).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -4.0F, 4.0F));

        PartDefinition leg_l2 = leg_l.addOrReplaceChild("leg_l2", CubeListBuilder.create().texOffs(8, 36).addBox(-1.0F, 0.0F, -4.0F, 3.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, 0.0F));

        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 12).addBox(-6.0F, -12.0F, -6.0F, 12.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 4.0F));

        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-5.0F, -6.0F, -6.0F, 10.0F, 6.0F, 6.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-5.5F, 0.0F, -6.0F, 11.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, -6.0F));

        PartDefinition mouth = head.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(66, 0).addBox(-3.5F, -4.0F, -2.0F, 7.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 2.0F, -6.0F));

        PartDefinition jaw = mouth.addOrReplaceChild("jaw", CubeListBuilder.create().texOffs(84, 0).addBox(-2.0F, 0.0F, -2.0F, 4.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.5672F, 0.0F, 0.0F));

        PartDefinition ear_r = head.addOrReplaceChild("ear_r", CubeListBuilder.create().texOffs(36, 12).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 12).addBox(-4.0F, -4.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.0F, -6.0F, -3.0F));

        PartDefinition ear_l = head.addOrReplaceChild("ear_l", CubeListBuilder.create().texOffs(36, 12).addBox(-1.0F, -4.0F, -1.0F, 2.0F, 4.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(44, 12).addBox(1.0F, -4.0F, -1.0F, 3.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.0F, -6.0F, -3.0F));

        PartDefinition tail = body.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(49, 12).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 6.0F));

        PartDefinition tail2 = tail.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(49, 12).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

        PartDefinition leaf = tail2.addOrReplaceChild("leaf", CubeListBuilder.create().texOffs(45, 19).addBox(-2.0F, -1.0F, 0.0F, 4.0F, 0.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

        return LayerDefinition.create(meshdefinition, 128, 64);
    }

    @Override
    public void setupAnim(T p_364104_) {
        super.setupAnim(p_364104_);
        this.walkAnimation.applyWalk(p_364104_.walkAnimationPos, p_364104_.walkAnimationSpeed, 12.0F, 100.0F);
    }
}