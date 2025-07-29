package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.entity.ai.goal.SeaMonsterAttackableTargetGoal;
import com.apocalypse.caerulaarbor.entity.base.SeaMonster;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.init.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
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
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class NetherseaFounderEntity extends SeaMonster {

    public NetherseaFounderEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.NETHERSEA_FOUNDER.get(), world);
    }

    public NetherseaFounderEntity(EntityType<NetherseaFounderEntity> type, Level world) {
        super(type, world);
        xpReward = 4;
        setNoAi(false);
        setMaxUpStep(0.8f);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.4, true) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 4;
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
        this.goalSelector.addGoal(14, new RandomStrollGoal(this, 0.4));
        this.goalSelector.addGoal(15, new RandomLookAroundGoal(this));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.PUFFER_FISH_AMBIENT;
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
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL))
            return false;
        return super.hurt(source, amount);
    }

    @Override
    @ParametersAreNonnullByDefault
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
        var san = this.getAttribute(ModAttributes.SANITY_RATE.get());
        if (san != null) {
            san.setBaseValue(9);
        }

        return super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.refreshDimensions();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.4);
        builder = builder.add(Attributes.MAX_HEALTH, 32);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 5);
        builder = builder.add(Attributes.FOLLOW_RANGE, 24);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.75);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<?> event) {
        if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F))) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.nethersea_founder.move"));
        }
        if (this.isDeadOrDying()) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.nethersea_founder.die"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.nethersea_founder.idle"));
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
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.nethersea_founder.attack"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime < 20) return;

        this.remove(NetherseaFounderEntity.RemovalReason.KILLED);
        this.dropExperience();

        var world = this.level();
        var pos = this.blockPosition();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        if (ModBlocks.SEA_TRAIL_GROWN.get().defaultBlockState().canSurvive(world, pos)
                && world.getBlockState(pos).canBeReplaced()
                && GameplayConfig.ENABLE_MOB_BREAK.get()
                && world.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)
        ) {
            if (!world.isClientSide()) {
                world.playSound(null, pos, SoundEvents.SCULK_VEIN_PLACE, SoundSource.NEUTRAL, 2, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.SCULK_VEIN_PLACE, SoundSource.NEUTRAL, 2, 1, false);
            }

            world.setBlock(pos, ModBlocks.SEA_TRAIL_GROWN.get().defaultBlockState(), 3);
            world.levelEvent(2001, pos, Block.getId(ModBlocks.SEA_TRAIL_GROWN.get().defaultBlockState()));
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 3, this::movementPredicate));
        data.add(new AnimationController<>(this, "attacking", 3, this::attackingPredicate));
    }
}
