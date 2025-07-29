
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.init.ModAttributes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SanityProtectMobEffect extends InvisibleMobEffect {
	public SanityProtectMobEffect() {
		super(MobEffectCategory.NEUTRAL, -6684724);
		this.addAttributeModifier(ModAttributes.SANITY_INJURY_RESISTANCE.get(), "92276964-0e2a-345f-b7fb-492831284e84", -0.15, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(ModAttributes.SANITY_RATE.get(), "5c2e2c42-635d-3d77-b67c-a3b051eef465", 1, AttributeModifier.Operation.ADDITION);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
