
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KeepBeddingMobEffect extends InvisibleMobEffect {
	public KeepBeddingMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -13159);
		this.addAttributeModifier(Attributes.ARMOR, "0e93db8c-2e5f-3901-9f43-6984ea418c67", 3, AttributeModifier.Operation.ADDITION);
		this.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "aba9ebec-aefe-37f1-ad12-798565bf4715", 2, AttributeModifier.Operation.ADDITION);
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
