
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class AddAttackPerclyMobEffect extends InvisibleMobEffect {
	public AddAttackPerclyMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -3407770);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "8ab28a02-a588-3be0-95f6-28531bc0b796", 0.2, AttributeModifier.Operation.MULTIPLY_BASE);
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
