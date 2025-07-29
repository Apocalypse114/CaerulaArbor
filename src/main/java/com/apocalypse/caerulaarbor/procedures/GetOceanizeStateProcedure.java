package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

public class GetOceanizeStateProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
		if ((ModCapabilities.getPlayerVariables(entity)).seabornization == 3) {
			return Component.translatable("item.caerula_arbor.language_key.description_13").getString();
		} else {
			if ((ModCapabilities.getPlayerVariables(entity)).seabornization == 2) {
				return Component.translatable("item.caerula_arbor.language_key.description_12").getString();
			} else {
				if ((ModCapabilities.getPlayerVariables(entity)).seabornization == 1) {
					return Component.translatable("item.caerula_arbor.language_key.description_11").getString();
				}
			}
		}
		return Component.translatable("item.caerula_arbor.language_key.description_10").getString();
	}
}
