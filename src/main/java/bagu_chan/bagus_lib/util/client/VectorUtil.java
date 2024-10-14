package bagu_chan.bagus_lib.util.client;

import net.minecraft.client.model.geom.ModelPart;
import org.joml.Vector3f;

public class VectorUtil {
    public static Vector3f movePartToVec(ModelPart part) {
        return new Vector3f(part.xRot, part.yRot, part.zRot);
    }

    public static void moveVecToPart(Vector3f vector3f, ModelPart part) {
        part.xRot = vector3f.x;
        part.yRot = vector3f.y;
        part.zRot = vector3f.z;
    }

    public static void moveVecToPartWithAdd(Vector3f vector3f, ModelPart part) {
        part.xRot += vector3f.x;
        part.yRot += vector3f.y;
        part.zRot += vector3f.z;
    }
}
