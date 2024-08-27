package bagu_chan.bagus_lib.client;

import net.minecraft.client.renderer.ShaderInstance;
import org.jetbrains.annotations.Nullable;

public class BagusShaders {
    private static ShaderInstance renderTypeAnimateEyeShader;
    private static ShaderInstance renderTypeAnimateEntityShader;

    @Nullable
    public static ShaderInstance getRenderTypeAnimateEyeShader() {
        return renderTypeAnimateEyeShader;
    }

    public static void setRenderTypeAnimateEyeShader(ShaderInstance instance) {
        renderTypeAnimateEyeShader = instance;
    }

    @Nullable
    public static ShaderInstance getRenderTypeAnimateEntityShader() {
        return renderTypeAnimateEntityShader;
    }

    public static void setRenderTypeAnimateEntityShader(ShaderInstance instance) {
        renderTypeAnimateEntityShader = instance;
    }
}