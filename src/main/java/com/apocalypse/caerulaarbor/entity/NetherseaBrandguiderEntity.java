package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.entity.ai.goal.SeaMonsterAttackableTargetGoal;
import com.apocalypse.caerulaarbor.entity.base.SeaMonster;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.init.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.ParametersAreNonnullByDefault;

public class NetherseaBrandguiderEntity extends SeaMonster {

    public static final EntityDataAccessor<Integer> DATA_laylimit = SynchedEntityData.defineId(NetherseaBrandguiderEntity.class, EntityDataSerializers.INT);

    public NetherseaBrandguiderEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.NETHERSEA_BRANDGUIDER.get(), world);
    }

    public NetherseaBrandguiderEntity(EntityType<NetherseaBrandguiderEntity> type, Level world) {
        super(type, world);
        xpReward = 8;
        setNoAi(false);
        setMaxUpStep(1.2f);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_laylimit, 64);
    }

    private boolean canPerformAttack(NetherseaBrandguiderEntity entity) {
        return entity.tickCount - entity.getLastHurtMobTimestamp() >= 0;
    }

    private <T extends LivingEntity> NearestAttackableTargetGoal<T> createNearestAttackableGoal(Class<T> goal) {
        return new NearestAttackableTargetGoal<>(this, goal, false, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && canPerformAttack(NetherseaBrandguiderEntity.this);
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && canPerformAttack(NetherseaBrandguiderEntity.this);
            }
        };
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && canPerformAttack(NetherseaBrandguiderEntity.this);
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && canPerformAttack(NetherseaBrandguiderEntity.this);
            }
        });
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.35, true) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 12.25;
            }

            @Override
            public boolean canUse() {
                return super.canUse() && canPerformAttack(NetherseaBrandguiderEntity.this);
            }

            @Override
            public boolean canContinueToUse() {
                return super.canContinueToUse() && canPerformAttack(NetherseaBrandguiderEntity.this);
            }

        });
        this.targetSelector.addGoal(3, createNearestAttackableGoal(IronGolem.class));
        this.targetSelector.addGoal(4, createNearestAttackableGoal(SnowGolem.class));
        this.targetSelector.addGoal(5, createNearestAttackableGoal(Villager.class));
        this.targetSelector.addGoal(6, createNearestAttackableGoal(Illusioner.class));
        this.targetSelector.addGoal(7, createNearestAttackableGoal(Pillager.class));
        this.targetSelector.addGoal(8, createNearestAttackableGoal(Vindicator.class));
        this.targetSelector.addGoal(9, createNearestAttackableGoal(Witch.class));
        this.targetSelector.addGoal(10, createNearestAttackableGoal(Piglin.class));
        this.targetSelector.addGoal(11, createNearestAttackableGoal(PiglinBrute.class));
        this.targetSelector.addGoal(12, createNearestAttackableGoal(ZombifiedPiglin.class));
        this.targetSelector.addGoal(13, new SeaMonsterAttackableTargetGoal<>(this, Player.class, false, false));
        this.goalSelector.addGoal(14, new RandomStrollGoal(this, 0.35));
        this.goalSelector.addGoal(15, new RandomLookAroundGoal(this));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.RAVAGER_AMBIENT;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15f, 1);
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.RAVAGER_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.PUFFER_FISH_DEATH;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Datalaylimit", this.entityData.get(DATA_laylimit));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Datalaylimit"))
            this.entityData.set(DATA_laylimit, compound.getInt("Datalaylimit"));
    }

    @Override
    public void baseTick() {
        super.baseTick();

        var world = this.level();
        var pos = this.blockPosition();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        if (this.getHealth() < this.getMaxHealth() * 0.5
                && GameplayConfig.ENABLE_MOB_BREAK.get()
                && world.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
        ) {
            if (!this.level().isClientSide()) {
                this.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 2));
            }

            if (this.getEntityData().get(DATA_laylimit) > 0) {
                var state = world.getBlockState(pos);
                if (state.canBeReplaced()
                        && state.getBlock() != ModBlocks.SEA_TRAIL_GROWN.get()
                        && ModBlocks.SEA_TRAIL_GROWN.get().defaultBlockState().canSurvive(world, pos)
                ) {
                    world.setBlock(pos, ModBlocks.SEA_TRAIL_GROWN.get().defaultBlockState(), 3);

                    if (world instanceof ServerLevel server) {
                        server.playLocalSound(x, y, z, SoundEvents.SCULK_VEIN_STEP, SoundSource.NEUTRAL, 2, 1, false);
                    } else {
                        world.playSound(null, pos, SoundEvents.SCULK_VEIN_STEP, SoundSource.NEUTRAL, 2, 1);
                    }

                    this.getEntityData().set(DATA_laylimit, this.getEntityData().get(DATA_laylimit) - 1);
                }
            }
        }
        this.refreshDimensions();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.35);
        builder = builder.add(Attributes.MAX_HEALTH, 80);
        builder = builder.add(Attributes.ARMOR, 15);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 14);
        builder = builder.add(Attributes.FOLLOW_RANGE, 24);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.8);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<?> event) {
        if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) && !this.isVehicle() && !this.isAggressive() && !this.isSprinting()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.nethersea_brandguider.move"));
        }
        if (this.isDeadOrDying()) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.nethersea_brandguider.die"));
        }
        if (this.isInWaterOrBubble() || this.isSprinting() || (this.isVehicle() && event.isMoving()) || (this.isAggressive() && event.isMoving() && !this.isVehicle())) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.nethersea_brandguider.move"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.nethersea_brandguider.idle"));
    }

    private PlayState attackingPredicate(AnimationState<?> event) {
        if (getAttackAnim(event.getPartialTick()) > 0f && !this.swinging) {
            this.swinging = true;
            this.lastSwing = level().getGameTime();
        }
        if (this.swinging && this.lastSwing + 30L <= level().getGameTime()) {
            this.swinging = false;
        }
        if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.nethersea_brandguider.attack"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            this.remove(NetherseaBrandguiderEntity.RemovalReason.KILLED);
            this.dropExperience();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.add(new AnimationController<>(this, "attacking", 0, this::attackingPredicate));
    }
}
