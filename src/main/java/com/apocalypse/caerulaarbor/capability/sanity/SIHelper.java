package com.apocalypse.caerulaarbor.capability.sanity;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;

public class SIHelper {
    public static void causeSanityInjury(LivingEntity living, double value) {
        // 仅在服务端计算，避免客户端重复与视觉假象
        if (living.level().isClientSide) return;
        var sanRate = living.getAttribute(ModAttributes.SANITY_RATE.get());
        double sanRateValue = sanRate == null ? 1 : sanRate.getValue();
        if (sanRateValue <= 0) sanRateValue = 1; // 容错：默认或被清零时按1倍计算
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
