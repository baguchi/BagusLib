package bagu_chan.bagus_lib.client.overlay;

import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.gui.screens.WinScreen;

public class DialogOverlay implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics guiGraphics, float f) {
        if (!(Minecraft.getInstance().screen instanceof WinScreen)) {
            DialogHandler.INSTANCE.renderDialogue(guiGraphics, f, Minecraft.getInstance().gui.getGuiTicks());
        }
    }
}
