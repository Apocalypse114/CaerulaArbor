package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.network.CaerulaArborModVariables;
import net.minecraft.world.entity.Entity;

public class GetLiveMaxShownProcedure {
	public static String execute(Entity entity) {
		if (entity == null)
			return "";
        return "/" + Math.round((entity.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY).orElse(new CaerulaArborModVariables.PlayerVariables())).maxLive);
	}
}
