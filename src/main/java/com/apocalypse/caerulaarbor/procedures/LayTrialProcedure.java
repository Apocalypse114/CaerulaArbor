package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.entity.NetherseaBrandguiderEntity;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class LayTrialProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.5 && GameplayConfig.ENABLE_MOB_BREAK.get() && world.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
			if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
				_entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, 2));
			if ((entity instanceof NetherseaBrandguiderEntity _datEntI ? _datEntI.getEntityData().get(NetherseaBrandguiderEntity.DATA_laylimit) : 0) > 0) {
				if ((world.getBlockState(BlockPos.containing(x, y, z))).canBeReplaced() && !((world.getBlockState(BlockPos.containing(x, y, z))).getBlock() == ModBlocks.SEA_TRAIL_GROWN.get())) {
					if (ModBlocks.SEA_TRAIL_GROWN.get().defaultBlockState().canSurvive(world, BlockPos.containing(x, y, z))) {
						world.setBlock(BlockPos.containing(x, y, z), ModBlocks.SEA_TRAIL_GROWN.get().defaultBlockState(), 3);
						if (world instanceof Level _level) {
							if (!_level.isClientSide()) {
								_level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.SCULK_VEIN_STEP, SoundSource.NEUTRAL, 2, 1);
							} else {
								_level.playLocalSound(x, y, z, SoundEvents.SCULK_VEIN_STEP, SoundSource.NEUTRAL, 2, 1, false);
							}
						}
						if (entity instanceof NetherseaBrandguiderEntity _datEntSetI)
							_datEntSetI.getEntityData().set(NetherseaBrandguiderEntity.DATA_laylimit, (int) ((entity instanceof NetherseaBrandguiderEntity _datEntI ? _datEntI.getEntityData().get(NetherseaBrandguiderEntity.DATA_laylimit) : 0) - 1));
					}
				}
			}
		}
	}
}
