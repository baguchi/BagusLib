package bagu_chan.bagus_lib.mixin.client;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.client.dialog.WinDialogType;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(WinScreen.class)
public abstract class WinScreenMixin extends Screen {
    @Shadow
    private float scroll;
    @Shadow
    private float scrollSpeed;
    @Unique
    public int bagusLib$lineIndex = -1;
    @Unique
    public int bagusLib$talkTimer = 0;
    @Unique
    public int bagusLib$ticks = 0;


    private List<MutableComponent> talkLines;

    protected WinScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "init", at = @At("HEAD"))
    protected void init(CallbackInfo callbackInfo) {
        bagusLib$lineIndex = -1;
        bagusLib$talkTimer = 200;
        DialogHandler.INSTANCE.removeAllDialogType();
        talkLines = new ArrayList<MutableComponent>();
    }

    @Inject(method = "render", at = @At("HEAD"))
    protected void render(GuiGraphics p_281907_, int p_282364_, int p_282696_, float p_281316_, CallbackInfo callbackInfo) {
        if (BagusConfigs.CLIENT.coolerEndPoem.get()) {
            DialogHandler.INSTANCE.renderDialogue(p_281907_, Minecraft.getInstance().getTimer().getGameTimeDeltaTicks(), bagusLib$ticks);
            int j = this.height + 50;
            float f = this.scroll;

            if (!talkLines.isEmpty()) {

                if (bagusLib$talkTimer <= 0) {
                    bagusLib$lineIndex++;
                    if (bagusLib$lineIndex >= 0 && bagusLib$lineIndex < this.talkLines.size()) {
                        MutableComponent chat = this.talkLines.get(bagusLib$lineIndex);
                        WinDialogType dialogType = new WinDialogType();
                        dialogType.setRenderDialogY(16);
                        dialogType.setDialogueBase(chat.copy());
                        DialogHandler.INSTANCE.addOrReplaceDialogType("Something", dialogType);
                        if (chat.getString().length() <= 0) {
                            bagusLib$talkTimer = 10;
                        } else if (bagusLib$lineIndex == this.talkLines.size() - 2) {
                            bagusLib$talkTimer = (int) (60 + (chat.getString().length() * 1.5F));
                        } else {
                            bagusLib$talkTimer = (int) (60 + (chat.getString().length() * 1.5F));
                        }
                    } else {
                        bagusLib$talkTimer = 100;
                    }

                }
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo callbackInfo) {
        if (BagusConfigs.CLIENT.coolerEndPoem.get()) {
            bagusLib$talkTimer -= (int) 1;
            bagusLib$ticks++;
        }
    }

    @Inject(method = "addPoemLines", at = @At("HEAD"), cancellable = true)
    private void addPoemLines(String p_181398_, CallbackInfo ci) {
        if (BagusConfigs.CLIENT.coolerEndPoem.get()) {
            this.talkLines.add(Component.literal(p_181398_));
            ci.cancel();
        }
    }

}