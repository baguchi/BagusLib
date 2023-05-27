package bagu_chan.bagus_lib.client.camera;

import bagu_chan.bagus_lib.BagusLib;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

@Mod.EventBusSubscriber(modid = BagusLib.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CameraEvent {
    public static List<CameraHolder> cameraHolderList = Lists.newArrayList();

    @SubscribeEvent
    public static void cameraEvent(ViewportEvent.ComputeCameraAngles event) {
        for (int i = 0; i < cameraHolderList.size(); i++) {
            CameraHolder cameraHolder = cameraHolderList.get(i);
            if(cameraHolder.getPos().dimension() != event.getCamera().getEntity().level.dimension()){
                cameraHolderList.remove(cameraHolder);
            }
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

    public static void addCameraHolderList(CameraHolder cameraHolder) {
        cameraHolderList.add(cameraHolder);
    }
}
