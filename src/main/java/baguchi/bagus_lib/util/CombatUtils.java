package baguchi.bagus_lib.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class CombatUtils {
    public static List<Entity> getTotalEntityHitResult(Level p_150176_, Entity p_150177_, Vec3 p_150178_, Vec3 p_150179_, AABB p_150180_, Predicate<Entity> p_150181_, float p_150182_) {
        List<Entity> list = Lists.newArrayList();

        for (Entity entity1 : p_150176_.getEntities(p_150177_, p_150180_, p_150181_)) {
            AABB aabb = entity1.getBoundingBox().inflate((double) p_150182_);
            Optional<Vec3> optional1 = aabb.clip(p_150178_, p_150179_);
            if (optional1.isPresent()) {
                list.add(entity1);
            }
        }

        return list;
    }
}
