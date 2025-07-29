
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class BoostOfSilenceMobEffect extends InvisibleMobEffect {
	public BoostOfSilenceMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -3407872);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "b339e741-20d6-37eb-a046-041d84843f25", 0.25, AttributeModifier.Operation.MULTIPLY_BASE);
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
