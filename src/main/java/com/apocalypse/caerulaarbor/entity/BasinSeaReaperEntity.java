package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.entity.base.SeaMonster;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class BasinSeaReaperEntity extends SeaMonster {

    public BasinSeaReaperEntity(PlayMessages.SpawnEntity packet, Level level) {
        this(ModEntities.BASIN_SEA_REAPER.get(), level);
    }

    public BasinSeaReaperEntity(EntityType<BasinSeaReaperEntity> type, Level level) {
        super(type, level);
        xpReward = 8;
        setNoAi(false);
        setMaxUpStep(1.2f);
        setPersistenceRequired();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.8, true) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 4;
            }

            @Override
            public boolean canUse() {
                if (!super.canUse()) return false;
                return BasinSeaReaperEntity.this.tickCount - BasinSeaReaperEntity.this.getLastHurtMobTimestamp() >= 30;
            }

        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, false, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, SnowGolem.class, false, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Villager.class, false, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Illusioner.class, false, false));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, Pillager.class, false, false));
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Vindicator.class, false, false));
        this.targetSelector.addGoal(9, new NearestAttackableTargetGoal<>(this, Witch.class, false, false));
        this.targetSelector.addGoal(10, new NearestAttackableTargetGoal<>(this, Piglin.class, false, false));
        this.targetSelector.addGoal(11, new NearestAttackableTargetGoal<>(this, PiglinBrute.class, false, false));
        this.targetSelector.addGoal(12, new NearestAttackableTargetGoal<>(this, ZombifiedPiglin.class, false, false));
        this.goalSelector.addGoal(13, new RandomStrollGoal(this, 0.3));
        this.goalSelector.addGoal(14, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.PHANTOM_AMBIENT;
    }

    @Override
    public void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15f, 1);
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.PUFFER_FISH_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.PHANTOM_DEATH;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        var world = this.level();
        int x = (int) this.getX();
        int y = (int) this.getY();
        int z = (int) this.getZ();
        double limithard;
        double hardness;

        limithard = -1;
        double migrationStrategy = MapVariables.get(world).strategyMigration;

        if (migrationStrategy >= 2) {
            limithard = 3.5;
        } else if (migrationStrategy >= 4) {
            // TODO 什么数可以既小于2又大于等于4？
            limithard = 5;
        }
        if (GameplayConfig.ENABLE_MOB_BREAK.get()
                && world.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
                && limithard > 0
                && Math.random() < 0.5
        ) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    for (int dy = 1; dy <= 3; dy++) {
                        var pos = new BlockPos(x + dx, y + dy, z + dz);
                        var state = world.getBlockState(pos);

                        hardness = state.getDestroySpeed(world, new BlockPos(0, 0, 0));
                        if (hardness <= limithard
                                && hardness >= 0
                                && world.getBlockFloorHeight(pos) > 0
                                && Math.random() < 0.75
                        ) {
                            Block.dropResources(state, world, this.blockPosition(), null);
                            world.destroyBlock(pos, false);

                            world.updateNeighborsAt(pos, state.getBlock());
                        }
                    }
                }
            }

            if (!world.isClientSide()) {
                world.playSound(null, BlockPos.containing(x, y, z), SoundEvents.WITHER_BREAK_BLOCK, SoundSource.NEUTRAL, 1, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.WITHER_BREAK_BLOCK, SoundSource.NEUTRAL, 1, 1, false);
            }
        }

        if (source.is(DamageTypes.DROWN)) return false;

        return super.hurt(source, amount);
    }

    @Override
    @ParametersAreNonnullByDefault
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        var san = this.getAttribute(ModAttributes.SANITY_RATE.get());
        if (san != null) {
            san.setBaseValue(6);
        }
        return super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.isAggressive()
                && !this.hasEffect(ModMobEffects.FISH_REAP.get())
                && !this.level().isClientSide()
        ) {
            this.addEffect(new MobEffectInstance(ModMobEffects.FISH_REAP.get(), 100, 0, false, false));
        }

        this.refreshDimensions();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.8);
        builder = builder.add(Attributes.MAX_HEALTH, 80);
        builder = builder.add(Attributes.ARMOR, 10);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 7);
        builder = builder.add(Attributes.FOLLOW_RANGE, 32);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.75);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<?> event) {
        if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) && !this.isAggressive() && !this.isSprinting()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.basin_sea_reaper.move"));
        }
        if (this.isSprinting() || (this.isAggressive() && event.isMoving())) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.basin_sea_reaper.sprint"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.basin_sea_reaper.idle"));
    }

    private PlayState attackingPredicate(AnimationState<?> event) {
        if (getAttackAnim(event.getPartialTick()) > 0f && !this.swinging) {
            this.swinging = true;
            this.lastSwing = level().getGameTime();
        }
        if (this.swinging && this.lastSwing + 15L <= level().getGameTime()) {
            this.swinging = false;
        }
        if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.basin_sea_reaper.attack"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            this.remove(BasinSeaReaperEntity.RemovalReason.KILLED);
            this.dropExperience();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 1, this::movementPredicate));
        data.add(new AnimationController<>(this, "attacking", 1, this::attackingPredicate));
    }
}
