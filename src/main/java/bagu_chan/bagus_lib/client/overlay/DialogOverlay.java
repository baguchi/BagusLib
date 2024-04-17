package bagu_chan.bagus_lib.client.overlay;

import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class DialogOverlay implements IGuiOverlay {
    @Override
    public void render(ForgeGui extendedGui, GuiGraphics guiGraphics, float v, int i, int i1) {
        if (!(Minecraft.getInstance().screen instanceof WinScreen)) {
            DialogHandler.INSTANCE.renderDialogue(guiGraphics, Minecraft.getInstance().getPartialTick(), extendedGui.getGuiTicks());
        }
    }
}
