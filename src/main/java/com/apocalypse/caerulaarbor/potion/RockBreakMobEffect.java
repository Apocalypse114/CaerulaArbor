
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class RockBreakMobEffect extends InvisibleMobEffect {
	public RockBreakMobEffect() {
		super(MobEffectCategory.NEUTRAL, -6710887);
		this.addAttributeModifier(Attributes.ARMOR, "643337bb-099c-3b1a-9484-1ee3bf459028", -0.7, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "fb973f54-7fd2-375c-8a38-fec9a7e3aee1", -0.7, AttributeModifier.Operation.MULTIPLY_BASE);
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
