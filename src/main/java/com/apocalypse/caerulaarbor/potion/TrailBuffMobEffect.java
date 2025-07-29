
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import com.apocalypse.caerulaarbor.init.ModTags;
import com.apocalypse.caerulaarbor.procedures.DeductPlayerSanityProcedure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TrailBuffMobEffect extends InvisibleMobEffect {
	public TrailBuffMobEffect() {
		super(MobEffectCategory.NEUTRAL, -10053121);
		this.addAttributeModifier(ModAttributes.SANITY_RATE.get(), "204fd8a1-28dc-3106-bb46-8c9977e6079d", 1, AttributeModifier.Operation.MULTIPLY_BASE);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		return new ArrayList<>();
	}

	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
		LevelAccessor world = entity.level();

		if (!entity.getType().is(ModTags.EntityTypes.SEA_BORN)) {
			if (!(entity instanceof Player && (ModCapabilities.getPlayerVariables(entity)).seabornization == 3)) {
				entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)), 2);
				DeductPlayerSanityProcedure.execute(entity, 20);
			}
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 20 == 0;
	}
}
