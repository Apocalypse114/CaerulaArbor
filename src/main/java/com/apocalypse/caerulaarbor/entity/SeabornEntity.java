package com.apocalypse.caerulaarbor.entity;

//孩子不懂事复制写着玩的

import com.apocalypse.caerulaarbor.init.ModMobEffects;
//import com.apocalypse.caerulaarbor.network.CaerulaArborModVariables;
//import com.apocalypse.caerulaarbor.procedures.OceanizedPlayerProcedure;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.entity.monster.Illusioner;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.monster.Vindicator;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class SeabornEntity extends Monster {

    protected SeabornEntity(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.5, false) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return this.mob.getBbWidth() * this.mob.getBbWidth() + entity.getBbWidth();
            }
        });
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 0.5, true) {
            @Override
            protected double getAttackReachSqr(@NotNull LivingEntity entity) {
                return 1;
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, false, false) {
            @Override
            public boolean canUse() {
                double x = SeabornEntity.this.getX();
                double y = SeabornEntity.this.getY();
                double z = SeabornEntity.this.getZ();
                Level world = SeabornEntity.this.level();
                return super.canUse(); //&& canAttackPlayer();
            }

            @Override
            public boolean canContinueToUse() {
                double x = SeabornEntity.this.getX();
                double y = SeabornEntity.this.getY();
                double z = SeabornEntity.this.getZ();
                Entity entity = SeabornEntity.this;
                Level world = SeabornEntity.this.level();
                return super.canContinueToUse();
            }
        });
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, false, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, SnowGolem.class, false, false));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Villager.class, false, false));
        this.targetSelector.addGoal(7, new NearestAttackableTargetGoal<>(this, Illusioner.class, false, false));
        this.targetSelector.addGoal(8, new NearestAttackableTargetGoal<>(this, Pillager.class, false, false));
        this.targetSelector.addGoal(9, new NearestAttackableTargetGoal<>(this, Vindicator.class, false, false));
        this.targetSelector.addGoal(10, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(11, new RandomStrollGoal(this, 0.5));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(13, new FloatGoal(this));
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.is(DamageTypes.DROWN))
            return false;
        if (SeabornEntity.this.hasEffect(ModMobEffects.INVULNERABLE.get()))
            return false;
        return super.hurt(source, amount);
    }
//    public boolean canAttackPlayer(Entity player){
//        if(!(player instanceof Player))
//            return true;
//        return !player.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY)
//                .map(cap -> cap.player_oceanization == 3).orElse(false);
//    }
}
