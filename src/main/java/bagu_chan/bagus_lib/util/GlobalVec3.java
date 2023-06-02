package bagu_chan.bagus_lib.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;

public class GlobalVec3 {
    public static final Codec<GlobalVec3> CODEC = RecordCodecBuilder.create((p_122642_) -> {
        return p_122642_.group(Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(GlobalVec3::dimension), Vec3.CODEC.fieldOf("vec3").forGetter(GlobalVec3::pos)).apply(p_122642_, GlobalVec3::of);
    });
    private final ResourceKey<Level> dimension;
    private final Vec3 vec3;

    private GlobalVec3(ResourceKey<Level> p_122638_, Vec3 p_122639_) {
        this.dimension = p_122638_;
        this.vec3 = p_122639_;
    }

    public static GlobalVec3 of(ResourceKey<Level> p_122644_, Vec3 p_122645_) {
        return new GlobalVec3(p_122644_, p_122645_);
    }

    public ResourceKey<Level> dimension() {
        return this.dimension;
    }

    public Vec3 pos() {
        return this.vec3;
    }

    public boolean equals(Object p_122648_) {
        if (this == p_122648_) {
            return true;
        } else if (p_122648_ != null && this.getClass() == p_122648_.getClass()) {
            GlobalVec3 GlobalVec3 = (GlobalVec3) p_122648_;
            return Objects.equals(this.dimension, GlobalVec3.dimension) && Objects.equals(this.vec3, GlobalVec3.vec3);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(this.dimension, this.vec3);
    }

    public String toString() {
        return this.dimension + " " + this.vec3;
    }
}