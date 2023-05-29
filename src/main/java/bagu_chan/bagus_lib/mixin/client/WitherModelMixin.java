package bagu_chan.bagus_lib.mixin.client;

import bagu_chan.bagus_lib.client.layer.IArmor;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WitherBossModel;
import net.minecraft.client.model.geom.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WitherBossModel.class)
public class WitherModelMixin implements IArmor {
    @Shadow
    @Final
    private ModelPart root;
    @Shadow
    @Final
    private ModelPart centerHead;
    @Shadow
    @Final
    private ModelPart rightHead;
    @Shadow
    @Final
    private ModelPart leftHead;

    @Override
    public void translateToHead(ModelPart part, PoseStack poseStack) {
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
    public Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.centerHead, this.rightHead, this.leftHead);
    }
}
