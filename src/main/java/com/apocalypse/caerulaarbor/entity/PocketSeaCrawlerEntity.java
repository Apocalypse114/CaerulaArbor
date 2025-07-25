package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.entity.base.SeaMonster;
import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.init.ModTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveBackToVillageGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

public class PocketSeaCrawlerEntity extends SeaMonster {

    public static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(PocketSeaCrawlerEntity.class, EntityDataSerializers.FLOAT);

    public PocketSeaCrawlerEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.POCKET_SEA_CRAWLER.get(), world);
    }

    public PocketSeaCrawlerEntity(EntityType<PocketSeaCrawlerEntity> type, Level world) {
        super(type, world);
        xpReward = 8;
        setNoAi(false);
        setMaxUpStep(1.2f);
        setPersistenceRequired();
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        float damage = this.getEntityData().get(DAMAGE);
        this.getEntityData().set(DAMAGE, damage + amount);
        return super.hurt(source, amount);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DAMAGE, 0f);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.3, true) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 1;
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Cat.class, false, false));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 0.3));
        this.goalSelector.addGoal(5, new MoveBackToVillageGoal(this, 0.6, false));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.TROPICAL_FISH_AMBIENT;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.CREEPER_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.PUFFER_FISH_DEATH;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Damage", this.entityData.get(DAMAGE));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("Damage")) {
            this.entityData.set(DAMAGE, compound.getFloat("Damage"));
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand hand) {
        super.mobInteract(pPlayer, hand);
        ItemStack itemstack = pPlayer.getItemInHand(hand);

        var level = this.level();
        if (itemstack.getItem() == Items.FLINT_AND_STEEL) {
            if (!level.isClientSide()) {
                level.playSound(null, this.getOnPos(), SoundEvents.CREEPER_PRIMED, SoundSource.HOSTILE, 2, 1);
            } else {
                level.playLocalSound(this.getOnPos(), SoundEvents.CREEPER_PRIMED, SoundSource.HOSTILE, 2, 1, false);
                this.triggerAnim("jump", "jump");
            }

            for (int i = 0; i < 5; i++) {
                CaerulaArborMod.queueServerWork(i * 4, this::selfExplosion);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void baseTick() {
        super.baseTick();
        this.refreshDimensions();

        float damage = this.getEntityData().get(DAMAGE);
        if (damage >= this.getMaxHealth() * 0.15f) {
            this.selfExplosion();
            this.getEntityData().set(DAMAGE, 0f);
        }
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.3);
        builder = builder.add(Attributes.MAX_HEALTH, 85);
        builder = builder.add(Attributes.ARMOR, 0);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 2);
        builder = builder.add(Attributes.FOLLOW_RANGE, 24);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 0.85);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<?> event) {
        if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F)) && !this.isVehicle() && !this.isAggressive() && !this.isSprinting()) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.pocket_sea_crawler.move"));
        }
        if (this.isDeadOrDying()) {
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.pocket_sea_crawler.die"));
        }
        if (this.isShiftKeyDown() || this.isSprinting() || (this.isVehicle() && event.isMoving()) || (this.isAggressive() && event.isMoving() && !this.isVehicle())) {
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.pocket_sea_crawler.move"));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop("animation.pocket_sea_crawler.idle"));
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
            return event.setAndContinue(RawAnimation.begin().thenPlay("animation.pocket_sea_crawler.attack"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 20) {
            this.remove(PocketSeaCrawlerEntity.RemovalReason.KILLED);
            this.dropExperience();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 2, this::movementPredicate));
        data.add(new AnimationController<>(this, "attacking", 2, this::attackingPredicate));
        data.add(new AnimationController<>(this, "jump", 2, event -> PlayState.STOP)
                .triggerableAnim("jump", RawAnimation.begin().thenPlay("animation.pocket_sea_crawler.jump").thenLoop("animation.pocket_sea_crawler.idle")));
    }

    public void selfExplosion() {
        var level = this.level();
        for (int i = 0; i < 5; i++) {
            CaerulaArborMod.queueServerWork(i * 2, () -> {
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK, this.getX(), this.getY(), this.getZ(), 32, 4, 4, 4, 0.1);
                }
            });
        }
        var attr = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attr == null) return;
        double damage = attr.getValue();

        List<LivingEntity> entities = level.getEntitiesOfClass(LivingEntity.class, new AABB(this.getOnPos()).inflate(4), e -> true);
        for (var e : entities) {
            if (e == this || e.getType().is(ModTags.EntityTypes.SEA_BORN)) {
                continue;
            }
            e.hurt(level().damageSources().magic(), (float) (damage * 3));
            ModCapabilities.getSanityInjury(e).hurt(damage * 150);
        }
    }
}
