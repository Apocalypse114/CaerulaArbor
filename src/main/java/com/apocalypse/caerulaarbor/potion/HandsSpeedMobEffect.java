
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class HandsSpeedMobEffect extends InvisibleMobEffect {
	public HandsSpeedMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -3381760);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, "85191107-953f-312c-a90f-9745e7283824", 0.6, AttributeModifier.Operation.ADDITION);
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
