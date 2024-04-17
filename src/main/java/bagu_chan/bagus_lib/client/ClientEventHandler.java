package bagu_chan.bagus_lib.client;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.util.DialogHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = BagusLib.MODID)
public class ClientEventHandler {
    private static boolean initDate = false;

    private static boolean aprilFools = false;


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
