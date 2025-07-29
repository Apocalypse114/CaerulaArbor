package com.apocalypse.caerulaarbor.entity.ai.goal;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class SeaMonsterAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {

    public SeaMonsterAttackableTargetGoal(Mob pMob, Class<T> pTargetType, boolean pMustSee, boolean pMustReach) {
        super(pMob, pTargetType, pMustSee, pMustReach);
    }

    @Override
    public boolean canUse() {
        return super.canUse() && excludeSpecialPlayers();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && excludeSpecialPlayers();
    }

    private boolean excludeSpecialPlayers() {
        return this.mob.level().getEntitiesOfClass(Player.class, new AABB(this.mob.getOnPos()).inflate(16), e -> true)
                .stream()
                .noneMatch(player -> player.getCapability(ModCapabilities.PLAYER_VARIABLE)
                        .map(cap -> cap.seabornization == 3)
                        .orElse(false)
                );
    }
}
