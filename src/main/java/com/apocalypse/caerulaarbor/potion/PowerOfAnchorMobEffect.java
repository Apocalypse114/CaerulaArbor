package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PowerOfAnchorMobEffect extends MobEffect {
	public PowerOfAnchorMobEffect() {
		super(MobEffectCategory.NEUTRAL, -6684724);
		this.addAttributeModifier(ModAttributes.SANITY_INJURY_RESISTANCE.get(), "fca8c1c6-9152-3107-9573-bd52fa24d2f9", -0.4, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return new ArrayList<>();
	}

	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
		ModCapabilities.getPlayerVariables(entity).light = Mth.clamp(ModCapabilities.getPlayerVariables(entity).light + 0.5, 0, 100);
		ModCapabilities.getPlayerVariables(entity).syncPlayerVariables(entity);
		ModCapabilities.getSanityInjury(entity).heal(40);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 40 == 0;
	}
}
