package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.entity.ai.goal.SeaMonsterAttackableTargetGoal;
import com.apocalypse.caerulaarbor.entity.base.SeaMonster;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.init.ModParticleTypes;
import com.apocalypse.caerulaarbor.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreakDoorGoal;
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
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.UUID;

public class NetherseaReefbreakerEntity extends SeaMonster {

    public static final String MODIFIER_UUID = "4bf1ea4a-c7f0-37b3-8940-7ab10737ff32";

    private int skillCooldown = 0;
    private int bonusTime = 0;
    private int bonusCount = 0;

    public NetherseaReefbreakerEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.NETHERSEA_REEFBREAKER.get(), world);
    }

    public NetherseaReefbreakerEntity(EntityType<NetherseaReefbreakerEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(false);
        setMaxUpStep(1.2f);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("SkillCooldown", this.skillCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        if (pCompound.contains("SkillCooldown")) {
            this.skillCooldown = pCompound.getInt("SkillCooldown");
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new BreakDoorGoal(this, e -> true) {
            @Override
            public boolean canUse() {
                Level world = NetherseaReefbreakerEntity.this.level();
                if (!super.canUse()) return false;
                return GameplayConfig.ENABLE_MOB_BREAK.get() && world.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            }

            @Override
            public boolean canContinueToUse() {
                Level world = NetherseaReefbreakerEntity.this.level();
                if (!super.canContinueToUse()) return false;
                return GameplayConfig.ENABLE_MOB_BREAK.get() && world.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
            }
        });
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 0.4, true) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 12.25;
            }
        });
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, false, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, SnowGolem.class, false, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Villager.class, false, false));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, Illusioner.class, false, false));
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Pillager.class, false, false));
        this.targetSelector.addGoal(9, new NearestAttackableTargetGoal<>(this, Vindicator.class, false, false));
        this.targetSelector.addGoal(10, new NearestAttackableTargetGoal<>(this, Witch.class, false, false));
        this.targetSelector.addGoal(11, new NearestAttackableTargetGoal<>(this, Piglin.class, false, false));
        this.targetSelector.addGoal(12, new NearestAttackableTargetGoal<>(this, PiglinBrute.class, false, false));
        this.targetSelector.addGoal(13, new NearestAttackableTargetGoal<>(this, ZombifiedPiglin.class, false, false));
        this.targetSelector.addGoal(14, new SeaMonsterAttackableTargetGoal<>(this, Player.class, false, false));
        this.goalSelector.addGoal(15, new RandomStrollGoal(this, 0.4));
        this.goalSelector.addGoal(16, new RandomLookAroundGoal(this));
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.PARROT_IMITATE_SILVERFISH;
    }

    @Override
    public void playStepSound(@NotNull BlockPos pos, @NotNull BlockState blockIn) {
        this.playSound(SoundEvents.SILVERFISH_STEP, 0.15f, 1);
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
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (flag && this.skillCooldown <= 0) {
            var attr = this.getAttribute(Attributes.ATTACK_DAMAGE);
            if (attr == null) return true;

            int count = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3), e -> e != this && e.getMaxHealth() >= 10).size();
            if (count >= 2) {
                this.triggerAnim("spin", "spin");
                if (!this.level().isClientSide()) {
                    this.level().playSound(null, this.getOnPos(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.HOSTILE, 1, 1);
                } else {
                    this.level().playLocalSound(this.getOnPos(), SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.HOSTILE, 1, 1, false);
                }
                var list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3), e -> e != this && !e.getType().is(ModTags.EntityTypes.SEA_BORN));
                list.forEach(e -> e.hurt(this.level().damageSources().mobAttack(this), (float) (attr.getValue() * 1.5)));
                this.skillCooldown = 40;
            }
        }
        return flag;
    }

    @Override
    public boolean doHurtTarget(Entity pEntity) {
        boolean flag = super.doHurtTarget(pEntity);
        if (flag) {
            this.bonusTime = 70;
            if (this.bonusCount < 15) {
                this.bonusCount++;
            }
            this.addBonusAttackModifier(this.bonusCount);
        }
        return flag;
    }

    private void addBonusAttackModifier(int level) {
        if (level <= 0) return;
        AttributeInstance attributeinstance = this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
        if (attributeinstance != null) {
            attributeinstance.removeModifier(UUID.fromString(MODIFIER_UUID));
            attributeinstance.addPermanentModifier(new AttributeModifier(
                    UUID.fromString(MODIFIER_UUID),
                    CaerulaArborMod.ATTRIBUTE_MODIFIER,
                    this.bonusCount * 0.15,
                    AttributeModifier.Operation.MULTIPLY_BASE
            ));
        }
    }

    private void removeBonusAttackModifier() {
        AttributeInstance attributeinstance = this.getAttributes().getInstance(Attributes.ATTACK_DAMAGE);
        if (attributeinstance != null) {
            attributeinstance.removeModifier(UUID.fromString(MODIFIER_UUID));
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.skillCooldown > 0) {
            this.skillCooldown--;
        }
        if (this.bonusTime > 0) {
            this.bonusTime--;
        } else {
            this.removeBonusAttackModifier();
            this.bonusCount = 0;
        }

        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ModParticleTypes.CRACKER_BUFF_0.get(),
                    this.getX(), this.getY() + this.getBbHeight() * 0.5, this.getZ(),
                    this.bonusCount + 1, 0.8, 1.5, 0.8, 0.3);
            if (this.bonusCount > 6) {
                serverLevel.sendParticles(ModParticleTypes.CRACKER_BUFF_1.get(),
                        this.getX(), this.getY(), this.getZ(),
                        3, 1, 0.5, 1, 0.3);
            }
        }

        LevelAccessor world = this.level();
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();

        if (world.getBlockState(this.blockPosition()).getBlock() == ModBlocks.SEA_TRAIL_GROWN.get() && world instanceof ServerLevel server) {
            var effect = this.getEffect(MobEffects.INVISIBILITY);
            if (effect != null && effect.getDuration() <= 5) {
                this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 20, 0));
            }
            server.sendParticles(ParticleTypes.SMOKE, x, y + 1, z, 4, 0.4, 2, 0.4, 0.01);
        }
        this.refreshDimensions();
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.5);
        builder = builder.add(Attributes.MAX_HEALTH, 85);
        builder = builder.add(Attributes.ARMOR, 10);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 13);
        builder = builder.add(Attributes.FOLLOW_RANGE, 48);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.85);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<?> event) {
        if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F))) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.nethersea_reefbreaker.idle"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.nethersea_reefbreaker.move"));
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
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.nethersea_reefbreaker.attack"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            this.remove(NetherseaReefbreakerEntity.RemovalReason.KILLED);
            this.dropExperience();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 0, this::movementPredicate));
        data.add(new AnimationController<>(this, "attacking", 0, this::attackingPredicate));
        data.add(new AnimationController<>(this, "spin", 0, event -> PlayState.STOP)
                .triggerableAnim("spin", RawAnimation.begin().thenPlay("animation.nethersea_reefbreaker.spin").thenLoop("animation.nethersea_reefbreaker.idle")));
    }
}
