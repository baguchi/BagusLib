package bagu_chan.bagus_lib.client.layer;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelPart;

public interface IArmor {
    /*
     * This method translate for correct armor render. basically Armor
     * @author bagu_chan
     */

    void translateToHead(ModelPart part, PoseStack poseStack);

    void translateToChest(ModelPart part, PoseStack poseStack);

    void translateToLeg(ModelPart part, PoseStack poseStack);

    void translateToChestPat(ModelPart part, PoseStack poseStack);

    /*
     * if empty part. dosen't render
     */

    default Iterable<ModelPart> rightHands() {
        return ImmutableList.of();
    }

    default Iterable<ModelPart> leftHands() {
        return ImmutableList.of();
    }

    default Iterable<ModelPart> rightLegParts() {
        return ImmutableList.of();
    }

    default Iterable<ModelPart> leftLegParts() {
        return ImmutableList.of();
    }

    default Iterable<ModelPart> bodyParts() {
        return ImmutableList.of();
    }

    default Iterable<ModelPart> headParts() {
        return ImmutableList.of();
    }
}
