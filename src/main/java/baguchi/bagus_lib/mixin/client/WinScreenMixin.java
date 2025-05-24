package baguchi.bagus_lib.mixin.client;

import baguchi.bagus_lib.BagusConfigs;
import baguchi.bagus_lib.client.dialog.DialogEffectOption;
import baguchi.bagus_lib.client.dialog.NextDialogOption;
import baguchi.bagus_lib.client.dialog.WinDialogType;
import baguchi.bagus_lib.util.DialogHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(WinScreen.class)
public abstract class WinScreenMixin extends Screen {
    @Unique
    public DialogHandler bagusLib$INSTANCE;

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


    private List<String> talkLines;

    protected WinScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Inject(method = "init", at = @At("HEAD"))
    protected void init(CallbackInfo callbackInfo) {
        bagusLib$lineIndex = -1;
        bagusLib$talkTimer = 200;
        DialogHandler.INSTANCE.removeAllDialogType();
        bagusLib$INSTANCE = new DialogHandler();

        talkLines = new ArrayList<>();
    }

    @Inject(method = "render", at = @At("HEAD"))
    protected void render(GuiGraphics p_281907_, int p_282364_, int p_282696_, float p_281316_, CallbackInfo callbackInfo) {
        if (BagusConfigs.CLIENT.coolerEndPoem.get()) {
            bagusLib$INSTANCE.renderDialogue(p_281907_, Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaTicks(), bagusLib$ticks);
            int j = this.height + 50;
            float f = this.scroll;

            if (!talkLines.isEmpty()) {

                if (bagusLib$talkTimer <= 0) {
                    bagusLib$lineIndex++;
                    if (bagusLib$lineIndex >= 0 && bagusLib$lineIndex < this.talkLines.size()) {
                        String chat = this.talkLines.get(bagusLib$lineIndex);
                        WinDialogType dialogType = new WinDialogType(chat, new DialogEffectOption(1, 1, false, Optional.empty()), new NextDialogOption(Optional.empty(), 300), 3);
                        bagusLib$INSTANCE.addOrReplaceDialogType("Something", dialogType);
                        if (chat.length() <= 0) {
                            bagusLib$talkTimer = 10;
                        } else if (bagusLib$lineIndex == this.talkLines.size() - 2) {
                            bagusLib$talkTimer = (int) (60 + (chat.length() * 1.5F));
                        } else {
                            bagusLib$talkTimer = (int) (60 + (chat.length() * 1.5F));
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
            bagusLib$talkTimer -= 1;
            bagusLib$ticks++;
        }
    }

    @Inject(method = "addPoemLines", at = @At("HEAD"), cancellable = true)
    private void addPoemLines(String p_181398_, CallbackInfo ci) {
        if (BagusConfigs.CLIENT.coolerEndPoem.get()) {
            this.talkLines.add(p_181398_);
            ci.cancel();
        }
    }

}