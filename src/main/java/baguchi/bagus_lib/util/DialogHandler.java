package baguchi.bagus_lib.util;

import baguchi.bagus_lib.client.dialog.DialogType;
import baguchi.bagus_lib.message.DialogMessage;
import baguchi.bagus_lib.register.DialogRegister;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.network.PacketDistributor;
import org.joml.Matrix3x2fStack;

import java.util.Map;
import java.util.Optional;

//direct port from Minecraft 24w14potato
public class DialogHandler {
    public static final DialogHandler INSTANCE = new DialogHandler();


    public final Map<String, DialogType> dialogTypes = Maps.newHashMap();

    
    public void renderDialogue(GuiGraphics guiGraphics, float f, float tickCount) {
        Minecraft minecraft = Minecraft.getInstance();
        float g = tickCount + f;
        Matrix3x2fStack poseStack = guiGraphics.pose();
        int y = 14;
        for (Map.Entry<String, DialogType> dialogue : dialogTypes.entrySet()) {
            DialogType dialogType = dialogue.getValue();

            poseStack.pushMatrix();
            dialogType.render(guiGraphics, poseStack, f, tickCount, y);
            poseStack.popMatrix();
            poseStack.pushMatrix();
            dialogType.renderText(guiGraphics, poseStack, f, tickCount, y);
            poseStack.popMatrix();
            y += 20;
        }

        if (minecraft.level != null) {
            for (Map.Entry<String, DialogType> dialogue : dialogTypes.entrySet()) {
                DialogType dialogType = dialogue.getValue();
                if (dialogType.getNextDialogOption().dialogRenderTime() > 0) {
                    if (dialogType.getLastDialogRenderTime() < minecraft.level.getGameTime()) {
                        if (dialogType.getNextDialogOption().nextDialogLocation().isPresent()) {
                            Optional<DialogType> optional = minecraft.level.registryAccess().lookupOrThrow(DialogRegister.REGISTRY_KEY).getOptional(dialogType.getNextDialogOption().nextDialogLocation().get());
                            optional.ifPresent(type -> {
                                type.setLastDialogRenderTime(minecraft.level.getGameTime() + type.getNextDialogOption().dialogRenderTime());
                                dialogTypes.put(dialogue.getKey(), type);
                            });
                        } else {
                            dialogTypes.remove(dialogue.getKey());
                        }
                    }
                }
            }
        }
    }


    @Deprecated
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

    public static void addOrReplaceDialogTypeOnServer(ServerPlayer player, String name, DialogType dialogType) {
        PacketDistributor.sendToPlayer(player, new DialogMessage(name, dialogType));
    }


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
                for (l = Math.min(this.subString.length() + k, this.targetString.length()); l >= 0 && l < this.targetString.length() && Character.isWhitespace(this.targetString.charAt(l - 1)); ++l) {
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


        public interface DrawFunction {
            void apply(String var1, int var2, int var3);
        }
    }
}
