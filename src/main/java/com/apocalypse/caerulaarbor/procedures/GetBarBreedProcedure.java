package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.network.CaerulaArborModVariables;
import net.minecraft.world.level.LevelAccessor;

public class GetBarBreedProcedure {
	public static double execute(LevelAccessor world) {
		double rate = 0;
		if (CaerulaArborModVariables.MapVariables.get(world).strategyBreed >= 4) {
			return 18;
		}
		rate = CaerulaArborModVariables.MapVariables.get(world).evo_point_breed / (Math.pow(CaerulaArborModVariables.MapVariables.get(world).strategyBreed + 1, 3) * (double) GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get());
		if (rate > 1) {
			rate = 1;
		}
		return Math.round(18 * rate);
	}
}
