
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.init.ModTags;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class AngerOfTideMobEffect extends InvisibleMobEffect {
    public AngerOfTideMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -10092544);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (!(entity instanceof Mob mob && mob.isAggressive())) {
            final Vec3 _center = new Vec3(entity.getX(), entity.getY(), entity.getZ());

            if (entity instanceof Mob mob) {
                entity.level().getEntitiesOfClass(
                                Mob.class,
                                new AABB(_center, _center).inflate(64 / 2d),
                                e -> e.getMaxHealth() >= 7 && !e.getType().is(ModTags.EntityTypes.SEA_BORN)
                        )
                        .stream()
                        .findFirst()
                        .ifPresent(mob::setTarget);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 10 == 0;
    }
}
