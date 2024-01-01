package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusConfigs;
import bagu_chan.bagus_lib.client.game.WaterMelonScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ScreenEvent;

import java.util.Calendar;
import java.util.Date;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
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
}
