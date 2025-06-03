package baguchi.bagus_lib.client.overlay;

import baguchi.bagus_lib.util.DialogHandler;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.WinScreen;
import net.neoforged.neoforge.client.gui.GuiLayerManager;

public class DialogOverlay implements GuiLayerManager.Layer {

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!(Minecraft.getInstance().screen instanceof WinScreen)) {
            DialogHandler.INSTANCE.renderDialogue(guiGraphics, deltaTracker.getGameTimeDeltaTicks(), Minecraft.getInstance().gui.getGuiTicks());
        }
    }
}
