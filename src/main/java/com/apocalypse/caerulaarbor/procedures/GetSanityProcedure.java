package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.sanity.SanityInjuryCapability;
import net.minecraft.world.entity.LivingEntity;

public class GetSanityProcedure {
	public static String execute(LivingEntity entity) {
		if (entity == null)
			return "";
		var cap = entity.getCapability(ModCapabilities.SANITY_INJURY).resolve();
		if (cap.isEmpty()) return "";
		if (!(cap.get() instanceof SanityInjuryCapability capImpl)) return "";
		return "" + Math.round(capImpl.getValue());
	}
}
