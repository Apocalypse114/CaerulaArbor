//package com.apocalypse.caerulaarbor.procedures;
//
//import com.apocalypse.caerulaarbor.capability.map.MapVariables;
//import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
//import net.minecraft.world.level.LevelAccessor;
//
//public class GetPointSilenceProcedure {
//	public static String execute(LevelAccessor world) {
//		if (MapVariables.get(world).strategySilence >= 4) {
//			return "§4^&$%!!";
//		}
//		return Math.round(MapVariables.get(world).evoPointSilence) + "§c/"
//				+ Math.round(Math.pow(MapVariables.get(world).strategySilence + 1, 3) * GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get() * 4);
//	}
//}
