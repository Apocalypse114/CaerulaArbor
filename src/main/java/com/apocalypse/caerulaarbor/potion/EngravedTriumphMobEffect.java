
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class EngravedTriumphMobEffect extends InvisibleMobEffect {
	public EngravedTriumphMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -16751002);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "ad3ee760-5e55-36db-8e49-63dc3b67a750", 0.01, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.MAX_HEALTH, "1feedda3-ba88-3e2a-8856-5212332ac8b2", 0.01, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "814b3564-fb66-3420-ba9e-9b9c027d3843", 0.01, AttributeModifier.Operation.MULTIPLY_BASE);
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
