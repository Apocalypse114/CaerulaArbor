
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SurvivorsGuideMobEffect extends InvisibleMobEffect {
	public SurvivorsGuideMobEffect() {
		super(MobEffectCategory.NEUTRAL, -3407872);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "253a73d0-8589-3a25-8b5c-32b3035cce6e", 0.2, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ARMOR, "1e800216-1635-33d1-a232-0f265561ee39", 2, AttributeModifier.Operation.ADDITION);
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
