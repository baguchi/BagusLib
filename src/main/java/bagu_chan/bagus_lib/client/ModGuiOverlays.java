package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.overlay.DialogOverlay;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;

public class ModGuiOverlays {
    public static final NamedGuiOverlay DIALOG = new NamedGuiOverlay(new ResourceLocation(BagusLib.MODID, "dialog"), new DialogOverlay());
}
