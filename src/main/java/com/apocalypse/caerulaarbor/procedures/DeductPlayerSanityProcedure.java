package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class DeductPlayerSanityProcedure {
    public static void execute(Entity entity, double value) {
        if (!(entity instanceof Player player)) return;

        ModCapabilities.getSanityInjury(player).hurt(value);
    }
}
