package bagu_chan.bagus_lib.util;

import bagu_chan.bagus_lib.client.dialog.DialogType;
import bagu_chan.bagus_lib.client.dialog.builder.DialogBuilder;
import bagu_chan.bagus_lib.message.DialogMessage;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Map;

//direct port from Minecraft 24w14potato
public class DialogHandler {
    public static final DialogHandler INSTANCE = new DialogHandler();


    public final Map<String, DialogType> dialogTypes = Maps.newHashMap();

    @OnlyIn(value = Dist.CLIENT)
    public void renderDialogue(GuiGraphics guiGraphics, float f, float tickCount) {
        Minecraft minecraft = Minecraft.getInstance();
        float g = tickCount + f;
        PoseStack poseStack = guiGraphics.pose();
        for (Map.Entry<String, DialogType> dialogue : dialogTypes.entrySet()) {
            DialogType dialogType = dialogue.getValue();

            poseStack.pushPose();
            dialogType.render(guiGraphics, poseStack, f, tickCount);
            poseStack.popPose();
            poseStack.pushPose();
            dialogType.renderText(guiGraphics, poseStack, f, tickCount);
            poseStack.popPose();
        }

        if (minecraft.level != null) {
            for (Map.Entry<String, DialogType> dialogue : dialogTypes.entrySet()) {
                DialogType dialogType = dialogue.getValue();
                if (dialogType.getDialogRenderTime() > 0) {
                    if (dialogType.getDialogRenderTime() < minecraft.level.getGameTime()) {
                        dialogTypes.remove(dialogue.getKey());
                    }
                }
            }
        }
    }

    @OnlyIn(value = Dist.CLIENT)
    public void addOrReplaceDialogType(String name, DialogType dialogType, DialogBuilder builder) {
        dialogTypes.remove(name);
        dialogTypes.put(name, dialogType.getClone(builder.writeTag()));
    }

    @OnlyIn(value = Dist.CLIENT)
    @Deprecated
    public void addOrReplaceDialogType(String name, DialogType dialogType) {
        dialogTypes.remove(name);
        dialogTypes.put(name, dialogType);
    }

    @OnlyIn(value = Dist.CLIENT)
    public void removeDialogType(String name) {
        dialogTypes.remove(name);
    }

    @OnlyIn(value = Dist.CLIENT)
    public void removeAllDialogType() {
        dialogTypes.clear();
    }

    public static void addOrReplaceDialogTypeOnServer(ServerPlayer player, String name, DialogType dialogType, DialogBuilder builder) {
        PacketDistributor.sendToPlayer(player, new DialogMessage(name, dialogType, builder.writeTag()));
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

        public DrawString(double lastTick, double charsPerTick, String string, DrawFunction drawFunction) {
            this.lastTick = lastTick;
            this.charsPerTick = charsPerTick;
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
            int k = Mth.floor((d - this.lastTick) * this.charsPerTick);
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
        public interface DrawFunction {
            void apply(String var1, int var2, int var3);
        }
    }
}
