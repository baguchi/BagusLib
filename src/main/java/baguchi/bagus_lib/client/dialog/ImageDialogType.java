package baguchi.bagus_lib.client.dialog;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2fStack;

import javax.annotation.Nullable;
import java.util.Optional;


/*
 * If using this dialog. Image Path should be textures/gui/sprites/[filename]
 */
public class ImageDialogType extends DialogType {
    public static final MapCodec<ImageDialogType> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.STRING.fieldOf("dialog").forGetter(ImageDialogType::getDialogueBase),
                            DialogEffectOption.CODEC.fieldOf("dialog_option").orElse(new DialogEffectOption(1, 1, true, Optional.empty())).forGetter(ImageDialogType::getDialogueOption),
                            NextDialogOption.CODEC.fieldOf("next_dialog").orElse(new NextDialogOption(Optional.empty(), -1)).forGetter(DialogType::getNextDialogOption),
                            Codec.DOUBLE.fieldOf("draw_per_tick").forGetter(ImageDialogType::getDialogPerTick),
                            Identifier.CODEC.optionalFieldOf("image").forGetter(ImageDialogType::getImage),
                            Codec.INT.fieldOf("texture_size_x").forGetter(ImageDialogType::getTextureSizeX),
                            Codec.INT.fieldOf("texture_size_y").forGetter(ImageDialogType::getTextureSizeY))
                    .apply(p_345644_, ImageDialogType::new)
    );

    protected final Optional<Identifier> image;
    private final int textureSizeX;
    private final int textureSizeY;

    public ImageDialogType(String dialogueBase, DialogEffectOption dialogueOption, NextDialogOption nextDialogOption, double dialogPerTick, Optional<Identifier> image, int textureSizeX, int textureSizeY) {
        super(dialogueBase, dialogueOption, nextDialogOption, dialogPerTick);
        this.image = image;
        this.textureSizeX = textureSizeX;
        this.textureSizeY = textureSizeY;
    }


    @Override
    public void render(GuiGraphicsExtractor guiGraphics, Matrix3x2fStack poseStack, float f, float tickCount, int y) {
        if (this.image.isPresent()) {
            poseStack.pushMatrix();
            poseStack.translate(0, y);
            poseStack.scale(this.dialogueOption.scaleX(), this.dialogueOption.scaleY());
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, image.get(), 0, 0, this.textureSizeX, this.textureSizeY);
            poseStack.popMatrix();
        }
    }

    @Nullable
    public Optional<Identifier> getImage() {
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
