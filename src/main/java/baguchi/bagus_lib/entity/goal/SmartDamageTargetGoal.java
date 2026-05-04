package baguchi.bagus_lib.entity.goal;

import baguchi.bagus_lib.entity.SmartHurtMob;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.phys.AABB;
import org.jspecify.annotations.Nullable;

import java.util.Iterator;
import java.util.List;

public class SmartDamageTargetGoal extends MostDamageTargetGoal {
    private boolean alertSameType;
    private final Class<?>[] toIgnoreDamage;
    private Class<?> @Nullable [] toIgnoreAlert;

    public SmartDamageTargetGoal(PathfinderMob mob, Class<?>... ignoreDamageFromTheseTypes) {
        super(mob, 1.0F);
        this.toIgnoreDamage = ignoreDamageFromTheseTypes;
    }

    public SmartDamageTargetGoal(PathfinderMob mob, float calmDownRate, Class<?>... ignoreDamageFromTheseTypes) {
        super(mob, calmDownRate);
        this.toIgnoreDamage = ignoreDamageFromTheseTypes;
    }

    @Override
    public boolean canUse() {
        int timestamp = this.mob.getLastHurtByMobTimestamp();
        LivingEntity lastHurtByMob = this.mob.getLastHurtByMob();
        if (timestamp != this.lastHurtTimestamp && lastHurtByMob != null) {
            if (lastHurtByMob.is(EntityType.PLAYER) && (Boolean) getServerLevel(this.mob).getGameRules().get(GameRules.UNIVERSAL_ANGER)) {
                return false;
            } else {
                for (Class<?> ignoreClass : this.toIgnoreDamage) {
                    if (ignoreClass.isAssignableFrom(lastHurtByMob.getClass())) {
                        return false;
                    }
                }

                return canUse();
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        if (this.alertSameType) {
            this.alertOthers();
        }

        super.start();
    }

    protected void alertOthers() {
        double within = this.getFollowDistance();
        AABB searchAabb = AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(within, (double) 10.0F, within);
        List<? extends Mob> nearby = this.mob.level().getEntitiesOfClass(this.mob.getClass(), searchAabb, EntitySelector.NO_SPECTATORS);
        Iterator var5 = nearby.iterator();

        while (true) {
            Mob other;
            while (true) {
                if (!var5.hasNext()) {
                    return;
                }

                other = (Mob) var5.next();
                if (this.mob != other && other.getTarget() == null && (!(this.mob instanceof TamableAnimal) || ((TamableAnimal) this.mob).getOwner() == ((TamableAnimal) other).getOwner()) && !other.isAlliedTo(this.mob.getLastHurtByMob())) {
                    if (this.toIgnoreAlert == null) {
                        break;
                    }

                    boolean ignore = false;

                    for (Class<?> ignoreClass : this.toIgnoreAlert) {
                        if (other.getClass() == ignoreClass) {
                            ignore = true;
                            break;
                        }
                    }

                    if (!ignore) {
                        break;
                    }
                }
            }

            this.alertOther(other, this.mob.getLastHurtByMob());
        }
    }

    public SmartDamageTargetGoal setAlertOthers(Class<?>... exceptTheseTypes) {
        this.alertSameType = true;
        this.toIgnoreAlert = exceptTheseTypes;
        return this;
    }


    protected void alertOther(Mob other, LivingEntity hurtByMob) {
        if (other instanceof SmartHurtMob smartHurtMob) {
            smartHurtMob.noticedByHurtAllies(hurtByMob);
        } else {
            other.setTarget(hurtByMob);
        }
    }
}
