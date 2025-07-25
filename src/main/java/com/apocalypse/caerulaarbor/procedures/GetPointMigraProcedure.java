//package com.apocalypse.caerulaarbor.procedures;
//
//import com.apocalypse.caerulaarbor.capability.map.MapVariables;
//import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
//import net.minecraft.world.level.LevelAccessor;
//
//public class GetPointMigraProcedure {
//	public static String execute(LevelAccessor world) {
//		if (MapVariables.get(world).strategyMigration >= 4) {
//			return "§bFinished";
//		}
//		return Math.round(MapVariables.get(world).evoPointMigration) + "§b/"
//				+ Math.round(Math.pow(MapVariables.get(world).strategyMigration + 1, 3) * GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get());
//	}
//}
