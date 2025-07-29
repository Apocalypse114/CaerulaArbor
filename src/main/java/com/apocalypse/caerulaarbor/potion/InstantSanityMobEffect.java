
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.procedures.DeductPlayerSanityProcedure;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InstantSanityMobEffect extends InvisibleMobEffect {
	public InstantSanityMobEffect() {
		super(MobEffectCategory.HARMFUL, -3342388);
	}

	@Override
	public boolean isInstantenous() {
		return true;
	}

	@Override
	public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
	}

	@Override
	public void applyInstantenousEffect(Entity source, Entity indirectSource, LivingEntity entity, int amplifier, double health) {
		DeductPlayerSanityProcedure.execute(entity, 125 * ((double) amplifier + 1));
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}
}
