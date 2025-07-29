
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.List;

public class AddReachMobEffect extends InvisibleMobEffect {
	public AddReachMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -10066432);
		this.addAttributeModifier(ForgeMod.BLOCK_REACH.get(), "e1ecf318-fb03-3b5c-8f72-f0c607ec5341", 0.2, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(ForgeMod.ENTITY_REACH.get(), "afdcee44-7b74-36b1-ba4f-23bd6a1cf564", 0.2, AttributeModifier.Operation.MULTIPLY_BASE);
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
