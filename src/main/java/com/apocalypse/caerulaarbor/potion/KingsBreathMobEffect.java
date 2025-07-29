
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.init.ModParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

public class KingsBreathMobEffect extends InvisibleMobEffect {
	public KingsBreathMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -3342337);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "8ea78b84-6658-3acc-9950-6582aa0e22ee", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		LevelAccessor world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		if (((Entity) entity).getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable()).kingShowPtc) {
			if ((double) amplifier < 1) {
				world.addParticle(ModParticleTypes.KING_SLAY.get(),
						x + Mth.nextDouble(RandomSource.create(), -0.55, 0.55),
						y + Mth.nextDouble(RandomSource.create(), 0, entity.getBbHeight() * 0.6),
						z + Mth.nextDouble(RandomSource.create(), -0.55, 0.55),
						Math.sin(Mth.nextDouble(RandomSource.create(), 0, 6.283)),
						0.05,
						Math.cos(Mth.nextDouble(RandomSource.create(), 0, 6.283))
				);
			} else {
				world.addParticle(ModParticleTypes.KING_SLAY_RED.get(),
						x + Mth.nextDouble(RandomSource.create(), -0.55, 0.55),
						y + Mth.nextDouble(RandomSource.create(), 0, entity.getBbHeight() * 0.6),
						z + Mth.nextDouble(RandomSource.create(), -0.55, 0.55),
						Math.sin(Mth.nextDouble(RandomSource.create(), 0, 6.283)),
						0.05,
						Math.cos(Mth.nextDouble(RandomSource.create(), 0, 6.283))
				);
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
