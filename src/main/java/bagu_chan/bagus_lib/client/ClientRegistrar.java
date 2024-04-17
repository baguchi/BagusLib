package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.layer.BagusLayer;
import bagu_chan.bagus_lib.client.layer.IArmor;
import bagu_chan.bagus_lib.register.ModEntities;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = BagusLib.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
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
            if (r instanceof LivingEntityRenderer<?, ?> livingEntityRenderer) {
                if (livingEntityRenderer.getModel() instanceof IArmor armor) {
                    ((LivingEntityRenderer) r).addLayer(new BagusLayer(((LivingEntityRenderer) r), event.getEntityModels(), Minecraft.getInstance().getModelManager()));
                }
            }
        });
    }

    @SubscribeEvent
    public void onPostRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay() == VanillaGuiOverlay.VIGNETTE.type()) {
            DialogHandler.INSTANCE.renderDialogue(event.getGuiGraphics(), Minecraft.getInstance().getPartialTick(), 0);

        }
    }

}
