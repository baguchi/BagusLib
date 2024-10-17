package baguchi.bagus_lib.client;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.layer.BagusLayer;
import baguchi.bagus_lib.client.layer.IArmor;
import baguchi.bagus_lib.client.overlay.DialogOverlay;
import baguchi.bagus_lib.client.render.MiniBaguModel;
import baguchi.bagus_lib.client.render.MiniBaguRenderer;
import baguchi.bagus_lib.register.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = BagusLib.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientRegistrar {
    @SubscribeEvent
    public static void registerEntityRenders(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.MINI_BAGU.get(), MiniBaguRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.MINI_BAGU, MiniBaguModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerEntityLayer(EntityRenderersEvent.AddLayers event) {
        Minecraft.getInstance().getEntityRenderDispatcher().getSkinMap().values().forEach(r -> {
            if (r instanceof LivingEntityRenderer<?, ?, ?> livingEntityRenderer) {
                if (livingEntityRenderer.getModel() instanceof IArmor armor) {
                    ((LivingEntityRenderer) r).addLayer(new BagusLayer(((LivingEntityRenderer) r), event.getEntityModels(), Minecraft.getInstance().getModelManager()));
                }
            }
        });
    }

    @SubscribeEvent
    public static void overlayRegister(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "dialog"), new DialogOverlay());
    }
}