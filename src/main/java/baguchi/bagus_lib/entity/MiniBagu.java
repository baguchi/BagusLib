package baguchi.bagus_lib.entity;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.equipment.trim.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

//example Entity
public class MiniBagu extends PathfinderMob implements ISmartJump {
    public MiniBagu(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public static AttributeSupplier.Builder createAttributeMap() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.3F).add(Attributes.MAX_HEALTH, 24.0D).add(Attributes.FOLLOW_RANGE, 24.0D).add(Attributes.ATTACK_DAMAGE, 2.0F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }

    @Override
    public boolean doHurtTarget(ServerLevel p_376642_, Entity p_21372_) {
        //AnimationUtil.sendAnimation(this, CommonEvent.TEST);
        return super.doHurtTarget(p_376642_, p_21372_);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        //CollideUtil.collideEntities(this);
    }
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, EntitySpawnReason p_363352_, @Nullable SpawnGroupData p_21437_) {
        HolderLookup.RegistryLookup<TrimMaterial> registrylookup1 = this.registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL);
        HolderLookup.RegistryLookup<TrimPattern> registrylookup2 = this.registryAccess().lookupOrThrow(Registries.TRIM_PATTERN);

        ItemStack stack = new ItemStack(Items.LEATHER_HELMET);
        stack.set(DataComponents.TRIM, new ArmorTrim(registrylookup1.getOrThrow(TrimMaterials.EMERALD), registrylookup2.getOrThrow(TrimPatterns.SENTRY)));
        this.setItemSlot(EquipmentSlot.HEAD, stack);
        this.setDropChance(EquipmentSlot.HEAD, 0.0F);
        return super.finalizeSpawn(p_21434_, p_21435_, p_363352_, p_21437_);
    }

    @Override
    protected float getJumpPower() {
        float f = 0.42F;

        Path path = this.navigation.getPath();
        if (path != null && !path.isDone()) {
            Vec3 vec3 = path.getNextEntityPos(this);
            if (vec3.y > this.getY() + 0.5) {
                f = 0.5F;
            }
            if (vec3.y > this.getY() + 1.5) {
                f = 0.65F;
            }

            /*if (vec3.y > this.getY() + 2.5) {
                f = 1.0F;
            }*/
        }

        return super.getJumpPower((float) (f / this.getAttributeValue(Attributes.JUMP_STRENGTH)));
    }


    @Override
    public float getSuppportJump() {
        return 2.125F;
    }
}