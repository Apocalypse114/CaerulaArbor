package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class GetOceanizeStateProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		if ((entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable())).seabornization == 3) {
			return Component.translatable("item.caerula_arbor.language_key.description_13").getString();
		} else {
			if ((entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable())).seabornization == 2) {
				return Component.translatable("item.caerula_arbor.language_key.description_12").getString();
			} else {
				if ((entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable())).seabornization == 1) {
					return Component.translatable("item.caerula_arbor.language_key.description_11").getString();
				}
			}
		}
		return Component.translatable("item.caerula_arbor.language_key.description_10").getString();
	}
}
