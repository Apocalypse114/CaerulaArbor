package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.entity.ai.goal.SeaMonsterAttackableTargetGoal;
import com.apocalypse.caerulaarbor.entity.base.SeaMonster;
import com.apocalypse.caerulaarbor.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class PathshaperFractalEntity extends SeaMonster {

    private int attackCount = 0;
    private int life = 1800;

    public PathshaperFractalEntity(PlayMessages.SpawnEntity packet, Level level) {
        this(ModEntities.PATHSHAPER_FRACTAL.get(), level);
    }

    public PathshaperFractalEntity(Level level) {
        this(ModEntities.PATHSHAPER_FRACTAL.get(), level);
    }

    public PathshaperFractalEntity(EntityType<PathshaperFractalEntity> type, Level level) {
        super(type, level);
        xpReward = 8;
        setNoAi(false);
        setMaxUpStep(1f);
        setPersistenceRequired();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.5, false) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 6.25;
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
        this.targetSelector.addGoal(13, new SeaMonsterAttackableTargetGoal<>(this, Player.class, false, false));
        this.goalSelector.addGoal(14, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(15, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.PUFFER_FISH_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.PUFFER_FISH_DEATH;
    }

    @Override
    @ParametersAreNonnullByDefault
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        if (world instanceof Level level) {
            if (!level.isClientSide()) {
                level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.AXOLOTL_SPLASH, SoundSource.HOSTILE, 0.75f, 1);
            } else {
                level.playLocalSound(x, y, z, SoundEvents.AXOLOTL_SPLASH, SoundSource.HOSTILE, 0.75f, 1, false);
            }
        }
        return super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("SkillCooldown", this.attackCount);
        compound.putInt("Life", this.life);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("SkillCooldown")) {
            this.attackCount = compound.getInt("SkillCooldown");
        }
        if (compound.contains("Life")) {
            this.life = compound.getInt("Life");
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.life > 0) {
            this.life--;
        } else {
            this.kill();
        }

        this.refreshDimensions();
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        if (this.attackCount >= 2) {
            this.attackCount = 0;
            this.summonFractals();
        } else {
            this.attackCount++;
        }
        return super.doHurtTarget(pEntity);
    }

    private void summonFractals() {
        var num = this.level().getEntitiesOfClass(PathshaperFractalEntity.class, this.getBoundingBox().inflate(32), e -> true).size();
        if (num >= 12) return;
        var dx = Mth.nextDouble(RandomSource.create(), -0.5, 0.5);
        var dz = Mth.nextDouble(RandomSource.create(), -0.5, 0.5);

        if (this.level() instanceof ServerLevel serverLevel) {
            var entity = new PathshaperFractalEntity(serverLevel);
            entity.setPos(this.getX() + dx, this.getY(), this.getZ() + dz);
            entity.setYRot(this.level().random.nextFloat() * 360F);
            entity.setHealth(entity.getMaxHealth() * (this.getHealth() / this.getMaxHealth()));
            serverLevel.addFreshEntity(entity);
            serverLevel.sendParticles(ParticleTypes.CLOUD, this.getX() + dx, this.getY(), this.getZ() + dz, 24, 0.5, 1, 0.5, 0.1);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.5);
        builder = builder.add(Attributes.MAX_HEALTH, 75);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 12);
        builder = builder.add(Attributes.FOLLOW_RANGE, 32);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<?> event) {
        if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) && !this.isVehicle() && !this.isAggressive() && !this.isSprinting()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.path_shaper.move"));
        }
        if (this.isSprinting() || (this.isVehicle() && event.isMoving()) || (this.isAggressive() && event.isMoving() && !this.isVehicle())) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.path_shaper.move"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.path_shaper.idle"));
    }

    private PlayState attackingPredicate(AnimationState<?> event) {
        if (getAttackAnim(event.getPartialTick()) > 0f && !this.swinging) {
            this.swinging = true;
            this.lastSwing = level().getGameTime();
        }
        if (this.swinging && this.lastSwing + 20L <= level().getGameTime()) {
            this.swinging = false;
        }
        if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.path_shaper.attack"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            this.remove(PathshaperFractalEntity.RemovalReason.KILLED);
            this.dropExperience();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.add(new AnimationController<>(this, "attacking", 0, this::attackingPredicate));
    }
}
