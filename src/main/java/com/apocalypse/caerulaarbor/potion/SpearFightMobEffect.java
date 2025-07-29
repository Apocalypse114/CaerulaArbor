
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.List;

public class SpearFightMobEffect extends InvisibleMobEffect {
	public SpearFightMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -3355444);
		this.addAttributeModifier(ForgeMod.BLOCK_REACH.get(), "9f09e814-768f-30ef-8126-99947a514b97", 0.5, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(ForgeMod.ENTITY_REACH.get(), "5b1909df-711e-3b48-ac79-71f39809f0be", 0.5, AttributeModifier.Operation.ADDITION);
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
