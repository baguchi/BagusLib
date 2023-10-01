package bagu_chan.bagus_lib.util;

import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class GlobalVec3ByteBuf {
    public static GlobalVec3 readGlobalVec(FriendlyByteBuf buf) {
        ResourceKey<Level> resourcekey = readResourceKey(buf, Registry.DIMENSION_REGISTRY);
        Vec3 blockpos = readVec3(buf);
        return GlobalVec3.of(resourcekey, blockpos);
    }

    public static void writeGlobalVec(FriendlyByteBuf buf, GlobalVec3 p_236815_) {
        writeResourceKey(buf, p_236815_.dimension());
        writeVec3(buf, p_236815_.pos());
    }

    public static <T> ResourceKey<T> readResourceKey(FriendlyByteBuf buf, ResourceKey<? extends Registry<T>> p_236802_) {
        ResourceLocation resourcelocation = buf.readResourceLocation();
        return ResourceKey.create(p_236802_, resourcelocation);
    }

    public static void writeResourceKey(FriendlyByteBuf buf, ResourceKey<?> p_236859_) {
        buf.writeResourceLocation(p_236859_.location());
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
