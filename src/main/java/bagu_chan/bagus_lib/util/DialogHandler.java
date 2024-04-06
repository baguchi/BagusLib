package bagu_chan.bagus_lib.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

//direct port from Minecraft 24w14potato
public class DialogHandler {
    private static final Vector3f TRANSLATION = new Vector3f();

    public static final DialogHandler INSTANCE = new DialogHandler();
    @Nullable
    private DrawString dialogue;
    @Nullable
    private MutableComponent dialogueBase;
    @Nullable
    private Holder<SoundEvent> soundEvent;
    @Nullable
    private ResourceLocation resourceLocation;
   /* @Nullable
    private EntityType<?> entityType;
    @Nullable
    private Entity entityPreview;
    @Nullable
    private EntityType<?> oldEntityType;*/

    private int sizeX = 16;
    private int sizeY = 16;
    private float scaleX = 1;
    private float scaleY = 1;
    private int posX = 1;
    private int posY = 1;

    public void renderDialogue(GuiGraphics guiGraphics, float f, float tickCount) {
        Minecraft minecraft = Minecraft.getInstance();
        float g = (float) tickCount + f;
        PoseStack poseStack = guiGraphics.pose();
        Font font = minecraft.font;
        ItemStack itemStack2 = new ItemStack(Items.POISONOUS_POTATO);
        if (this.dialogue == null && this.dialogueBase != null) {
            MutableComponent component = dialogueBase;
            this.dialogue = beginString(guiGraphics, g, 2.0, font, component.getString(), 0xFFFFFF, guiGraphics.guiWidth() - 72);
        }
        /*if(this.entityPreview == null && this.entityType != null || this.entityType != this.oldEntityType){
            this.entityPreview = this.entityType.create(Minecraft.getInstance().level);
            this.oldEntityType = this.entityType;
            this.entityPreview.setYRot(0F);
            this.entityPreview.setYBodyRot(0F);
            this.entityPreview.setYHeadRot(0F);
        }*/
        if (this.dialogue == null) {
            return;
        }
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        guiGraphics.fill(0, 0, guiGraphics.guiWidth(), 32, 0, 0x40000000);
        guiGraphics.fillGradient(0, 32, guiGraphics.guiWidth(), 64, 0x40000000, 0);

        /*if(entityPreview instanceof LivingEntity livingEntity){
            poseStack.pushPose();
            poseStack.scale(this.scaleX, this.scaleY, 1.0f);
            InventoryScreen.renderEntityInInventory(guiGraphics, this.posX, this.posY, 25, TRANSLATION, new Quaternionf().rotationXYZ((float) Math.PI, (float) Math.PI, 0F), null, livingEntity);
            poseStack.popPose();
        }else
        */
        if (this.resourceLocation != null) {
            poseStack.pushPose();
            poseStack.scale(this.scaleX, this.scaleY, 1.0f);
            guiGraphics.blitSprite(resourceLocation, this.posX, this.posY, this.sizeX, this.sizeY);
            poseStack.popPose();
        }
        poseStack.pushPose();
        if (this.dialogue != null && this.dialogue.draw(g, 72, 16)) {
            if (this.soundEvent != null) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(soundEvent.value(), 1.0F, 0.75F));
            }
        }
        poseStack.popPose();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(true);

    }

    public void setDialogue(@Nullable MutableComponent dialogueBase, @Nullable Holder<SoundEvent> soundEvent, @Nullable ResourceLocation resourceLocation, int sizeX, int sizey) {
        this.dialogueBase = dialogueBase;
        this.soundEvent = soundEvent;
        this.dialogue = null;
        this.resourceLocation = resourceLocation;
        this.sizeX = sizeX;
        this.sizeY = sizey;
    }

    public void setDialogue(@Nullable MutableComponent dialogueBase, @Nullable ResourceLocation resourceLocation, int sizex, int sizey) {
        this.dialogueBase = dialogueBase;
        this.soundEvent = SoundEvents.NOTE_BLOCK_FLUTE;
        this.dialogue = null;
        this.resourceLocation = resourceLocation;
        this.sizeX = sizex;
        this.sizeY = sizey;
    }

    public void setDialogue(@Nullable MutableComponent dialogueBase) {
        this.dialogueBase = dialogueBase;
        this.soundEvent = SoundEvents.NOTE_BLOCK_FLUTE;
        this.dialogue = null;
        this.resourceLocation = null;
    }

    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    public void setPos(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
    }

    /*public void setEntityType(@Nullable EntityType<?> entityType) {
        this.entityType = entityType;
    }

    public void setEntityPreview(@Nullable Entity entityPreview) {
        this.entityPreview = entityPreview;
    }*/

    @OnlyIn(value = Dist.CLIENT)
    public static class DrawString {
        private final double charsPerTick;
        private final String targetString;
        private final DrawFunction drawFunction;
        private double lastTick;
        private String subString = "";

        DrawString(double d, double e, String string, DrawFunction drawFunction) {
            this.lastTick = d;
            this.charsPerTick = e;
            this.targetString = string;
            this.drawFunction = drawFunction;
        }

        public boolean draw(double d, int i, int j) {
            int l;
            if (this.targetString.equals(this.subString)) {
                this.drawFunction.apply(this.targetString, i, j);
                return false;
            }
            int k = Mth.floor((double) ((d - this.lastTick) * this.charsPerTick));
            if (k == 0) {
                this.drawFunction.apply(this.subString, i, j);
                return false;
            }
            for (l = Math.min(this.subString.length() + k, this.targetString.length()); l < this.targetString.length() && Character.isWhitespace(this.targetString.charAt(l - 1)); ++l) {
            }
            this.subString = this.targetString.substring(0, l);
            this.drawFunction.apply(this.subString, i, j);
            this.lastTick = d;
            return true;
        }

        public double getLastTick() {
            return this.lastTick;
        }

        @OnlyIn(value = Dist.CLIENT)
        public static interface DrawFunction {
            public void apply(String var1, int var2, int var3);
        }
    }

    public DrawString beginString(GuiGraphics guiGraphics, double d, double e, Font font, String string2, int i, int j2) {
        List<FormattedText> list = font.getSplitter().splitLines(string2, j2, Style.EMPTY);
        String string22 = list.stream().map(FormattedText::getString).collect(Collectors.joining("\n"));
        return new DrawString(d, e, string22, (string, j, k) -> {
            String[] strings = string.split("\\r?\\n");
            int l = k;
            for (String string3 : strings) {
                guiGraphics.drawString(font, string3, j, l, i);
                l += font.lineHeight + 4;
            }
        });
    }
}
