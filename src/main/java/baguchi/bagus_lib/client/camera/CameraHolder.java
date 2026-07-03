package baguchi.bagus_lib.client.camera;

import baguchi.bagus_lib.client.camera.shake.CameraShake;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record CameraHolder(int time, CameraShake cameraShake) {
    public static final MapCodec<CameraHolder> CODEC = RecordCodecBuilder.mapCodec(
            p_345644_ -> p_345644_.group(Codec.INT.fieldOf("time").forGetter(CameraHolder::time),
                            CameraShake.CODEC.codec().fieldOf("camera_shake").forGetter(CameraHolder::cameraShake))
                    .apply(p_345644_, CameraHolder::new)
    );

    public CameraHolder setTick(int time) {
        return new CameraHolder(time, cameraShake);
    }
}
