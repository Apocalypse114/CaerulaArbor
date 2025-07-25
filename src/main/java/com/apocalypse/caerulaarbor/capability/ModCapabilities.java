package com.apocalypse.caerulaarbor.capability;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.capability.sanity.ISanityInjuryCapability;
import com.apocalypse.caerulaarbor.capability.sanity.SanityInjuryCapability;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ModCapabilities {

    public static final Capability<ISanityInjuryCapability> SANITY_INJURY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<PlayerVariable> PLAYER_VARIABLE = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static ISanityInjuryCapability getSanityInjury(LivingEntity entity) {
        return entity.getCapability(SANITY_INJURY).orElseGet(
                () -> {
                    CaerulaArborMod.LOGGER.warn("Failed to get capability {} for entity {} ", SANITY_INJURY, entity);
                    return new SanityInjuryCapability(entity);
                }
        );
    }

    public static PlayerVariable getPlayerVariables(Entity entity) {
        return entity.getCapability(PLAYER_VARIABLE).orElseGet(
                () -> {
                    CaerulaArborMod.LOGGER.warn("Failed to get capability {} for entity {} ", PLAYER_VARIABLE, entity);
                    return new PlayerVariable();
                }
        );
    }
}
