
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.procedures.DeductPlayerSanityProcedure;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DeductOneSanityMobEffect extends MobEffect {
	public DeductOneSanityMobEffect() {
		super(MobEffectCategory.HARMFUL, -6684724);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		ArrayList<ItemStack> cures = new ArrayList<>();
		cures.add(new ItemStack(Items.MILK_BUCKET));
		cures.add(new ItemStack(Items.TOTEM_OF_UNDYING));
		cures.add(new ItemStack(Items.HONEY_BOTTLE));
		return cures;
	}

	@Override
	public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
		DeductPlayerSanityProcedure.execute(entity, (double) amplifier + 1);

		if (entity.level() instanceof ServerLevel server) {
			server.sendParticles(ParticleTypes.ELECTRIC_SPARK, entity.getX(), (entity.getY() + 1), entity.getZ(), (int) ((double) amplifier + 1), 0.5, 0.8, 0.5, 0.1);
		}
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return true;
	}

	@Override
	public void initializeClient(java.util.function.Consumer<IClientMobEffectExtensions> consumer) {
		consumer.accept(new IClientMobEffectExtensions() {
			@Override
			public boolean isVisibleInInventory(MobEffectInstance effect) {
				return false;
			}

			@Override
			public boolean isVisibleInGui(MobEffectInstance effect) {
				return false;
			}
		});
	}
}
