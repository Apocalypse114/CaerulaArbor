package com.apocalypse.caerulaarbor.item.equipment;

import com.apocalypse.caerulaarbor.capability.sanity.SIHelper;
import com.apocalypse.caerulaarbor.tiers.ModItemTier;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;

import java.util.List;

public class ComplexChitinPickaxeItem extends PickaxeItem {

	public ComplexChitinPickaxeItem() {
		super(ModItemTier.COMPLEX_CHITIN, -2, -2.8f, new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON));
	}

	@Override
	public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, LivingEntity pAttacker) {
		SIHelper.causeSanityInjuryWithParticles(entity, 80);
		return super.hurtEnemy(itemstack, entity, pAttacker);
	}

	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
		ItemStack stack = new ItemStack(this);
		stack.setDamageValue(itemstack.getDamageValue() + 1);
		if (stack.getDamageValue() >= stack.getMaxDamage()) {
			return ItemStack.EMPTY;
		}
		return stack;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		list.add(Component.translatable("item.caerula_arbor.complex_chitin_tool.des").withStyle(ChatFormatting.GRAY));
	}
}
