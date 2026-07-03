package baguchi.bagus_lib.client.camera;

import baguchi.bagus_lib.BagusLib;
import baguchi.bagus_lib.client.camera.shake.CameraShake;
import baguchi.bagus_lib.packet.CameraPacket;
import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

@EventBusSubscriber(modid = BagusLib.MODID, value = Dist.CLIENT)
public class CameraCore {
    public static List<CameraHolder> cameraHolders = Lists.newArrayList();

    @SubscribeEvent
    public static void cameraEvent(ViewportEvent.ComputeCameraAngles event) {
        for (int i = 0; i < cameraHolders.size(); i++) {
            CameraHolder cameraShake = cameraHolders.get(i);
            if (cameraShake.cameraShake().duration <= cameraShake.time()) {
                cameraHolders.remove(cameraShake);
            } else {
                cameraHolders.get(i).setTick(cameraShake.time() + 1);
            }

        }
    }

    public static List<CameraHolder> getCameraHolderList() {
        return cameraHolders;
    }

    public static void addCameraHolderList(Level level, CameraShake cameraShake) {
        if (level.isClientSide()) {
            if (level.dimension() == cameraShake.getPos().dimension()) {
                cameraHolders.add(new CameraHolder(0, cameraShake));
            }
        } else if (!level.isClientSide()) {
            for (Player player : level.players()) {
                if (player instanceof ServerPlayer serverPlayer) {
                    PacketDistributor.sendToPlayer(serverPlayer, new CameraPacket(cameraShake));
                }
            }
        }
    }
}
