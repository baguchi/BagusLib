package bagu_chan.bagus_lib.entity;

import bagu_chan.bagus_lib.CommonEvent;
import bagu_chan.bagus_lib.util.client.AnimationUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raid;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
    public boolean doHurtTarget(Entity p_21372_) {

        AnimationUtil.sendAnimation(this, CommonEvent.TEST);
        return super.doHurtTarget(p_21372_);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_21434_, DifficultyInstance p_21435_, MobSpawnType p_21436_, @Nullable SpawnGroupData p_21437_, @Nullable CompoundTag p_21438_) {
        ItemStack stack = new ItemStack(Items.IRON_HELMET);
        Optional<Holder.Reference<TrimMaterial>> trimMaterial = p_21434_.registryAccess().registry(Registries.TRIM_MATERIAL).get().getHolder(TrimMaterials.LAPIS);
        Optional<Holder.Reference<TrimPattern>> trimPattern = p_21434_.registryAccess().registry(Registries.TRIM_PATTERN).get().getHolder(TrimPatterns.WILD);
        if (trimMaterial.isPresent() && trimPattern.isPresent()) {
            ArmorTrim.setTrim(p_21434_.registryAccess(), stack, new ArmorTrim(trimMaterial.get(), trimPattern.get()));
        }
        this.setItemSlot(EquipmentSlot.HEAD, Raid.getLeaderBannerInstance());

        return super.finalizeSpawn(p_21434_, p_21435_, p_21436_, p_21437_, p_21438_);
    }
}
