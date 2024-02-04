package bagu_chan.bagus_lib.mixin.client;

import bagu_chan.bagus_lib.client.layer.IArmor;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(PlayerModel.class)
public abstract class PlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> implements IArmor {

    public PlayerModelMixin(ModelPart p_170679_) {
        super(p_170679_);
    }

    @Override
    public void translateToHead(ModelPart part, PoseStack poseStack) {
        part.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChest(ModelPart part, PoseStack poseStack) {
        part.translateAndRotate(poseStack);
    }

    @Override
    public void translateToLeg(ModelPart part, PoseStack poseStack) {
        part.translateAndRotate(poseStack);
    }

    @Override
    public void translateToChestPat(ModelPart part, PoseStack poseStack) {
        part.translateAndRotate(poseStack);
    }

    public Iterable<ModelPart> rightHandArmors() {
        return ImmutableList.of(this.rightArm);
    }

    public Iterable<ModelPart> leftHandArmors() {
        return ImmutableList.of(this.leftArm);
    }

    public Iterable<ModelPart> rightLegPartArmors() {
        return ImmutableList.of(this.rightLeg);
    }

    public Iterable<ModelPart> leftLegPartArmors() {
        return ImmutableList.of(this.leftLeg);
    }

    public Iterable<ModelPart> bodyPartArmors() {
        return ImmutableList.of(this.body);
    }

    public Iterable<ModelPart> headPartArmors() {
        return ImmutableList.of(this.head);
    }
}
