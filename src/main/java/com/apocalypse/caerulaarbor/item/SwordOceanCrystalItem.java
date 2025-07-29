
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.init.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class SwordOceanCrystalItem extends SwordItem {
	public SwordOceanCrystalItem() {
		super(new Tier() {
			public int getUses() {
				return 325;
			}

			public float getSpeed() {
				return 4f;
			}

			public float getAttackDamageBonus() {
				return 5f;
			}

			public int getLevel() {
				return 2;
			}

			public int getEnchantmentValue() {
				return 18;
			}

			public @NotNull Ingredient getRepairIngredient() {
				return Ingredient.of(new ItemStack(ModItems.OCEAN_CRYSTAL.get()));
			}
		}, 3, -2.4f, new Item.Properties());
	}

	@Override
	public boolean hasCraftingRemainingItem(ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
		ItemStack retval = new ItemStack(this);
		retval.setDamageValue(itemstack.getDamageValue() + 1);
		if (retval.getDamageValue() >= retval.getMaxDamage()) {
			return ItemStack.EMPTY;
		}
		return retval;
	}

	@Override
	public boolean isRepairable(@NotNull ItemStack itemstack) {
		return false;
	}
}
