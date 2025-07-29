
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.init.ModParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class HaemophiliaMobEffect extends InvisibleMobEffect {
    public HaemophiliaMobEffect() {
        super(MobEffectCategory.NEUTRAL, -3381505);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (Math.round(entity.getHealth()) >= Math.round(entity.getMaxHealth())) return;

        double health_cur = Math.max(0.5, entity.getHealth() * 0.9);

        if (entity.getHealth() > 0.5 && entity.isAlive()) {
            entity.setHealth((float) health_cur);
            for (int index0 = 0; index0 < 24; index0++) {
                var random = entity.level().random;

                entity.level().addParticle(ModParticleTypes.BLOODOOZE.get(),
                        entity.getX(),
                        entity.getY() + 1.33,
                        entity.getZ(),
                        Mth.nextDouble(random, -1.25, 1.25),
                        Mth.nextDouble(random, -0.05, 0.05),
                        Mth.nextDouble(random, -1.25, 1.25)
                );
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }
}
