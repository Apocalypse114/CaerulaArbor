
package com.apocalypse.caerulaarbor.item.relic.normal;

import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.init.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class AntiquatedSheetMusicItem extends RecordItem {
	// TODO 这玩意怎么整成RelicItem
	public AntiquatedSheetMusicItem() {
		super(8, ModSounds.BLOODYWOLF_OPENMOUTH, new Item.Properties().stacksTo(1).rarity(Rarity.COMMON), 1180);
	}

	@Override
	public void appendHoverText(@NotNull ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		list.add(Component.translatable("item.caerula_arbor.antiquated_sheet_music.description_0").withStyle(ChatFormatting.AQUA));
		list.add(Component.translatable("item.caerula_arbor.antiquated_sheet_music.description_1").withStyle(ChatFormatting.GRAY));
	}

	@Override
	@ParametersAreNonnullByDefault
	public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
		InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		ItemStack itemstack = ar.getObject();
		if (!itemstack.getOrCreateTag().getBoolean("Used")) {
			Relic.UTIL_SCORE.gainAndSync(entity);

			if ((Entity) entity instanceof Player _player)
				_player.giveExperienceLevels(2);
			if ((LevelAccessor) world instanceof Level _level) {
				if (!_level.isClientSide()) {
					_level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 2, 1);
				} else {
					_level.playLocalSound(x, y, z, SoundEvents.PLAYER_LEVELUP, SoundSource.NEUTRAL, 2, 1, false);
				}
			}
			if ((LevelAccessor) world instanceof ServerLevel _level)
				_level.sendParticles(ParticleTypes.NOTE, x, y, z, 48, 1, 1, 1, 1);
			itemstack.getOrCreateTag().putBoolean("Used", true);
		}
		return ar;
	}
}
