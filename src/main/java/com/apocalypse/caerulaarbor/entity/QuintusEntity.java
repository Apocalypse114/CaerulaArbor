package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.entity.ai.Skill;
import com.apocalypse.caerulaarbor.entity.ai.goal.SeaMonsterAttackableTargetGoal;
import com.apocalypse.caerulaarbor.entity.base.SkilledSeaMonster;
import com.apocalypse.caerulaarbor.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

//TODO 主教鱼
@Mod.EventBusSubscriber
public class QuintusEntity extends SkilledSeaMonster {
    public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(QuintusEntity.class, EntityDataSerializers.BOOLEAN);

    private final ServerBossEvent bossInfo = new ServerBossEvent(this.getDisplayName(), ServerBossEvent.BossBarColor.BLUE, ServerBossEvent.BossBarOverlay.NOTCHED_10);

    private Vec3 anchorPos;

    public QuintusEntity(PlayMessages.SpawnEntity packet, Level world) {
        this(ModEntities.QUINTUS.get(), world);
    }

    private final List<EntityType<?>> candidate = initUnderTidesEntities();

    public QuintusEntity(EntityType<QuintusEntity> type, Level world) {
        super(type, world);
        xpReward = 64;
        this.addSkill(Skill.Builder.of().init(200).max(200).durative(20).build());
        this.addSkill(Skill.Builder.of().init(1200).max(2400).durative(40).build());
        initUnderTidesEntities();
        setNoAi(false);
        setMaxUpStep(2f);
        setPersistenceRequired();
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SHOOT, false);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0, true) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 144;
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
        this.goalSelector.addGoal(14, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    @Override
    public SoundEvent getAmbientSound() {
        return SoundEvents.GUARDIAN_AMBIENT;
    }

    @Override
    public @NotNull SoundEvent getHurtSound(DamageSource ds) {
        return SoundEvents.GUARDIAN_HURT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.GUARDIAN_DEATH;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (source.is(DamageTypes.FALL) || source.is(DamageTypes.DROWN)
                || source.is(DamageTypes.LIGHTNING_BOLT) || source.is(DamageTypes.FALLING_ANVIL))
            return false;
        if (this.skillReady(0)) this.greatTide();
        return super.hurt(source, amount);
    }

    @Override
    public void baseTick() {
        super.baseTick();
        if(this.skillReady(1)) this.speciedOutbreak();
        if(this.tickCount <= 2) this.anchorPos = this.position();
        else if(this.distanceToSqr(this.anchorPos) >= 16) this.setPos(this.anchorPos);
    }

    @Override
    public EntityDimensions getDimensions(Pose p_33597_) {
        return super.getDimensions(p_33597_).scale((float) 1);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.bossInfo.setProgress(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty,
                                        @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.anchorPos = this.position();
        return pSpawnData;
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0);
        builder = builder.add(Attributes.MAX_HEALTH, 560);
        builder = builder.add(Attributes.ARMOR, 8);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 9);
        builder = builder.add(Attributes.FOLLOW_RANGE, 64);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 10);
        builder = builder.add(ModAttributes.SANITY_RATE.get(), 10);
        return builder;
    }

    private PlayState movementPredicate(AnimationState<QuintusEntity> event) {
        if (this.isDeadOrDying()) {
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("die")));
        }
        return event.setAndContinue(RawAnimation.begin().thenLoop(animLoc("idle")));
    }

    private PlayState attackingPredicate(AnimationState<QuintusEntity> event) {
        if (getAttackAnim(event.getPartialTick()) > 0f && !this.swinging) {
            this.swinging = true;
            this.lastSwing = level().getGameTime();
        }
        if (this.swinging && this.lastSwing + 20L <= level().getGameTime()) {
            this.swinging = false;
        }
        if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
            event.getController().forceAnimationReset();
            return event.setAndContinue(RawAnimation.begin().thenPlay(animLoc("attack")));
        }
        return PlayState.CONTINUE;
    }

    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime == 40) {
            this.remove(RemovalReason.KILLED);
            this.dropExperience();
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        data.add(new AnimationController<>(this, "movement", 2, this::movementPredicate));
        data.add(new AnimationController<>(this, "attacking", 2, this::attackingPredicate));
        data.add(new AnimationController<>(this, "skill", 0, event -> PlayState.STOP)
                .triggerableAnim("skill", RawAnimation.begin()
                        .thenPlay(animLoc("skill"))
                        .thenLoop(animLoc("idle"))));
        data.add(new AnimationController<>(this, "end", 0, event -> PlayState.STOP)
                .triggerableAnim("end", RawAnimation.begin()
                        .thenPlay(animLoc("blast"))
                        .thenLoop(animLoc("idle"))));
    }

    private void greatTide() {
        int p = 25;
        float perc = this.getHealth() / this.getMaxHealth();
        if (perc < 0.33) p = 15;
        else if (perc < 0.67) p = 5;
        resetSkill(0,p * 20);
        setDuration(0);
        triggerSound(SoundEvents.AMBIENT_UNDERWATER_EXIT);
        triggerAnim("skill", "skill");
        for (int i = 1; i <= 10; i++) {
            int I = 2 * i;
            CaerulaArborMod.queueServerWork(2 * i, () -> rim(I));
        }
        Vec3 pos = this.position();
        AABB aabb = new AABB(pos.add(-20, -4, -20), pos.add(20, 8, 20));
        List<LivingEntity> ents = this.level().getEntitiesOfClass(LivingEntity.class, aabb, e -> isLegalTarget(e) && this.distanceTo(e) <= 20);
        ents.forEach(this::pushAndDamage);
    }

    private void speciedOutbreak(){
        resetSkill(1);
        setDuration(1);
        setPermanent(40);
        triggerAnim("end","end");
        Vec3 pos = this.position();
        AABB aabb = new AABB(pos.add(-18, -18, -18), pos.add(18, 18, 18));
        for(int i=0;i<16;i++) CaerulaArborMod.queueServerWork(i * 5,()->{
            if(!this.isCrowded()) this.spawnSeaborn();
            List<LivingEntity> ents = ents = this.level().getEntitiesOfClass(LivingEntity.class, aabb, e -> isLegalTarget(e) && this.distanceTo(e) <= 18);
            float damage = (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 3);
            ents.forEach(ent -> ent.hurt(this.damageSources().magic(), damage));
        });
    }

    private void rim(double R) {
        if (this.level() instanceof ServerLevel sLevel) {
            for (int i = 0; i < 360; i += 3) {
                double x = this.getX() + R * Math.cos(i * Mth.DEG_TO_RAD),
                        y = this.getY(),
                        z = this.getZ() + R * Math.sin(i * Mth.DEG_TO_RAD);
                sLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                        x, y, z, 16, 0.15, 0.15, 0.15, 0.1);
            }
        }
    }

    private void pushAndDamage(LivingEntity ent) {
        Vec3 vec = ent.position().add(this.position().scale(-1));
        double dx = 0, dy = 0.25, dz = 0;
        if (vec.x != 0) dx = 1.5 / vec.x;
        if (vec.z != 0) dz = 1.5 / vec.z;
        ent.push(dx, dy, dz);
        MobEffectInstance dizzy = new MobEffectInstance(ModMobEffects.DIZZY.get(),60,0,false,false);
        ent.addEffect(dizzy);
        float damage = (float) (this.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.5);
        double rate = this.getAttributeValue(ModAttributes.SANITY_RATE.get());
        ent.hurt(this.level().damageSources().magic(), damage);
        ModCapabilities.getSanityInjury(ent).hurt(damage * rate);
    }

    private void spawnSeaborn(){
        RandomSource randomSource = this.level().random;
        double t = Mth.nextDouble(randomSource,0,Mth.TWO_PI);
        double d = Mth.nextDouble(randomSource,6,18);
        BlockPos pos = this.blockPosition();
        triggerSound(SoundEvents.AMBIENT_UNDERWATER_ENTER);
        int targetX = Math.toIntExact(Math.round(pos.getX() + d * Math.cos(t))),
                targetZ = Math.toIntExact(Math.round(pos.getZ() + Math.sin(t)));
        BlockPos targetPos = findValidY(targetX,pos.getY(),targetZ);
        if(this.level() instanceof ServerLevel sLevel) {
            sLevel.sendParticles(ParticleTypes.CLOUD,
                    targetPos.getX(),targetPos.getY(), targetPos.getZ(),
                    64, 1, 1, 1, 0.1);
            choseUnderTidesEntity().spawn(sLevel, targetPos, MobSpawnType.MOB_SUMMONED);
            BlockState toFall = ModBlocks.NETHERSEA_BRAND_GROWN.get().defaultBlockState();
            if(toFall.canSurvive(sLevel,targetPos)
                && GameplayConfig.ENABLE_MOB_BREAK.get()
                    && sLevel.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
                FallingBlockEntity.fall(sLevel,targetPos.above(6),toFall);
        }
    }

    private BlockPos findValidY(int x, int y, int z){
        Level level = this.level();
        int y0 = level.getHeight(Heightmap.Types.WORLD_SURFACE,x,z);
        if (Math.abs(y0 - y) <= 6) return new BlockPos(x,y0,z);
        BlockPos pos = new BlockPos(x,y,z);
        for(int dy=0;dy<=6;dy++){
            BlockPos newPos = pos.above(dy);
            if(!level.isLoaded(newPos))continue;
            if((level.isEmptyBlock(newPos) || level.isWaterAt(newPos))
                    &&(level.isEmptyBlock(newPos.above()) || level.isWaterAt(newPos.above())))return newPos;
            newPos = pos.below(dy);
            if((level.isEmptyBlock(newPos) || level.isWaterAt(newPos))
                    &&(level.isEmptyBlock(newPos.above()) || level.isWaterAt(newPos.above())))return newPos;
        }
        return pos;
    }

    private List<EntityType<?>> initUnderTidesEntities(){
        List<EntityType<?>> candidate = new ArrayList<>();
        candidate.add(ModEntities.SHELL_SEA_RUNNER.get());
        candidate.add(ModEntities.DEEP_SEA_SLIDER.get());
        candidate.add(ModEntities.RIDGE_SEA_SPITTER.get());
        candidate.add(ModEntities.BASIN_SEA_REAPER.get());
        candidate.add(ModEntities.POCKET_SEA_CRAWLER.get());
        candidate.add(ModEntities.FLOATING_SEA_DRIFTER.get());
        candidate.add(ModEntities.PRIMAL_SEA_PIERCER.get());
        return candidate;
    }

    private EntityType<?> choseUnderTidesEntity(){
        int index = Mth.nextInt(this.level().random,0,6);
        return this.candidate.get(index);
    }

    @SubscribeEvent
    public static void onEntityAttacked(LivingHurtEvent event) {
        var source = event.getSource();
        var attacker = source.getEntity();
        if (!(attacker instanceof QuintusEntity quintusEntity)) return;
        float perc = quintusEntity.getHealth() / quintusEntity.getMaxHealth();
        float rate = 1;
        if (perc < 0.33) rate = 1.4f;
        else if (perc < 0.67) rate = 1.8f;
        event.setAmount(event.getAmount() * rate);
    }
}
