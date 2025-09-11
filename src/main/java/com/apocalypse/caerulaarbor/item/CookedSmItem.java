package com.apocalypse.caerulaarbor.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class CookedSmItem extends Item {
    public CookedSmItem() {
        super(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(6)
                        .saturationMod(0.8f)
                        .meat()
                        .build()));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 40;
    }
}