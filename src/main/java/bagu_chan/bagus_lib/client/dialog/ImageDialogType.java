package bagu_chan.bagus_lib.client.dialog;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;


/*
 * If using this dialog. Image Path should be textures/gui/sprites/[filename]
 */
public class ImageDialogType extends DialogType {
    public static final MapCodec<ImageDialogType> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.STRING.fieldOf("dialog").forGetter(ImageDialogType::getDialogueBase),
                            DialogOption.CODEC.fieldOf("dialog_option").orElse(new DialogOption(1, 1, true, Optional.empty())).forGetter(ImageDialogType::getDialogueOption),
                            Codec.LONG.fieldOf("dialog_render_time").forGetter(ImageDialogType::getDialogRenderTime),
                            Codec.DOUBLE.fieldOf("draw_per_tick").forGetter(ImageDialogType::getDialogPerTick),
                            ResourceLocation.CODEC.optionalFieldOf("image").forGetter(ImageDialogType::getImage),
                            Codec.INT.fieldOf("texture_size_x").forGetter(ImageDialogType::getTextureSizeX),
                            Codec.INT.fieldOf("texture_size_y").forGetter(ImageDialogType::getTextureSizeY))
                    .apply(p_345644_, ImageDialogType::new)
    );

    protected final Optional<ResourceLocation> image;
    private final int textureSizeX;
    private final int textureSizeY;

    public ImageDialogType(String dialogueBase, DialogOption dialogueOption, long dialogRenderTime, double dialogPerTick, Optional<ResourceLocation> image, int textureSizeX, int textureSizeY) {
        super(dialogueBase, dialogueOption, dialogRenderTime, dialogPerTick);
        this.image = image;
        this.textureSizeX = textureSizeX;
        this.textureSizeY = textureSizeY;
    }


    @Override
    public void render(GuiGraphics guiGraphics, PoseStack poseStack, float f, float tickCount, int y) {
        if (this.image.isPresent()) {
            poseStack.pushPose();
            poseStack.translate(0, y, 0);
            poseStack.scale(this.dialogueOption.scaleX(), this.dialogueOption.scaleY(), 1.0f);
            guiGraphics.blitSprite(image.get(), 0, 0, this.textureSizeX, this.textureSizeY);
            poseStack.popPose();
        }
    }

    @Nullable
    public Optional<ResourceLocation> getImage() {
        return image;
    }

    public int getTextureSizeX() {
        return textureSizeX;
    }

    public int getTextureSizeY() {
        return textureSizeY;
    }

    @Override
    public MapCodec<ImageDialogType> codec() {
        return CODEC;
    }
}
