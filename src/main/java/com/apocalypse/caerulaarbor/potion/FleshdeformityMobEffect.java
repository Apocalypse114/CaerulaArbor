
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.List;

public class FleshdeformityMobEffect extends InvisibleMobEffect {
	public FleshdeformityMobEffect() {
		super(MobEffectCategory.NEUTRAL, -26215);
		this.addAttributeModifier(Attributes.ARMOR, "99a62c75-b1cd-36be-9ba7-c48700d07e81", -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.MAX_HEALTH, "fc28b35e-1191-3bde-b7cd-0990962eaf31", -0.25, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "706a1e8d-cc2b-36e2-b4b2-2cdc7f611750", -0.25, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "eb0c388c-9292-33e2-a13d-a928061de700", 0.15, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "411dda6c-0d9f-3f61-8f44-1b281c9f941e", 0.15, AttributeModifier.Operation.MULTIPLY_BASE);
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
