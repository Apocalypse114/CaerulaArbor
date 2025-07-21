//
//package com.apocalypse.caerulaarbor.entity;
//
//import software.bernie.geckolib.util.GeckoLibUtil;
//import software.bernie.geckolib.core.object.PlayState;
//import software.bernie.geckolib.core.animation.RawAnimation;
//import software.bernie.geckolib.core.animation.AnimationState;
//import software.bernie.geckolib.core.animation.AnimationController;
//import software.bernie.geckolib.core.animation.AnimatableManager;
//import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
//import software.bernie.geckolib.animatable.GeoEntity;
//
//import net.minecraftforge.registries.ForgeRegistries;
//import net.minecraftforge.network.PlayMessages;
//import net.minecraftforge.network.NetworkHooks;
//import net.minecraftforge.common.ForgeMod;
//
//import net.minecraft.world.level.pathfinder.BlockPathTypes;
//import net.minecraft.world.level.levelgen.Heightmap;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.ServerLevelAccessor;
//import net.minecraft.world.level.LevelReader;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.monster.Monster;
//import net.minecraft.world.entity.animal.TropicalFish;
//import net.minecraft.world.entity.animal.Squid;
//import net.minecraft.world.entity.animal.Salmon;
//import net.minecraft.world.entity.animal.Pufferfish;
//import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
//import net.minecraft.world.entity.ai.navigation.PathNavigation;
//import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
//import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
//import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
//import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
//import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
//import net.minecraft.world.entity.ai.control.MoveControl;
//import net.minecraft.world.entity.ai.attributes.Attributes;
//import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
//import net.minecraft.world.entity.SpawnPlacements;
//import net.minecraft.world.entity.SpawnGroupData;
//import net.minecraft.world.entity.Pose;
//import net.minecraft.world.entity.MobType;
//import net.minecraft.world.entity.MobSpawnType;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.GlowSquid;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.entity.EntityDimensions;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.damagesource.DamageTypes;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.DifficultyInstance;
//import net.minecraft.util.Mth;
//import net.minecraft.sounds.SoundEvent;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.network.syncher.SynchedEntityData;
//import net.minecraft.network.syncher.EntityDataSerializers;
//import net.minecraft.network.syncher.EntityDataAccessor;
//import net.minecraft.network.protocol.game.ClientGamePacketListener;
//import net.minecraft.network.protocol.Packet;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.core.BlockPos;
//
//import net.mcreator.caerulaarbor.procedures.OceanizedPlayerProcedure;
//import net.mcreator.caerulaarbor.procedures.InitBplusMagicProcedure;
//import net.mcreator.caerulaarbor.procedures.CanAttackAnimalsProcedure;
//import net.mcreator.caerulaarbor.procedures.AccumulatorSplitProcedure;
//import net.mcreator.caerulaarbor.init.CaerulaArborModEntities;
//
//import javax.annotation.Nullable;
//
//public class AccumulatorProkaryoteEntity extends Monster implements GeoEntity {
//	public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(AccumulatorProkaryoteEntity.class, EntityDataSerializers.BOOLEAN);
//	public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(AccumulatorProkaryoteEntity.class, EntityDataSerializers.STRING);
//	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(AccumulatorProkaryoteEntity.class, EntityDataSerializers.STRING);
//	public static final EntityDataAccessor<Boolean> DATA_split = SynchedEntityData.defineId(AccumulatorProkaryoteEntity.class, EntityDataSerializers.BOOLEAN);
//	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
//	private boolean swinging;
//	private boolean lastloop;
//	private long lastSwing;
//	public String animationprocedure = "empty";
//
//	public AccumulatorProkaryoteEntity(PlayMessages.SpawnEntity packet, Level world) {
//		this(CaerulaArborModEntities.ACCUMULATOR_PROKARYOTE.get(), world);
//	}
//
//	public AccumulatorProkaryoteEntity(EntityType<AccumulatorProkaryoteEntity> type, Level world) {
//		super(type, world);
//		xpReward = 0;
//		setNoAi(false);
//		setMaxUpStep(0.6f);
//		this.setPathfindingMalus(BlockPathTypes.WATER, 0);
//		this.moveControl = new MoveControl(this) {
//			@Override
//			public void tick() {
//				if (AccumulatorProkaryoteEntity.this.isInWater())
//					AccumulatorProkaryoteEntity.this.setDeltaMovement(AccumulatorProkaryoteEntity.this.getDeltaMovement().add(0, 0.005, 0));
//				if (this.operation == Operation.MOVE_TO && !AccumulatorProkaryoteEntity.this.getNavigation().isDone()) {
//					double dx = this.wantedX - AccumulatorProkaryoteEntity.this.getX();
//					double dy = this.wantedY - AccumulatorProkaryoteEntity.this.getY();
//					double dz = this.wantedZ - AccumulatorProkaryoteEntity.this.getZ();
//					float f = (float) (Mth.atan2(dz, dx) * (double) (180 / Math.PI)) - 90;
//					float f1 = (float) (this.speedModifier * AccumulatorProkaryoteEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
//					AccumulatorProkaryoteEntity.this.setYRot(this.rotlerp(AccumulatorProkaryoteEntity.this.getYRot(), f, 10));
//					AccumulatorProkaryoteEntity.this.yBodyRot = AccumulatorProkaryoteEntity.this.getYRot();
//					AccumulatorProkaryoteEntity.this.yHeadRot = AccumulatorProkaryoteEntity.this.getYRot();
//					if (AccumulatorProkaryoteEntity.this.isInWater()) {
//						AccumulatorProkaryoteEntity.this.setSpeed((float) AccumulatorProkaryoteEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
//						float f2 = -(float) (Mth.atan2(dy, (float) Math.sqrt(dx * dx + dz * dz)) * (180 / Math.PI));
//						f2 = Mth.clamp(Mth.wrapDegrees(f2), -85, 85);
//						AccumulatorProkaryoteEntity.this.setXRot(this.rotlerp(AccumulatorProkaryoteEntity.this.getXRot(), f2, 5));
//						float f3 = Mth.cos(AccumulatorProkaryoteEntity.this.getXRot() * (float) (Math.PI / 180.0));
//						AccumulatorProkaryoteEntity.this.setZza(f3 * f1);
//						AccumulatorProkaryoteEntity.this.setYya((float) (f1 * dy));
//					} else {
//						AccumulatorProkaryoteEntity.this.setSpeed(f1 * 0.05F);
//					}
//				} else {
//					AccumulatorProkaryoteEntity.this.setSpeed(0);
//					AccumulatorProkaryoteEntity.this.setYya(0);
//					AccumulatorProkaryoteEntity.this.setZza(0);
//				}
//			}
//		};
//	}
//
//	@Override
//	protected void defineSynchedData() {
//		super.defineSynchedData();
//		this.entityData.define(SHOOT, false);
//		this.entityData.define(ANIMATION, "undefined");
//		this.entityData.define(TEXTURE, "divicellular_hoarder");
//		this.entityData.define(DATA_split, true);
//	}
//
//	public void setTexture(String texture) {
//		this.entityData.set(TEXTURE, texture);
//	}
//
//	public String getTexture() {
//		return this.entityData.get(TEXTURE);
//	}
//
//	@Override
//	public Packet<ClientGamePacketListener> getAddEntityPacket() {
//		return NetworkHooks.getEntitySpawningPacket(this);
//	}
//
//	@Override
//	protected PathNavigation createNavigation(Level world) {
//		return new WaterBoundPathNavigation(this, world);
//	}
//
//	@Override
//	protected void registerGoals() {
//		super.registerGoals();
//		this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
//		this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 5, false) {
//			@Override
//			protected double getAttackReachSqr(LivingEntity entity) {
//				return 2.25;
//			}
//		});
//		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, Pufferfish.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canUse() && CanAttackAnimalsProcedure.execute();
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canContinueToUse() && CanAttackAnimalsProcedure.execute();
//			}
//		});
//		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, GlowSquid.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canUse() && CanAttackAnimalsProcedure.execute();
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canContinueToUse() && CanAttackAnimalsProcedure.execute();
//			}
//		});
//		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, Squid.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canUse() && CanAttackAnimalsProcedure.execute();
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canContinueToUse() && CanAttackAnimalsProcedure.execute();
//			}
//		});
//		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, TropicalFish.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canUse() && CanAttackAnimalsProcedure.execute();
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canContinueToUse() && CanAttackAnimalsProcedure.execute();
//			}
//		});
//		this.targetSelector.addGoal(7, new NearestAttackableTargetGoal(this, Salmon.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canUse() && CanAttackAnimalsProcedure.execute();
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canContinueToUse() && CanAttackAnimalsProcedure.execute();
//			}
//		});
//		this.targetSelector.addGoal(8, new NearestAttackableTargetGoal(this, Player.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canUse() && OceanizedPlayerProcedure.execute(world, x, y, z);
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = AccumulatorProkaryoteEntity.this.getX();
//				double y = AccumulatorProkaryoteEntity.this.getY();
//				double z = AccumulatorProkaryoteEntity.this.getZ();
//				Entity entity = AccumulatorProkaryoteEntity.this;
//				Level world = AccumulatorProkaryoteEntity.this.level();
//				return super.canContinueToUse() && OceanizedPlayerProcedure.execute(world, x, y, z);
//			}
//		});
//		this.goalSelector.addGoal(9, new RandomSwimmingGoal(this, 4, 40));
//		this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
//	}
//
//	@Override
//	public MobType getMobType() {
//		return MobType.WATER;
//	}
//
//	@Override
//	public void playStepSound(BlockPos pos, BlockState blockIn) {
//		this.playSound(ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.puffer_fish.flop")), 0.15f, 1);
//	}
//
//	@Override
//	public SoundEvent getHurtSound(DamageSource ds) {
//		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.puffer_fish.hurt"));
//	}
//
//	@Override
//	public SoundEvent getDeathSound() {
//		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.puffer_fish.death"));
//	}
//
//	@Override
//	public boolean hurt(DamageSource source, float amount) {
//		AccumulatorSplitProcedure.execute(this.level(), this);
//		if (source.is(DamageTypes.DROWN))
//			return false;
//		return super.hurt(source, amount);
//	}
//
//	@Override
//	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
//		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
//		InitBplusMagicProcedure.execute(this);
//		return retval;
//	}
//
//	@Override
//	public void addAdditionalSaveData(CompoundTag compound) {
//		super.addAdditionalSaveData(compound);
//		compound.putString("Texture", this.getTexture());
//		compound.putBoolean("Datasplit", this.entityData.get(DATA_split));
//	}
//
//	@Override
//	public void readAdditionalSaveData(CompoundTag compound) {
//		super.readAdditionalSaveData(compound);
//		if (compound.contains("Texture"))
//			this.setTexture(compound.getString("Texture"));
//		if (compound.contains("Datasplit"))
//			this.entityData.set(DATA_split, compound.getBoolean("Datasplit"));
//	}
//
//	@Override
//	public void baseTick() {
//		super.baseTick();
//		this.refreshDimensions();
//	}
//
//	@Override
//	public EntityDimensions getDimensions(Pose p_33597_) {
//		return super.getDimensions(p_33597_).scale((float) 1);
//	}
//
//	@Override
//	public boolean canBreatheUnderwater() {
//		return true;
//	}
//
//	@Override
//	public boolean checkSpawnObstruction(LevelReader world) {
//		return world.isUnobstructed(this);
//	}
//
//	@Override
//	public boolean isPushedByFluid() {
//		return false;
//	}
//
//	public static void init() {
//		SpawnPlacements.register(CaerulaArborModEntities.ACCUMULATOR_PROKARYOTE.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
//				(entityType, world, reason, pos, random) -> (world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)));
//	}
//
//	public static AttributeSupplier.Builder createAttributes() {
//		AttributeSupplier.Builder builder = Mob.createMobAttributes();
//		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.5);
//		builder = builder.add(Attributes.MAX_HEALTH, 32);
//		builder = builder.add(Attributes.ARMOR, 0);
//		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
//		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
//		builder = builder.add(ForgeMod.SWIM_SPEED.get(), 0.5);
//		return builder;
//	}
//
//	private PlayState movementPredicate(AnimationState event) {
//		if (this.animationprocedure.equals("empty")) {
//			if ((event.isMoving() || !(event.getLimbSwingAmount() > -0.15F && event.getLimbSwingAmount() < 0.15F))
//
//			) {
//				return event.setAndContinue(RawAnimation.begin().thenLoop("animation.divicellular_hoarder.move"));
//			}
//			return event.setAndContinue(RawAnimation.begin().thenLoop("animation.divicellular_hoarder.idle"));
//		}
//		return PlayState.STOP;
//	}
//
//	private PlayState attackingPredicate(AnimationState event) {
//		double d1 = this.getX() - this.xOld;
//		double d0 = this.getZ() - this.zOld;
//		float velocity = (float) Math.sqrt(d1 * d1 + d0 * d0);
//		if (getAttackAnim(event.getPartialTick()) > 0f && !this.swinging) {
//			this.swinging = true;
//			this.lastSwing = level().getGameTime();
//		}
//		if (this.swinging && this.lastSwing + 10L <= level().getGameTime()) {
//			this.swinging = false;
//		}
//		if (this.swinging && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
//			event.getController().forceAnimationReset();
//			return event.setAndContinue(RawAnimation.begin().thenPlay("animation.divicellular_hoarder.attack"));
//		}
//		return PlayState.CONTINUE;
//	}
//
//	String prevAnim = "empty";
//
//	private PlayState procedurePredicate(AnimationState event) {
//		if (!animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
//			if (!this.animationprocedure.equals(prevAnim))
//				event.getController().forceAnimationReset();
//			event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
//			if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
//				this.animationprocedure = "empty";
//				event.getController().forceAnimationReset();
//			}
//		} else if (animationprocedure.equals("empty")) {
//			prevAnim = "empty";
//			return PlayState.STOP;
//		}
//		prevAnim = this.animationprocedure;
//		return PlayState.CONTINUE;
//	}
//
//	@Override
//	protected void tickDeath() {
//		++this.deathTime;
//		if (this.deathTime == 20) {
//			this.remove(RemovalReason.KILLED);
//			this.dropExperience();
//		}
//	}
//
//	public String getSyncedAnimation() {
//		return this.entityData.get(ANIMATION);
//	}
//
//	public void setAnimation(String animation) {
//		this.entityData.set(ANIMATION, animation);
//	}
//
//	@Override
//	public void registerControllers(AnimatableManager.ControllerRegistrar data) {
//		data.add(new AnimationController<>(this, "movement", 4, this::movementPredicate));
//		data.add(new AnimationController<>(this, "attacking", 4, this::attackingPredicate));
//		data.add(new AnimationController<>(this, "procedure", 4, this::procedurePredicate));
//	}
//
//	@Override
//	public AnimatableInstanceCache getAnimatableInstanceCache() {
//		return this.cache;
//	}
//}
