package baguchi.bagus_lib.client;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.overlay.DialogOverlay;
import baguchi.bagus_lib.client.render.MiniBaguArmorModel;
import baguchi.bagus_lib.client.render.MiniBaguModel;
import baguchi.bagus_lib.client.render.MiniBaguRenderer;
import baguchi.bagus_lib.register.ModEntities;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;


@EventBusSubscriber(modid = BagusLib.MODID, value = Dist.CLIENT)
public class ClientRegistrar {
    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MINI_BAGU.get(), MiniBaguRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.MINI_BAGU, MiniBaguModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.MINI_BAGU_ARMOR, MiniBaguArmorModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityLayer(EntityRenderersEvent.AddLayers event) {
    }

    @SubscribeEvent
    public static void registerClientExtend(RegisterClientExtensionsEvent event) {
        //event.registerItem(new BagusArmorItem.ArmorRender(), ModItems.BAGU.get());
    }

    @SubscribeEvent
    public static void overlayRegister(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "dialog"), new DialogOverlay());
    }
}