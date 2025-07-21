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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class UpgradeSilenceProcedure {
	public static void execute(LevelAccessor world, Entity entity, double point) {
		if (entity == null)
			return;
		String num = "";
		String prefix = "";
		var mapVar = CaerulaArborModVariables.MapVariables.get(world);
		int stra = mapVar.strategy_silence;
		if (IfCanSilenceProcedure.execute(world)) {
			mapVar.evo_point_silence += point;
			mapVar.syncData(world);
			if (stra < 4) {
				if (mapVar.evo_point_silence >= Math.pow(stra + 1, 3) * (double) GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get() * 4) {
					for (Entity entityiterator : world.players()) {
						if (entityiterator instanceof ServerPlayer _player) {
							Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("caerula_arbor:she_coming"));
							AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
							if (!_ap.isDone()) {
								for (String criteria : _ap.getRemainingCriteria())
									_player.getAdvancements().award(_adv, criteria);
							}
						}
					}
					mapVar.strategy_silence = stra + 1;
					mapVar.syncData(world);
					stra ++;
					mapVar.evo_point_silence = 0;
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
							if (stra == 1) {
								if (world instanceof Level _level) {
									if (!_level.isClientSide()) {
										_level.playSound(null, BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), ModSounds.SILENCE1.get(),
												SoundSource.NEUTRAL, 4, 1);
									} else {
										_level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), ModSounds.SILENCE1.get(), SoundSource.NEUTRAL, 4, 1,
												false);
									}
								}
								if (entityiterator instanceof Player _player && !_player.level().isClientSide())
									_player.displayClientMessage(Component.literal((Component.translatable("item.caerula_arbor.language_key.description_6").getString())), true);
							} else if (stra == 2) {
								if (world instanceof Level _level) {
									if (!_level.isClientSide()) {
										_level.playSound(null, BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), ModSounds.SILENCE2.get(),
												SoundSource.NEUTRAL, 4, 1);
									} else {
										_level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), ModSounds.SILENCE2.get(), SoundSource.NEUTRAL, 4, 1,
												false);
									}
								}
								if (entityiterator instanceof Player _player && !_player.level().isClientSide())
									_player.displayClientMessage(Component.literal((Component.translatable("item.caerula_arbor.language_key.description_7").getString())), true);
							} else if (stra == 3) {
								if (world instanceof Level _level) {
									if (!_level.isClientSide()) {
										_level.playSound(null, BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), ModSounds.SILENCE3.get(),
												SoundSource.NEUTRAL, 4, 1);
									} else {
										_level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), ModSounds.SILENCE3.get(), SoundSource.NEUTRAL, 4, 1,
												false);
									}
								}
								if (entityiterator instanceof Player _player && !_player.level().isClientSide())
									_player.displayClientMessage(Component.literal((Component.translatable("item.caerula_arbor.language_key.description_8").getString())), true);
							} else if (stra == 4) {
								if (world instanceof Level _level) {
									if (!_level.isClientSide()) {
										_level.playSound(null, BlockPos.containing(entityiterator.getX(), entityiterator.getY(), entityiterator.getZ()), ModSounds.SILENCE4.get(),
												SoundSource.NEUTRAL, 4, 1);
									} else {
										_level.playLocalSound((entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), ModSounds.SILENCE4.get(), SoundSource.NEUTRAL, 4, 1,
												false);
									}
								}
								if (entityiterator instanceof Player _player && !_player.level().isClientSide())
									_player.displayClientMessage(Component.literal((Component.translatable("item.caerula_arbor.language_key.description_9").getString())), true);
							}
						}
					}
					if (!world.isClientSide() && world.getServer() != null)
						world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((prefix + Component.translatable("item.caerula_arbor.language_key.description_4").getString() + num)), false);
				}
			} else {
				mapVar.evo_point_silence = 0;
				mapVar.syncData(world);
				for (Entity entityiterator : world.players()) {
					if (entity instanceof ServerPlayer _player) {
						Advancement _adv = _player.server.getAdvancements().getAdvancement(new ResourceLocation("caerula_arbor:hymn_of_land"));
						AdvancementProgress _ap = _player.getAdvancements().getOrStartProgress(_adv);
						if (!_ap.isDone()) {
							for (String criteria : _ap.getRemainingCriteria())
								_player.getAdvancements().award(_adv, criteria);
						}
					}
				}
			}
		} else {
			mapVar.strategy_silence = 0;
			mapVar.syncData(world);
		}
	}
}
