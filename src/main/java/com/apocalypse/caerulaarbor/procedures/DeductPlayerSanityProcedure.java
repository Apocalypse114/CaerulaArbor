package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class DeductPlayerSanityProcedure {
    public static void execute(Entity entity, double value) {
        if (!(entity instanceof Player player)) return;
        player.getCapability(ModCapabilities.SANITY_INJURY)
                .ifPresent(cap -> cap.hurt(value));
    }
}
