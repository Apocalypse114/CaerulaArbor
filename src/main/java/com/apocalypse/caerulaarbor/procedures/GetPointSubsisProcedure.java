//package com.apocalypse.caerulaarbor.procedures;
//
//import com.apocalypse.caerulaarbor.capability.map.MapVariables;
//import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
//import net.minecraft.world.level.LevelAccessor;
//
//public class GetPointSubsisProcedure {
//	public static String execute(LevelAccessor world) {
//		if (MapVariables.get(world).strategySubsisting >= 4) {
//			return "§bFinished";
//		}
//		return Math.round(MapVariables.get(world).evoPointSubsisting) + "§b/"
//				+ Math.round(Math.pow(MapVariables.get(world).strategySubsisting + 1, 3) * GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get());
//	}
//}
