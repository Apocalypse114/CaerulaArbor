package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.init.ModSounds;
import com.apocalypse.caerulaarbor.network.CaerulaArborModVariables;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class UpgradeSubsisProcedure {
	public static void execute(LevelAccessor world) {
		String num = "";
		String prefix = "";
		var mapVar = CaerulaArborModVariables.MapVariables.get(world);
		int stra = mapVar.strategySubsisting;
		if (stra < 4) {
			if (mapVar.evo_point_subsisting >= Math.pow(stra + 1, 3) * (double) GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get()) {
				for (Entity entityiterator : world.players()) {
					if (entityiterator instanceof ServerPlayer _player) {
						Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("caerula_arbor:to_experience_evolution"));
						AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
						if (!_ap.isDone()) {
							for (String criteria : _ap.getRemainingCriteria())
								_player.getAdvancements().award(_adv, criteria);
						}
					}
				}
				mapVar.strategySubsisting = stra + 1;
				mapVar.syncData(world);
				stra ++;
				mapVar.evo_point_subsisting = 0;
				mapVar.syncData(world);
                prefix = switch (stra) {
                    case 1 -> {
                        num = "I";
                        yield "§p";
                    }
                    case 2 -> {
                        num = "II";
                        yield "§b";
                    }
                    case 3 -> {
                        num = "III";
                        yield "§9";
                    }
                    case 4 -> {
                        num = "IV";
                        yield "§1";
                    }
                    default -> prefix;
                };
				if (GameplayConfig.ENABLE_EVOLUTION_SOUND.get()) {
					for (Entity entityiterator : world.players()) {
						if (stra >= 3) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), ModSounds.SUBSISTING2.get(),
											SoundSource.NEUTRAL, 4, 1);
								} else {
									_level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), ModSounds.SUBSISTING2.get(), SoundSource.NEUTRAL, 4, 1,
											false);
								}
							}
						} else if (stra > 0) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), ModSounds.SUBSISTING1.get(),
											SoundSource.NEUTRAL, 4, 1);
								} else {
									_level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), ModSounds.SUBSISTING1.get(), SoundSource.NEUTRAL, 4, 1,
											false);
								}
							}
						}
					}
				}
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((prefix + Component.translatable("item.caerula_arbor.sample_subsisting.description_5").getString() + num)), false);
			}
		} else {
			for (Entity entityiterator : world.players()) {
				if (entityiterator instanceof ServerPlayer _player) {
					Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("caerula_arbor:to_terminate_evolution"));
					AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
					if (!_ap.isDone()) {
						for (String criteria : _ap.getRemainingCriteria())
							_player.getAdvancements().award(_adv, criteria);
					}
				}
			}
			mapVar.evo_point_subsisting = 1;
			mapVar.syncData(world);
		}
	}
}
