//package com.apocalypse.caerulaarbor.procedures;
//
//import com.apocalypse.caerulaarbor.capability.map.MapVariables;
//import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
//import net.minecraft.world.level.LevelAccessor;
//
//public class GetPointGrowProcedure {
//	public static String execute(LevelAccessor world) {
//		var mapVar = MapVariables.get(world);
//		if (mapVar.strategyGrow >= 4) {
//			return "§bFinished";
//		}
//		return Math.round(mapVar.evoPointGrow) + "§b/"
//				+ Math.round(Math.pow(mapVar.strategyGrow + 1, 3) * GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get());
//	}
//}
