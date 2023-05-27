package bagu_chan.bagus_lib.client.camera;

import bagu_chan.bagus_lib.BagusLib;
import bagu_chan.bagus_lib.message.CameraMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Mod.EventBusSubscriber(modid = BagusLib.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CameraEvent {
    public static List<CameraHolder> cameraHolderList = Lists.newArrayList();

    @SubscribeEvent
    public static void cameraEvent(ViewportEvent.ComputeCameraAngles event) {
        for (int i = 0; i < cameraHolderList.size(); i++) {
            CameraHolder cameraHolder = cameraHolderList.get(i);
            if (cameraHolder.getDuration() <= cameraHolder.time) {
                cameraHolderList.remove(cameraHolder);
            }else {
                cameraHolder.tick(event);
            }

        }
    }

    public static List<CameraHolder> getCameraHolderList() {
        return cameraHolderList;
    }

    public static void addCameraHolderList(Level level, CameraHolder cameraHolder) {
        for (Player player : level.players()) {
            if (level.isClientSide()) {
                if (player.level.dimension() == cameraHolder.getPos().dimension() && player.blockPosition().distSqr(cameraHolder.getPos().pos()) < cameraHolder.amount * cameraHolder.amount) {
                    cameraHolderList.add(cameraHolder);
                }
            } else {
                if (player instanceof ServerPlayer serverPlayer) {
                    BagusLib.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new CameraMessage(cameraHolder.duration, cameraHolder.amount, cameraHolder.getPos()));
                }
            }
        }
    }
}
