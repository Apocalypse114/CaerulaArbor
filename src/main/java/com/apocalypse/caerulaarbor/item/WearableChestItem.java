
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.procedures.GainRelicARMORProcedure;
import com.google.common.collect.Iterables;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public abstract class WearableChestItem extends ArmorItem {
	public WearableChestItem(ArmorItem.Type type, Item.Properties properties) {
		super(new ArmorMaterial() {
			@Override
			public int getDurabilityForType(ArmorItem.@NotNull Type type) {
				return new int[]{13, 15, 16, 11}[type.getSlot().getIndex()] * 18;
			}

			@Override
			public int getDefenseForType(ArmorItem.@NotNull Type type) {
				return new int[]{2, 5, 11, 2}[type.getSlot().getIndex()];
			}

			@Override
			public int getEnchantmentValue() {
				return 16;
			}

			@Override
			public @NotNull SoundEvent getEquipSound() {
				return SoundEvents.ARMOR_EQUIP_NETHERITE;
			}

			@Override
			public @NotNull Ingredient getRepairIngredient() {
				return Ingredient.of();
			}

			@Override
			public @NotNull String getName() {
				return "wearable_chest";
			}

			@Override
			public float getToughness() {
				return 2.5f;
			}

			@Override
			public float getKnockbackResistance() {
				return 0.2f;
			}
		}, type, properties);
	}

	public static class Chestplate extends WearableChestItem {
		public Chestplate() {
			super(ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant());
		}

		@Override
		public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
			return "caerula_arbor:textures/models/armor/kingarmor_layer_1.png";
		}

		@Override
		@OnlyIn(Dist.CLIENT)
		public boolean isFoil(@NotNull ItemStack itemstack) {
			return true;
		}

		@Override
		public boolean makesPiglinsNeutral(ItemStack itemstack, LivingEntity entity) {
			return true;
		}

		@Override
		@ParametersAreNonnullByDefault
		public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
			super.inventoryTick(itemstack, world, entity, slot, selected);
			if (entity instanceof Player player && Iterables.contains(player.getArmorSlots(), itemstack)) {
				GainRelicARMORProcedure.execute(world, entity.getX(), entity.getY(), entity.getZ(), entity, itemstack);
			}
		}
	}
}
