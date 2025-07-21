package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.entity.ai.goal.SeaMonsterAttackableTargetGoal;
import com.apocalypse.caerulaarbor.entity.base.SeaMonster;
import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class BoneSeaDrifterEntity extends SeaMonster {

    public BoneSeaDrifterEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.BONE_SEA_DRIFTER.get(), world);
    }

    public BoneSeaDrifterEntity(EntityType<BoneSeaDrifterEntity> type, Level world) {
        super(type, world);
        xpReward = 3;
        setNoAi(false);
        setMaxUpStep(0.6f);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0);
        this.moveControl = new MoveControl(this) {
            @Override
            public void tick() {
                if (BoneSeaDrifterEntity.this.isInWater())
                    BoneSeaDrifterEntity.this.setDeltaMovement(BoneSeaDrifterEntity.this.getDeltaMovement().add(0, 0.005, 0));
                if (this.operation == MoveControl.Operation.MOVE_TO && !BoneSeaDrifterEntity.this.getNavigation().isDone()) {
                    double dx = this.wantedX - BoneSeaDrifterEntity.this.getX();
                    double dy = this.wantedY - BoneSeaDrifterEntity.this.getY();
                    double dz = this.wantedZ - BoneSeaDrifterEntity.this.getZ();
                    float f = (float) (Mth.atan2(dz, dx) * (180 / Math.PI)) - 90;
                    float f1 = (float) (this.speedModifier * BoneSeaDrifterEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                    BoneSeaDrifterEntity.this.setYRot(this.rotlerp(BoneSeaDrifterEntity.this.getYRot(), f, 10));
                    BoneSeaDrifterEntity.this.yBodyRot = BoneSeaDrifterEntity.this.getYRot();
                    BoneSeaDrifterEntity.this.yHeadRot = BoneSeaDrifterEntity.this.getYRot();
                    if (BoneSeaDrifterEntity.this.isInWater()) {
                        BoneSeaDrifterEntity.this.setSpeed((float) BoneSeaDrifterEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
                        float f2 = -(float) (Mth.atan2(dy, (float) Math.sqrt(dx * dx + dz * dz)) * (180 / Math.PI));
                        f2 = Mth.clamp(Mth.wrapDegrees(f2), -85, 85);
                        BoneSeaDrifterEntity.this.setXRot(this.rotlerp(BoneSeaDrifterEntity.this.getXRot(), f2, 5));
                        float f3 = Mth.cos(BoneSeaDrifterEntity.this.getXRot() * (float) (Math.PI / 180.0));
                        BoneSeaDrifterEntity.this.setZza(f3 * f1);
                        BoneSeaDrifterEntity.this.setYya((float) (f1 * dy));
                    } else {
                        BoneSeaDrifterEntity.this.setSpeed(f1 * 0.05F);
                    }
                } else {
                    BoneSeaDrifterEntity.this.setSpeed(0);
                    BoneSeaDrifterEntity.this.setYya(0);
                    BoneSeaDrifterEntity.this.setZza(0);
                }
            }
        };
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level world) {
        return new WaterBoundPathNavigation(this, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 5, false) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, GlowSquid.class, false, false));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Squid.class, false, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, TropicalFish.class, false, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Salmon.class, false, false));
        this.targetSelector.addGoal(7, new SeaMonsterAttackableTargetGoal<>(this, Player.class, false, false));
        this.goalSelector.addGoal(8, new RandomSwimmingGoal(this, 5, 40));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.refreshDimensions();
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader world) {
        return world.isUnobstructed(this);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 1.5);
        builder = builder.add(Attributes.MAX_HEALTH, 12);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 4);
        builder = builder.add(Attributes.FOLLOW_RANGE, 12);
        builder = builder.add(ForgeMod.SWIM_SPEED.get(), 1.5);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<?> event) {
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.bone_sea_drifter.idle"));
    }

    private PlayState attackingPredicate(AnimationState<?> event) {
        if (getAttackAnim(event.getPartialTick()) > 0f && !this.swinging) {
            this.swinging = true;
            this.lastSwing = level().getGameTime();
        }
        if (this.swinging && this.lastSwing + 7L <= level().getGameTime()) {
            this.swinging = false;
        }
        if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.bone_sea_drifter.attack"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            this.remove(BoneSeaDrifterEntity.RemovalReason.KILLED);
            this.dropExperience();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 3, this::movementPredicate));
        data.add(new AnimationController<>(this, "attacking", 3, this::attackingPredicate));
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag && pEntity instanceof LivingEntity living && !living.level().isClientSide) {
            living.addEffect(new MobEffectInstance(ModMobEffects.ARMOR_BREAKING.get(), 300, 1), this);
        }
        return flag;
    }
}
