package baguchi.bagus_lib.entity;

import baguchi.bagus_lib.CommonEvent;
import baguchi.bagus_lib.util.client.AnimationUtil;
import net.minecraft.core.Registry;
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
import org.jetbrains.annotations.Nullable;

//example Entity
public class MiniBagu extends PathfinderMob {
    public MiniBagu(EntityType<? extends PathfinderMob> p_21683_, Level p_21684_) {
        super(p_21683_, p_21684_);
    }

    public static AttributeSupplier.Builder createAttributeMap() {
        return Mob.createMobAttributes().add(Attributes.MOVEMENT_SPEED, (double) 0.3F).add(Attributes.MAX_HEALTH, 24.0D).add(Attributes.FOLLOW_RANGE, 24.0D).add(Attributes.ATTACK_DAMAGE, 2.0F);
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
        AnimationUtil.sendAnimation(this, CommonEvent.TEST);
        return super.doHurtTarget(p_376642_, p_21372_);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        //CollideUtil.collideEntities(this);
    }
    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, EntitySpawnReason p_363352_, @Nullable SpawnGroupData p_21437_) {
        ItemStack stack = new ItemStack(Items.LEATHER_HELMET);
        Registry<TrimPattern> registry = p_21434_.registryAccess().lookupOrThrow(Registries.TRIM_PATTERN);
        Registry<TrimMaterial> registry1 = p_21434_.registryAccess().lookupOrThrow(Registries.TRIM_MATERIAL);

        stack.set(DataComponents.TRIM, new ArmorTrim(registry1.getOrThrow(TrimMaterials.DIAMOND), registry.getOrThrow(TrimPatterns.HOST)));
        this.setItemSlot(EquipmentSlot.HEAD, stack);


        return super.finalizeSpawn(p_21434_, p_21435_, p_363352_, p_21437_);
    }
}