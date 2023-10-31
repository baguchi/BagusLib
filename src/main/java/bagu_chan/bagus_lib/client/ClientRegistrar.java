package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.layer.BagusLayer;
import bagu_chan.bagus_lib.client.layer.IArmor;
import bagu_chan.bagus_lib.register.ModEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@OnlyIn(Dist.CLIENT)
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

}
