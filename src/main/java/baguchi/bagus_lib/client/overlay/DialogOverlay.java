package baguchi.bagus_lib.client.overlay;

import baguchi.bagus_lib.util.DialogHandler;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.WinScreen;
import net.neoforged.neoforge.client.gui.GuiLayer;

public class DialogOverlay implements GuiLayer {

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (!(Minecraft.getInstance().screen instanceof WinScreen)) {
            DialogHandler.INSTANCE.renderDialogue(guiGraphics, deltaTracker.getGameTimeDeltaPartialTick(false), Minecraft.getInstance().gui.getGuiTicks());
        }
    }
}
