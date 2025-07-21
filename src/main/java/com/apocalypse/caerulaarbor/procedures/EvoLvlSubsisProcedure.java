package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.init.ModSounds;
import com.apocalypse.caerulaarbor.network.CaerulaArborModVariables;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class EvoLvlSubsisProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, CommandContext<CommandSourceStack> arguments) {
		CaerulaArborModVariables.MapVariables.get(world).strategySubsisting = (int) DoubleArgumentType.getDouble(arguments, "lvl");
		CaerulaArborModVariables.MapVariables.get(world).syncData(world);
		if (DoubleArgumentType.getDouble(arguments, "lvl") >= 3) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), ModSounds.SUBSISTING2.get(), SoundSource.NEUTRAL, 4, 1);
				} else {
					_level.playLocalSound(x, y, z, ModSounds.SUBSISTING2.get(), SoundSource.NEUTRAL, 4, 1, false);
				}
			}
		} else if (DoubleArgumentType.getDouble(arguments, "lvl") > 0) {
			if (world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), ModSounds.SUBSISTING1.get(), SoundSource.NEUTRAL, 4, 1);
				} else {
					_level.playLocalSound(x, y, z, ModSounds.SUBSISTING1.get(), SoundSource.NEUTRAL, 4, 1, false);
				}
			}
		}
		if (DoubleArgumentType.getDouble(arguments, "lvl") < 4) {
			CaerulaArborModVariables.MapVariables.get(world).strategy_silence = 0;
			CaerulaArborModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
