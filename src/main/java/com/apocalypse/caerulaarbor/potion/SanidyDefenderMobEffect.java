
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.init.ModAttributes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SanidyDefenderMobEffect extends InvisibleMobEffect {
	public SanidyDefenderMobEffect() {
		super(MobEffectCategory.NEUTRAL, -6697729);
		this.addAttributeModifier(ModAttributes.SANITY_INJURY_RESISTANCE.get(), "498300c6-3f7a-35d7-ba0c-e5cbdbc312dc", -0.04, AttributeModifier.Operation.MULTIPLY_BASE);
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
