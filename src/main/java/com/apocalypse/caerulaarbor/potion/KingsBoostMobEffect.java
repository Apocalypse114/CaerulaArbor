
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KingsBoostMobEffect extends InvisibleMobEffect {
	public KingsBoostMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -13159);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, "7bb054f7-8873-3639-bb6b-32a266d665e4", 0.5, AttributeModifier.Operation.ADDITION);
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
