package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.entity.ai.goal.SeaMonsterAttackableTargetGoal;
import com.apocalypse.caerulaarbor.entity.base.SeaMonster;
import com.apocalypse.caerulaarbor.init.ModEntities;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class PathShaperEntity extends SeaMonster {

    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.PROGRESS);
    private int hurtCount = 0;
    private int skillCooldown = 0;

    public PathShaperEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.PATH_SHAPER.get(), world);
    }

    public PathShaperEntity(EntityType<PathShaperEntity> type, Level world) {
        super(type, world);
        xpReward = 32;
        setNoAi(false);
        setMaxUpStep(1.5f);
        setPersistenceRequired();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.4, false) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 16;
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
    public SoundEvent getAmbientSound() {
        return SoundEvents.ZOMBIE_VILLAGER_AMBIENT;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.RAVAGER_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.RAVAGER_DEATH;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (source.is(DamageTypes.FALL)) {
            return false;
        }
        if (flag) {
            this.hurtCount = Math.min(this.hurtCount + 1, 10);
        }
        return flag;
    }

    @Override
    public void die(@NotNull DamageSource source) {
        super.die(source);

        var list = this.level().getEntitiesOfClass(PathshaperFractalEntity.class, new AABB(this.getOnPos()).inflate(32), e -> true);
        list.forEach(LivingEntity::kill);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("HUrtCount", this.hurtCount);
        compound.putInt("SkillCooldown", this.skillCooldown);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("AttackCount")) {
            this.hurtCount = compound.getInt("AttackCount");
        }
        if (compound.contains("SkillCooldown")) {
            this.skillCooldown = compound.getInt("SkillCooldown");
        }
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.refreshDimensions();

        if (!this.level().isClientSide) {
            if (this.hurtCount >= 10) {
                this.summonFractals();
                this.hurtCount = 0;
            }
        }
        if (this.skillCooldown > 0) {
            this.skillCooldown--;
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity pEntity) {
        if (this.skillCooldown <= 0) {
            this.skillCooldown = 40;
            this.summonFractals();
        }
        return super.doHurtTarget(pEntity);
    }

    private void summonFractals() {
        var num = this.level().getEntitiesOfClass(PathshaperFractalEntity.class, this.getBoundingBox().inflate(32), e -> true).size();
        if (num >= 8) return;
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

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void startSeenByPlayer(@NotNull ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(@NotNull ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.4);
        builder = builder.add(Attributes.MAX_HEALTH, 135);
        builder = builder.add(Attributes.ARMOR, 8);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 17);
        builder = builder.add(Attributes.FOLLOW_RANGE, 48);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<?> event) {
        if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) && !this.isVehicle() && !this.isAggressive() && !this.isSprinting()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.path_shaper.move"));
        }
        if (this.isDeadOrDying()) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.path_shaper.die"));
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
        if (this.deathTime != 20) return;

        this.remove(PathShaperEntity.RemovalReason.KILLED);
        this.dropExperience();

        // TODO 战利品表换了
        if (this.level() instanceof ServerLevel serverLevel && this.level().getServer() != null) {
            for (ItemStack stack : this.level().getServer().getLootData().getLootTable(new ResourceLocation("caerula_arbor:gameplay/relic_route")).getRandomItems(new LootParams.Builder(serverLevel).create(LootContextParamSets.EMPTY))) {
                ItemEntity stackEntity = new ItemEntity(serverLevel, this.getX(), this.getY(), this.getZ(), stack);
                stackEntity.setPickUpDelay(10);
                stackEntity.setUnlimitedLifetime();
                serverLevel.addFreshEntity(stackEntity);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 3, this::movementPredicate));
        data.add(new AnimationController<>(this, "attacking", 3, this::attackingPredicate));
    }
}
