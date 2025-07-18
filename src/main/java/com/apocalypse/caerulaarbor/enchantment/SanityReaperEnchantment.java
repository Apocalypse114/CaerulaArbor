package com.apocalypse.caerulaarbor.enchantment;

import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SanityReaperEnchantment extends Enchantment {
    private static final EnchantmentCategory ENCHANTMENT_CATEGORY = EnchantmentCategory.create("caerula_arbor_sanity_reaper", item -> Ingredient.of(ItemTags.TOOLS).test(new ItemStack(item)));

    public SanityReaperEnchantment() {
        super(Enchantment.Rarity.UNCOMMON, ENCHANTMENT_CATEGORY, EquipmentSlot.values());
    }

    @Override
    public int getMinCost(int level) {
        return 1 + level * 10;
    }

    @Override
    public int getMaxCost(int level) {
        return 6 + level * 10;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
}
