package com.apocalypse.caerulaarbor.capability;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.capability.sanity.SanityInjuryCapability;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ModCapabilities {

    public static final Capability<SanityInjuryCapability> SANITY_INJURY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<PlayerVariable> PLAYER_VARIABLE = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<AnchorRecord> ANCHOR_RECORD = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static SanityInjuryCapability getSanityInjury(LivingEntity entity) {
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

    public static AnchorRecord getAnchorRecord(ServerLevel level) {
        return level.getCapability(ANCHOR_RECORD).orElseGet(
                () -> {
                    CaerulaArborMod.LOGGER.warn("Failed to get anchor record for level {} ", level.dimension().location());
                    return new AnchorRecord();
                }
        );
    }
}
