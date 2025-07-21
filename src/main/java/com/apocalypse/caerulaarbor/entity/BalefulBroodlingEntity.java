package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.entity.ai.goal.SeaMonsterAttackableTargetGoal;
import com.apocalypse.caerulaarbor.entity.base.SeaMonster;
import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.init.ModItems;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LeapAtTargetGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

public class BalefulBroodlingEntity extends SeaMonster {

    // TODO 修改随机角度旋转的实现方式
    public static final EntityDataAccessor<Integer> DATA_dx = SynchedEntityData.defineId(BalefulBroodlingEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> DATA_dz = SynchedEntityData.defineId(BalefulBroodlingEntity.class, EntityDataSerializers.INT);

    public BalefulBroodlingEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.BALEFUL_BROODLING.get(), world);
    }

    public BalefulBroodlingEntity(EntityType<BalefulBroodlingEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(false);
        setMaxUpStep(0.6f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_dx, 0);
        this.entityData.define(DATA_dz, 0);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.2, false) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
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
        this.goalSelector.addGoal(14, new LeapAtTargetGoal(this, 0.2F));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.PARROT_IMITATE_SLIME;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.SLIME_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.SLIME_DEATH;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    @ParametersAreNonnullByDefault
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        this.getEntityData().set(DATA_dx, Mth.nextInt(RandomSource.create(), -50, 50));
        this.getEntityData().set(DATA_dz, Mth.nextInt(RandomSource.create(), -50, 50));

        return super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Datadx", this.entityData.get(DATA_dx));
        compound.putInt("Datadz", this.entityData.get(DATA_dz));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Datadx"))
            this.entityData.set(DATA_dx, compound.getInt("Datadx"));
        if (compound.contains("Datadz"))
            this.entityData.set(DATA_dz, compound.getInt("Datadz"));
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.tickCount % 20 == 0) {
            float damage = this.getMaxHealth() / 8f;
            if (this.getHealth() > damage) {
                this.setHealth(this.getHealth() - damage);
            } else {
                this.kill();
            }
        }

        if (this.isAggressive()) {
            var target = this.getTarget();
            if (target != null && distanceTo(target) <= 4) {
                teleportTo(target.getX() + target.getBbWidth() * this.getEntityData().get(DATA_dx) * 0.01,
                        target.getY(),
                        target.getZ() + target.getBbWidth() * this.getEntityData().get(DATA_dz) * 0.01
                );
            }
        }
        this.refreshDimensions();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(@NotNull Entity entityIn) {
    }

    @Override
    protected void pushEntities() {
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.1);
        builder = builder.add(Attributes.MAX_HEALTH, 8);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<?> event) {
        if (this.isDeadOrDying()) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.baleful_broodling.die"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.baleful_broodling.idle"));
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
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.baleful_broodling.attack"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime != 20) return;

        this.remove(BalefulBroodlingEntity.RemovalReason.KILLED);
        this.dropExperience();

        if (Math.random() < 0.2 && this.level() instanceof ServerLevel serverLevel) {
            ItemEntity entityToSpawn = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), new ItemStack(ModItems.FAKE_EGG.get()));
            entityToSpawn.setPickUpDelay(10);
            serverLevel.addFreshEntity(entityToSpawn);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 4, this::movementPredicate));
        data.add(new AnimationController<>(this, "attacking", 4, this::attackingPredicate));
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && pEntity instanceof LivingEntity living && !living.level().isClientSide) {
            living.addEffect(new MobEffectInstance(ModMobEffects.ARMOR_BREAKING.get(), 300, 2), this);
        }
        return flag;
    }
}
