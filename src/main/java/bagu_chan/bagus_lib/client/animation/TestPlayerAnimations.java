package bagu_chan.bagus_lib.client.animation;// Save this class in your mod and generate all required imports

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;

/**
 * Made with Blockbench 4.11.1
 * Exported for Minecraft version 1.19 or later with Mojang mappings
 *
 * @author Author
 */
public class TestPlayerAnimations {
    public static final AnimationDefinition pat_right = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("right_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-61.0146F, -17.6605F, -9.5401F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-61.0146F, 17.6605F, 9.5401F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-61.0146F, -17.6605F, -9.5401F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();

    public static final AnimationDefinition pat_left = AnimationDefinition.Builder.withLength(1.0F).looping()
            .addAnimation("left_arm", new AnimationChannel(AnimationChannel.Targets.ROTATION,
                    new Keyframe(0.0F, KeyframeAnimations.degreeVec(-61.0146F, 17.6605F, 9.5401F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(0.5F, KeyframeAnimations.degreeVec(-61.0146F, -17.6605F, -9.5401F), AnimationChannel.Interpolations.CATMULLROM),
                    new Keyframe(1.0F, KeyframeAnimations.degreeVec(-61.0146F, 17.6605F, 9.5401F), AnimationChannel.Interpolations.CATMULLROM)
            ))
            .build();
}