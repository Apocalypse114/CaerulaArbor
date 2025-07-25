package com.apocalypse.caerulaarbor.capability.sanity;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class SIHelper {
    public static void causeSanityInjury(LivingEntity living, double value) {
        var sanRate = living.getAttribute(ModAttributes.SANITY_RATE.get());
        double sanRateValue = sanRate == null ? 0 : sanRate.getValue();
        double damage = value * sanRateValue;
        ModCapabilities.getSanityInjury(living).hurt(damage);
    }

    public static void causeSanityInjuryWithParticles(LivingEntity living, double value) {
        causeSanityInjury(living, value);
        for (int i = 1; i < 4; i++) {
            CaerulaArborMod.queueServerWork(i * 3, () -> {
                if (living.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                            living.getX(), (living.getY() + 0.5 * living.getBbHeight()), living.getZ(),
                            24, 0.86, 1.2, 0.86, 0.1);
                }
            });
        }
    }
}
