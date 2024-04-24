package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.client.game.WaterMelonScreen;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Calendar;
import java.util.Date;

@EventBusSubscriber(value = Dist.CLIENT, modid = BagusLib.MODID)
public class ClientEventHandler {
    private static boolean initDate = false;

    private static boolean aprilFools = false;

    @SubscribeEvent
    public static void screenRender(ScreenEvent.Init.Post event) {
        if (event.getScreen() instanceof TitleScreen titleScreen && isAprilFools()) {
            int l = titleScreen.height / 4 + 48;
            event.addListener(Button.builder(Component.translatable("bagus_lib.watermelon"), p_280785_ -> Minecraft.getInstance().setScreen(new WaterMelonScreen(Component.empty())))
                    .bounds(titleScreen.width / 2 - 100, l - 24, 100, 20)
                    .build());
        }
    }

    public static boolean isAprilFools() {
        if (!initDate) {
            initDate = true;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            aprilFools = calendar.get(Calendar.MONTH) + 1 == 4 && calendar.get(Calendar.DATE) == 1;
        }
        return aprilFools && BagusConfigs.COMMON.aprilFool.get();
    }

    @SubscribeEvent
    public static void clientLoggOut(PlayerEvent.PlayerLoggedOutEvent event) {
        DialogHandler.INSTANCE.removeAllDialogType();
    }

    @SubscribeEvent
    public static void clientRespawn(PlayerEvent.PlayerRespawnEvent event) {
        DialogHandler.INSTANCE.removeAllDialogType();
    }

    /*@SubscribeEvent
    public static void clientTick(TickEvent.PlayerTickEvent event) {
        if (Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.tickCount == 20) {
                ImageDialogType dialogType = new ImageDialogType();
                dialogType.setDialogueBase(Component.literal("Hmm... something different"));
                dialogType.setSize(280, 336);
                dialogType.setScale(0.25F, 0.25F);
                dialogType.setResourceLocation(new ResourceLocation(BagusLib.MODID, "foresight"));
                DialogHandler.INSTANCE.addOrReplaceDialogType("Host", dialogType);
            }
        }
    }*/
}
