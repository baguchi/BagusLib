package baguchi.bagus_lib.client;

import baguchi.bagus_lib.BagusLib;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static ModelLayerLocation MINI_BAGU = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "mini_bagu"), "main");
    public static ModelLayerLocation MINI_BAGU_ARMOR = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "mini_bagu_armor"), "main");

}
