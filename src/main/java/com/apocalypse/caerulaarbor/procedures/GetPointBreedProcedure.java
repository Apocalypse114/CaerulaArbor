//package com.apocalypse.caerulaarbor.procedures;
//
//import com.apocalypse.caerulaarbor.capability.map.MapVariables;
//import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
//import net.minecraft.world.level.LevelAccessor;
//
//public class GetPointBreedProcedure {
//	public static String execute(LevelAccessor world) {
//		var mapVar = MapVariables.get(world);
//		if (mapVar.strategyBreed >= 4) {
//			return "§bFinished";
//		}
//		return Math.round(mapVar.evoPointBreed) + "§b/"
//				+ Math.round(Math.pow(mapVar.strategyBreed + 1, 3) * GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get());
//	}
//}
