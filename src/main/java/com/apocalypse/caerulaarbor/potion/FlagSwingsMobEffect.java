
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.List;

public class FlagSwingsMobEffect extends InvisibleMobEffect {
	public FlagSwingsMobEffect() {
		super(MobEffectCategory.BENEFICIAL, -39424);
		this.addAttributeModifier(Attributes.FLYING_SPEED, "ee39ba99-5594-3b55-ab24-db9a9bca1dfd", 0.1, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "4d02b3b2-aadc-35ee-97e7-1141e2ec397a", 0.1, AttributeModifier.Operation.MULTIPLY_BASE);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "4aa749d2-a23d-36ef-b7a6-2603aeae1a32", 0.1, AttributeModifier.Operation.MULTIPLY_BASE);
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
