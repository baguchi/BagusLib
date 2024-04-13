package bagu_chan.bagus_lib.util;

import bagu_chan.bagus_lib.client.dialog.DialogType;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

//direct port from Minecraft 24w14potato
public class DialogHandler {
    public static final DialogHandler INSTANCE = new DialogHandler();


    public final Map<String, DialogType> dialogTypes = Maps.newHashMap();


    public void renderDialogue(GuiGraphics guiGraphics, float f, float tickCount) {
        Minecraft minecraft = Minecraft.getInstance();
        float g = (float) tickCount + f;
        PoseStack poseStack = guiGraphics.pose();
        for (DialogType dialogue : dialogTypes.values()) {
            poseStack.pushPose();
            dialogue.render(guiGraphics, poseStack, f, tickCount);
            poseStack.popPose();
            poseStack.pushPose();
            dialogue.renderText(guiGraphics, poseStack, f, tickCount);
            poseStack.popPose();
        }
    }


    public void addOrReplaceDialogType(String name, DialogType dialogType) {
        dialogTypes.remove(name);
        dialogTypes.put(name, dialogType);
    }

    public void removeDialogType(String name) {
        dialogTypes.remove(name);
    }


    public void removeAllDialogType() {
        dialogTypes.clear();
    }


    @OnlyIn(value = Dist.CLIENT)
    public static class DrawString {
        private final double charsPerTick;
        private final String targetString;
        private final DrawFunction drawFunction;
        private double lastTick;
        private String subString = "";
        private final boolean ignoreWhiteSpace;

        public DrawString(double d, double e, String string, DrawFunction drawFunction, boolean ignoreWhiteSpace) {
            this.lastTick = d;
            this.charsPerTick = e;
            this.targetString = string;
            this.drawFunction = drawFunction;
            this.ignoreWhiteSpace = ignoreWhiteSpace;
        }

        public DrawString(double d, double e, String string, DrawFunction drawFunction) {
            this.lastTick = d;
            this.charsPerTick = e;
            this.targetString = string;
            this.drawFunction = drawFunction;
            this.ignoreWhiteSpace = true;
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
            if (this.ignoreWhiteSpace) {
                for (l = Math.min(this.subString.length() + k, this.targetString.length()); l < this.targetString.length() && Character.isWhitespace(this.targetString.charAt(l - 1)); ++l) {
                }
            } else {
                l = Math.min(this.subString.length() + k, this.targetString.length());
            }
            if (l >= 0) {
                this.subString = this.targetString.substring(0, l);
                this.drawFunction.apply(this.subString, i, j);
            }
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
