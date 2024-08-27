package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.layer.BagusLayer;
import bagu_chan.bagus_lib.client.layer.IArmor;
import bagu_chan.bagus_lib.client.overlay.DialogOverlay;
import bagu_chan.bagus_lib.register.ModEntities;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

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
    public static void registerShaders(final RegisterShadersEvent event) {
        try {
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "rendertype_animate_eyes"), DefaultVertexFormat.NEW_ENTITY), BagusShaders::setRenderTypeAnimateEyeShader);
            event.registerShader(new ShaderInstance(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "rendertype_animate_entity_cutout"), DefaultVertexFormat.NEW_ENTITY), BagusShaders::setRenderTypeAnimateEntityShader);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
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
    public static void overlayRegister(RegisterGuiLayersEvent event) {
        event.registerAboveAll(ResourceLocation.fromNamespaceAndPath(BagusLib.MODID, "dialog"), new DialogOverlay());
    }
}