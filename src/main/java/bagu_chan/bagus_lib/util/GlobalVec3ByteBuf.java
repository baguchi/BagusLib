package bagu_chan.bagus_lib.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GlobalVec3ByteBuf {
    public static GlobalVec3 readGlobalPos(FriendlyByteBuf buf) {
        ResourceKey<Level> resourcekey = buf.readResourceKey(Registries.DIMENSION);
        Vec3 blockpos = readVec3(buf);
        return GlobalVec3.of(resourcekey, blockpos);
    }

    public static void writeGlobalPos(FriendlyByteBuf buf, GlobalVec3 p_236815_) {
        buf.writeResourceKey(p_236815_.dimension());
        writeVec3(buf, p_236815_.pos());
    }


    public static Vec3 readVec3(FriendlyByteBuf buf) {
        return new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    public static void writeVec3(FriendlyByteBuf buf, Vec3 p_270985_) {
        buf.writeDouble(p_270985_.x());
        buf.writeDouble(p_270985_.y());
        buf.writeDouble(p_270985_.z());
    }
}
