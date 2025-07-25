package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import com.apocalypse.caerulaarbor.entity.ai.goal.SeaMonsterAttackableTargetGoal;
import com.apocalypse.caerulaarbor.entity.base.SeaMonster;
import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class NetherseaSwarmcallerEntity extends SeaMonster {

    public NetherseaSwarmcallerEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.NETHERSEA_SWARMCALLER.get(), world);
    }

    public NetherseaSwarmcallerEntity(EntityType<NetherseaSwarmcallerEntity> type, Level world) {
        super(type, world);
        xpReward = 6;
        setNoAi(false);
        setMaxUpStep(1f);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.4, false) {
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
        this.goalSelector.addGoal(14, new RandomStrollGoal(this, 0.4));
        this.goalSelector.addGoal(15, new RandomLookAroundGoal(this));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.GLOW_SQUID_AMBIENT;
    }

    @Override
    public void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15f, 1);
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.GLOW_SQUID_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.PUFFER_FISH_DEATH;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        var world = this.level();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        double angle;

        this.removeEffect(MobEffects.MOVEMENT_SLOWDOWN);
        if (world instanceof ServerLevel server) {
            for (int index0 = 0; index0 < 8; index0++) {
                angle = Mth.nextDouble(RandomSource.create(), 0, 6.283);
                server.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, x + 3.9 * Math.sin(angle), y + 0.4, z + 3.9 * Math.cos(angle), 2, 0.1, 0.1, 0.1, 0.1);

                angle = Mth.nextDouble(RandomSource.create(), 0, 6.283);
                server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x + 3.9 * Math.sin(angle), y + 0.4, z + 3.9 * Math.cos(angle), 2, 0.1, 0.1, 0.1, 0.1);
            }

            if (MapVariables.get(world).strategyGrow >= 3) {
                for (int index1 = 0; index1 < 14; index1++) {
                    angle = Mth.nextDouble(RandomSource.create(), 0, 6.283);
                    server.sendParticles(ParticleTypes.BUBBLE_COLUMN_UP, x + 6.9 * Math.sin(angle), y + 0.4, z + 6.9 * Math.cos(angle), 2, 0.15, 0.15, 0.15, 0.1);

                    angle = Mth.nextDouble(RandomSource.create(), 0, 6.283);
                    server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x + 6.9 * Math.sin(angle), y + 0.4, z + 6.9 * Math.cos(angle), 2, 0.15, 0.15, 0.15, 0.1);
                }
            }

            if (!this.hasEffect(ModMobEffects.UMBRELLA_SETTLE.get())) {
                this.addEffect(new MobEffectInstance(ModMobEffects.UMBRELLA_SETTLE.get(), 120, 0, false, false));
            }
        }

        this.refreshDimensions();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.4);
        builder = builder.add(Attributes.MAX_HEALTH, 40);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 2);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<?> event) {
        if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F))) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.nethersea_swarmcaller.move"));
        }
        if (this.isDeadOrDying()) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.nethersea_swarmcaller.die"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.nethersea_swarmcaller.idle"));
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            this.remove(NetherseaSwarmcallerEntity.RemovalReason.KILLED);
            this.dropExperience();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 4, this::movementPredicate));
    }
}
