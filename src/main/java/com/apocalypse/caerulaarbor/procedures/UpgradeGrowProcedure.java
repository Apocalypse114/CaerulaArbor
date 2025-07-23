package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.init.ModSounds;
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

public class UpgradeGrowProcedure {
	public static void execute(LevelAccessor world) {
		double stra = 0;
		String num = "";
		String prefix = "";
		stra = MapVariables.get(world).strategyGrow;
		if (stra < 4) {
			if (MapVariables.get(world).evoPointGrow >= Math.pow(stra + 1, 3) * (double) GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get()) {
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
				MapVariables.get(world).strategyGrow = stra + 1;
				MapVariables.get(world).syncData(world);
				stra = MapVariables.get(world).strategyGrow;
				MapVariables.get(world).evoPointGrow = 0;
				MapVariables.get(world).syncData(world);
				if (stra == 1) {
					num = "I";
					prefix = "§p";
				} else if (stra == 2) {
					num = "II";
					prefix = "§b";
				} else if (stra == 3) {
					num = "III";
					prefix = "§9";
				} else if (stra == 4) {
					num = "IV";
					prefix = "§1";
				}
				if (GameplayConfig.ENABLE_EVOLUTION_SOUND.get()) {
					for (Entity entityiterator : world.players()) {
						if (stra >= 3) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), ModSounds.GROW2.get(),
											SoundSource.NEUTRAL, 4, 1);
								} else {
									_level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), ModSounds.GROW2.get(), SoundSource.NEUTRAL, 4, 1,
											false);
								}
							}
						} else if (stra > 0) {
							if (world instanceof Level _level) {
								if (!_level.isClientSide()) {
									_level.playSound(null, BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), ModSounds.GROW1.get(),
											SoundSource.NEUTRAL, 4, 1);
								} else {
									_level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), ModSounds.GROW1.get(), SoundSource.NEUTRAL, 4, 1,
											false);
								}
							}
						}
					}
				}
				if (!world.isClientSide() && world.getServer() != null)
					world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((prefix + Component.translatable("item.caerula_arbor.sample_grow.description_5").getString() + num)), false);
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
			MapVariables.get(world).evoPointGrow = 1;
			MapVariables.get(world).syncData(world);
		}
	}
}
