package bagu_chan.bagus_lib.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;

public interface IArmor {

    void translateToHead(ModelPart part, PoseStack poseStack);

    void translateToChest(ModelPart part, PoseStack poseStack);

    void translateToLeg(ModelPart part, PoseStack poseStack);

    void translateToChestPat(ModelPart part, PoseStack poseStack);

    Iterable<ModelPart> rightHands();

    Iterable<ModelPart> leftHands();

    Iterable<ModelPart> rightLegParts();

    Iterable<ModelPart> leftLegParts();

    Iterable<ModelPart> bodyParts();

    Iterable<ModelPart> headParts();
}
