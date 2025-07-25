//package com.apocalypse.caerulaarbor.procedures;
//
//import com.apocalypse.caerulaarbor.capability.map.MapVariables;
//import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
//import net.minecraft.world.level.LevelAccessor;
//
//public class GetBarGrowProcedure {
//	public static double execute(LevelAccessor world) {
//		double rate;
//		if (MapVariables.get(world).strategyGrow >= 4) {
//			return 18;
//		}
//		rate = MapVariables.get(world).evoPointGrow / (Math.pow(MapVariables.get(world).strategyGrow + 1, 3) * GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get());
//		if (rate > 1) {
//			rate = 1;
//		}
//		return Math.round(18 * rate);
//	}
//}
