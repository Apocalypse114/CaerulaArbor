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
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.ServerLevelAccessor;
//import net.minecraft.world.level.LevelReader;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.entity.monster.RangedAttackMob;
//import net.minecraft.world.entity.monster.Monster;
//import net.minecraft.world.entity.animal.TropicalFish;
//import net.minecraft.world.entity.animal.Squid;
//import net.minecraft.world.entity.animal.Salmon;
//import net.minecraft.world.entity.animal.Pufferfish;
//import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
//import net.minecraft.world.entity.ai.navigation.PathNavigation;
//import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
//import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
//import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
//import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
//import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
//import net.minecraft.world.entity.ai.goal.Goal;
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
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.InteractionHand;
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
//
//import net.mcreator.caerulaarbor.procedures.OceanizedPlayerProcedure;
//import net.mcreator.caerulaarbor.procedures.FloaterInitProcedure;
//import net.mcreator.caerulaarbor.procedures.ContainFishProcedure;
//import net.mcreator.caerulaarbor.procedures.CanAttackAnimalsProcedure;
//import net.mcreator.caerulaarbor.init.CaerulaArborModEntities;
//
//import javax.annotation.Nullable;
//
//import java.util.EnumSet;
//
//public class FloaterProkaryoteEntity extends Monster implements RangedAttackMob, GeoEntity {
//	public static final EntityDataAccessor<Boolean> SHOOT = SynchedEntityData.defineId(FloaterProkaryoteEntity.class, EntityDataSerializers.BOOLEAN);
//	public static final EntityDataAccessor<String> ANIMATION = SynchedEntityData.defineId(FloaterProkaryoteEntity.class, EntityDataSerializers.STRING);
//	public static final EntityDataAccessor<String> TEXTURE = SynchedEntityData.defineId(FloaterProkaryoteEntity.class, EntityDataSerializers.STRING);
//	private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
//	private boolean swinging;
//	private boolean lastloop;
//	private long lastSwing;
//	public String animationprocedure = "empty";
//
//	public FloaterProkaryoteEntity(PlayMessages.SpawnEntity packet, Level world) {
//		this(CaerulaArborModEntities.FLOATER_PROKARYOTE.get(), world);
//	}
//
//	public FloaterProkaryoteEntity(EntityType<FloaterProkaryoteEntity> type, Level world) {
//		super(type, world);
//		xpReward = 5;
//		setNoAi(false);
//		setMaxUpStep(0.6f);
//		this.setPathfindingMalus(BlockPathTypes.WATER, 0);
//		this.moveControl = new MoveControl(this) {
//			@Override
//			public void tick() {
//				if (FloaterProkaryoteEntity.this.isInWater())
//					FloaterProkaryoteEntity.this.setDeltaMovement(FloaterProkaryoteEntity.this.getDeltaMovement().add(0, 0.005, 0));
//				if (this.operation == Operation.MOVE_TO && !FloaterProkaryoteEntity.this.getNavigation().isDone()) {
//					double dx = this.wantedX - FloaterProkaryoteEntity.this.getX();
//					double dy = this.wantedY - FloaterProkaryoteEntity.this.getY();
//					double dz = this.wantedZ - FloaterProkaryoteEntity.this.getZ();
//					float f = (float) (Mth.atan2(dz, dx) * (double) (180 / Math.PI)) - 90;
//					float f1 = (float) (this.speedModifier * FloaterProkaryoteEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
//					FloaterProkaryoteEntity.this.setYRot(this.rotlerp(FloaterProkaryoteEntity.this.getYRot(), f, 10));
//					FloaterProkaryoteEntity.this.yBodyRot = FloaterProkaryoteEntity.this.getYRot();
//					FloaterProkaryoteEntity.this.yHeadRot = FloaterProkaryoteEntity.this.getYRot();
//					if (FloaterProkaryoteEntity.this.isInWater()) {
//						FloaterProkaryoteEntity.this.setSpeed((float) FloaterProkaryoteEntity.this.getAttribute(Attributes.MOVEMENT_SPEED).getValue());
//						float f2 = -(float) (Mth.atan2(dy, (float) Math.sqrt(dx * dx + dz * dz)) * (180 / Math.PI));
//						f2 = Mth.clamp(Mth.wrapDegrees(f2), -85, 85);
//						FloaterProkaryoteEntity.this.setXRot(this.rotlerp(FloaterProkaryoteEntity.this.getXRot(), f2, 5));
//						float f3 = Mth.cos(FloaterProkaryoteEntity.this.getXRot() * (float) (Math.PI / 180.0));
//						FloaterProkaryoteEntity.this.setZza(f3 * f1);
//						FloaterProkaryoteEntity.this.setYya((float) (f1 * dy));
//					} else {
//						FloaterProkaryoteEntity.this.setSpeed(f1 * 0.05F);
//					}
//				} else {
//					FloaterProkaryoteEntity.this.setSpeed(0);
//					FloaterProkaryoteEntity.this.setYya(0);
//					FloaterProkaryoteEntity.this.setZza(0);
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
//		this.entityData.define(TEXTURE, "floater");
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
//		this.targetSelector.addGoal(2, new NearestAttackableTargetGoal(this, Pufferfish.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canUse() && CanAttackAnimalsProcedure.execute();
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canContinueToUse() && CanAttackAnimalsProcedure.execute();
//			}
//		});
//		this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, GlowSquid.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canUse() && CanAttackAnimalsProcedure.execute();
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canContinueToUse() && CanAttackAnimalsProcedure.execute();
//			}
//		});
//		this.targetSelector.addGoal(4, new NearestAttackableTargetGoal(this, Squid.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canUse() && CanAttackAnimalsProcedure.execute();
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canContinueToUse() && CanAttackAnimalsProcedure.execute();
//			}
//		});
//		this.targetSelector.addGoal(5, new NearestAttackableTargetGoal(this, TropicalFish.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canUse() && CanAttackAnimalsProcedure.execute();
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canContinueToUse() && CanAttackAnimalsProcedure.execute();
//			}
//		});
//		this.targetSelector.addGoal(6, new NearestAttackableTargetGoal(this, Salmon.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canUse() && CanAttackAnimalsProcedure.execute();
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canContinueToUse() && CanAttackAnimalsProcedure.execute();
//			}
//		});
//		this.targetSelector.addGoal(7, new NearestAttackableTargetGoal(this, Player.class, true, false) {
//			@Override
//			public boolean canUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canUse() && OceanizedPlayerProcedure.execute(world, x, y, z);
//			}
//
//			@Override
//			public boolean canContinueToUse() {
//				double x = FloaterProkaryoteEntity.this.getX();
//				double y = FloaterProkaryoteEntity.this.getY();
//				double z = FloaterProkaryoteEntity.this.getZ();
//				Entity entity = FloaterProkaryoteEntity.this;
//				Level world = FloaterProkaryoteEntity.this.level();
//				return super.canContinueToUse() && OceanizedPlayerProcedure.execute(world, x, y, z);
//			}
//		});
//		this.goalSelector.addGoal(8, new RandomSwimmingGoal(this, 3.5, 40));
//		this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
//		this.goalSelector.addGoal(1, new RangedAttackGoal(this, 1.25, 60, 5f) {
//			@Override
//			public boolean canContinueToUse() {
//				return this.canUse();
//			}
//		});
//	}
//
//	public class RangedAttackGoal extends Goal {
//		private final Mob mob;
//		private final RangedAttackMob rangedAttackMob;
//		@Nullable
//		private LivingEntity target;
//		private int attackTime = -1;
//		private final double speedModifier;
//		private int seeTime;
//		private final int attackIntervalMin;
//		private final int attackIntervalMax;
//		private final float attackRadius;
//		private final float attackRadiusSqr;
//
//		public RangedAttackGoal(RangedAttackMob p_25768_, double p_25769_, int p_25770_, float p_25771_) {
//			this(p_25768_, p_25769_, p_25770_, p_25770_, p_25771_);
//		}
//
//		public RangedAttackGoal(RangedAttackMob p_25773_, double p_25774_, int p_25775_, int p_25776_, float p_25777_) {
//			if (!(p_25773_ instanceof LivingEntity)) {
//				throw new IllegalArgumentException("ArrowAttackGoal requires Mob implements RangedAttackMob");
//			} else {
//				this.rangedAttackMob = p_25773_;
//				this.mob = (Mob) p_25773_;
//				this.speedModifier = p_25774_;
//				this.attackIntervalMin = p_25775_;
//				this.attackIntervalMax = p_25776_;
//				this.attackRadius = p_25777_;
//				this.attackRadiusSqr = p_25777_ * p_25777_;
//				this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
//			}
//		}
//
//		public boolean canUse() {
//			LivingEntity livingentity = this.mob.getTarget();
//			if (livingentity != null && livingentity.isAlive()) {
//				this.target = livingentity;
//				return true;
//			} else {
//				return false;
//			}
//		}
//
//		public boolean canContinueToUse() {
//			return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
//		}
//
//		public void stop() {
//			this.target = null;
//			this.seeTime = 0;
//			this.attackTime = -1;
//			((FloaterProkaryoteEntity) rangedAttackMob).entityData.set(SHOOT, false);
//		}
//
//		public boolean requiresUpdateEveryTick() {
//			return true;
//		}
//
//		public void tick() {
//			double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
//			boolean flag = this.mob.getSensing().hasLineOfSight(this.target);
//			if (flag) {
//				++this.seeTime;
//			} else {
//				this.seeTime = 0;
//			}
//			if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 5) {
//				this.mob.getNavigation().stop();
//			} else {
//				this.mob.getNavigation().moveTo(this.target, this.speedModifier);
//			}
//			this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
//			if (--this.attackTime == 0) {
//				if (!flag) {
//					((FloaterProkaryoteEntity) rangedAttackMob).entityData.set(SHOOT, false);
//					return;
//				}
//				((FloaterProkaryoteEntity) rangedAttackMob).entityData.set(SHOOT, true);
//				float f = (float) Math.sqrt(d0) / this.attackRadius;
//				float f1 = Mth.clamp(f, 0.1F, 1.0F);
//				this.rangedAttackMob.performRangedAttack(this.target, f1);
//				this.attackTime = Mth.floor(f * (float) (this.attackIntervalMax - this.attackIntervalMin) + (float) this.attackIntervalMin);
//			} else if (this.attackTime < 0) {
//				this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / (double) this.attackRadius, (double) this.attackIntervalMin, (double) this.attackIntervalMax));
//			} else
//				((FloaterProkaryoteEntity) rangedAttackMob).entityData.set(SHOOT, false);
//		}
//	}
//
//	@Override
//	public MobType getMobType() {
//		return MobType.WATER;
//	}
//
//	@Override
//	public SoundEvent getAmbientSound() {
//		return ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.glow_squid.ambient"));
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
//		if (source.is(DamageTypes.FALL))
//			return false;
//		if (source.is(DamageTypes.DROWN))
//			return false;
//		return super.hurt(source, amount);
//	}
//
//	@Override
//	public SpawnGroupData finalizeSpawn(ServerLevelAccessor world, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData livingdata, @Nullable CompoundTag tag) {
//		SpawnGroupData retval = super.finalizeSpawn(world, difficulty, reason, livingdata, tag);
//		FloaterInitProcedure.execute(this);
//		return retval;
//	}
//
//	@Override
//	public void addAdditionalSaveData(CompoundTag compound) {
//		super.addAdditionalSaveData(compound);
//		compound.putString("Texture", this.getTexture());
//	}
//
//	@Override
//	public void readAdditionalSaveData(CompoundTag compound) {
//		super.readAdditionalSaveData(compound);
//		if (compound.contains("Texture"))
//			this.setTexture(compound.getString("Texture"));
//	}
//
//	@Override
//	public InteractionResult mobInteract(Player sourceentity, InteractionHand hand) {
//		ItemStack itemstack = sourceentity.getItemInHand(hand);
//		InteractionResult retval = InteractionResult.sidedSuccess(this.level().isClientSide());
//		super.mobInteract(sourceentity, hand);
//		double x = this.getX();
//		double y = this.getY();
//		double z = this.getZ();
//		Entity entity = this;
//		Level world = this.level();
//		return ContainFishProcedure.execute(entity, sourceentity);
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
//		return super.getDimensions(p_33597_).scale((float) 0.75);
//	}
//
//	@Override
//	public void performRangedAttack(LivingEntity target, float flval) {
//		FishSplashEntity.shoot(this, target);
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
//		SpawnPlacements.register(CaerulaArborModEntities.FLOATER_PROKARYOTE.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
//				(entityType, world, reason, pos, random) -> (world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)));
//	}
//
//	public static AttributeSupplier.Builder createAttributes() {
//		AttributeSupplier.Builder builder = Mob.createMobAttributes();
//		builder = builder.add(Attributes.MOVEMENT_SPEED, 0.4);
//		builder = builder.add(Attributes.MAX_HEALTH, 20);
//		builder = builder.add(Attributes.ARMOR, 0);
//		builder = builder.add(Attributes.ATTACK_DAMAGE, 3);
//		builder = builder.add(Attributes.FOLLOW_RANGE, 16);
//		builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 1);
//		builder = builder.add(ForgeMod.SWIM_SPEED.get(), 0.4);
//		return builder;
//	}
//
//	private PlayState movementPredicate(AnimationState event) {
//		if (this.animationprocedure.equals("empty")) {
//			if (this.isDeadOrDying()) {
//				return event.setAndContinue(RawAnimation.begin().thenPlay("animation.floater.die"));
//			}
//			return event.setAndContinue(RawAnimation.begin().thenLoop("animation.floater.idle"));
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
//		if (this.swinging && this.lastSwing + 20L <= level().getGameTime()) {
//			this.swinging = false;
//		}
//		if ((this.swinging || this.entityData.get(SHOOT)) && event.getController().getAnimationState() == AnimationController.State.STOPPED) {
//			event.getController().forceAnimationReset();
//			return event.setAndContinue(RawAnimation.begin().thenPlay("animation.floater.attack"));
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
