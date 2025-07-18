package com.apocalypse.caerulaarbor.capability;

import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.capability.sanity.ISanityInjuryCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class ModCapabilities {

    public static final Capability<ISanityInjuryCapability> SANITY_INJURY = CapabilityManager.get(new CapabilityToken<>() {
    });
    public static final Capability<PlayerVariable> PLAYER_VARIABLE = CapabilityManager.get(new CapabilityToken<>() {
    });
}
