package baguchi.bagus_lib.client;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.CommonEvent;
import baguchi.bagus_lib.client.animation.TestPlayerAnimations;
import baguchi.bagus_lib.client.event.RegisterBagusKeyframeEvents;
import baguchi.bagus_lib.client.overlay.DialogOverlay;
import baguchi.bagus_lib.client.render.MiniBaguModel;
import baguchi.bagus_lib.client.render.MiniBaguRenderer;
import baguchi.bagus_lib.register.ModEntities;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.resources.Identifier;
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
        event.registerAboveAll(Identifier.fromNamespaceAndPath(BagusLib.MODID, "dialog"), new DialogOverlay());
    }

    @SubscribeEvent
    public static void registerAnimation(RegisterBagusKeyframeEvents event) {
        if (event.getModel() instanceof HumanoidModel<?>) {
            event.addAnimationKeyframe(CommonEvent.PAT, TestPlayerAnimations.pat_right.bake(event.getModelPart()));
        }
    }
}