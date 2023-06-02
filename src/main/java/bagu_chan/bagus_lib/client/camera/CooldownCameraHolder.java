package bagu_chan.bagus_lib.client.camera;

import bagu_chan.bagus_lib.util.GlobalVec3;
import net.minecraftforge.client.event.ViewportEvent;

public class CooldownCameraHolder extends CameraHolder {
    public int cooldownTick;

    public CooldownCameraHolder(int amount, int duration, GlobalVec3 pos, int cooldownTick) {
        super(amount, duration, pos);
        this.cooldownTick = cooldownTick;
    }

    public int getCooldownTick() {
        return cooldownTick;
    }

    public void tick(ViewportEvent.ComputeCameraAngles event) {
        if (--this.cooldownTick < 0) {
            super.tick(event);
        }
    }
}
