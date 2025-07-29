
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.init.ModParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

public class SacreficeMobEffect extends InvisibleMobEffect {
    public SacreficeMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -3407872);
        this.addAttributeModifier(Attributes.MAX_HEALTH, "3febc167-27ee-37c3-b059-24127f4bc396", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        LevelAccessor world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if (ModCapabilities.getPlayerVariables(entity).kingShowPtc) {
            if (amplifier < 1) {
                world.addParticle(ModParticleTypes.ARCHFIEND_KEEP.get(),
                        x + Mth.nextDouble(RandomSource.create(), -0.55, 0.55),
                        y + Mth.nextDouble(RandomSource.create(), 0, entity.getBbHeight() * 0.6),
                        z + Mth.nextDouble(RandomSource.create(), -0.55, 0.55),
                        Math.sin(Mth.nextDouble(RandomSource.create(), 0, 6.283)),
                        0.05,
                        Math.cos(Mth.nextDouble(RandomSource.create(), 0, 6.283))
                );
            } else {
                world.addParticle(ModParticleTypes.ARCHFIEND_RESEV.get(),
                        x + Mth.nextDouble(RandomSource.create(), -0.55, 0.55),
                        y + Mth.nextDouble(RandomSource.create(), 0, entity.getBbHeight() * 0.6),
                        z + Mth.nextDouble(RandomSource.create(), -0.55, 0.55),
                        Math.sin(Mth.nextDouble(RandomSource.create(), 0, 6.283)),
                        0.05,
                        Math.cos(Mth.nextDouble(RandomSource.create(), 0, 6.283))
                );
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
