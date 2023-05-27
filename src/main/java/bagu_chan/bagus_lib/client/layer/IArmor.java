package bagu_chan.bagus_lib.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.HumanoidArm;

public interface IArmor {

    void translateToHead(PoseStack poseStack);

    void translateToChest(PoseStack poseStack);

    void translateToLeg(ModelPart part, PoseStack poseStack);

    void translateToChestPat(HumanoidArm arm, PoseStack poseStack);

    ModelPart rightHand();

    ModelPart leftHand();

    ModelPart rightLeg();

    ModelPart leftLeg();
}
