package bagu_chan.bagus_lib.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderUtils {
    public static void lineRendering(VertexConsumer vertexconsumer, Matrix4f matrix4f, Matrix3f matrix3f, float offsetX, float offsetY, float offsetZ, float x, float y, float z, float red, float green, float blue, float alpha) {
        vertexconsumer.vertex(matrix4f, offsetX, offsetY, offsetZ).color(red, green, blue, alpha).normal(matrix3f, 0, 0, 0).endVertex();
        vertexconsumer.vertex(matrix4f, x, y, z).color(red, green, blue, alpha).normal(matrix3f, 0, 0, 0).endVertex();
    }
}
