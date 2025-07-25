
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.init.ModParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.common.ForgeMod;

import java.util.ArrayList;
import java.util.List;

public class DizzyMobEffect extends MobEffect {
	public DizzyMobEffect() {
		super(MobEffectCategory.NEUTRAL, -3355444);
		this.addAttributeModifier(Attributes.ATTACK_SPEED, "acd7cc57-c6e7-3da1-b6fc-9e1e5dd88598", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.JUMP_STRENGTH, "d909f02c-69f1-3309-aa57-9512487407ff", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "8fce4344-e7dc-3485-bcfc-1eabaf2662f2", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(ForgeMod.SWIM_SPEED.get(), "c5b14588-5035-3f65-ae3a-5d9bf3ab67fa", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(ForgeMod.BLOCK_REACH.get(), "51754579-64ad-31b8-856b-d95093079463", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(ForgeMod.ENTITY_REACH.get(), "37b0609d-2667-3863-9226-91695957e9ce", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
		this.addAttributeModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), "db41f8c8-0f93-3c63-9c58-1e8e293749f3", -1, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

	@Override
	public List<ItemStack> getCurativeItems() {
		ArrayList<ItemStack> cures = new ArrayList<>();
		cures.add(new ItemStack(Items.TOTEM_OF_UNDYING));
		cures.add(new ItemStack(Items.HONEY_BOTTLE));
		return cures;
	}

	@Override
	public void applyEffectTick(LivingEntity entity, int amplifier) {
		LevelAccessor world = entity.level();
		double x = entity.getX();
		double y = entity.getY();
		double z = entity.getZ();
		if (world instanceof ServerLevel server) {
			server.sendParticles(ModParticleTypes.DIZZINESS.get(), x, y, z, 2, 1, 1, 1, 0.1);
		}
		world.addParticle(ModParticleTypes.DIZZINESS.get(), x, y, z, 0.5 - Math.random(), 0.1, 0.5 - Math.random());
	}

	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration % 10 == 0;
	}
}
