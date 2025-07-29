
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class StrengthOfCrowdMobEffect extends InvisibleMobEffect {
	public StrengthOfCrowdMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -6750157);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "c5f0c428-7667-36dd-9868-a1e76a7c2475", 0.05, AttributeModifier.Operation.MULTIPLY_BASE);
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
