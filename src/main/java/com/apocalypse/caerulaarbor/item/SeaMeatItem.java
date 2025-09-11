package com.apocalypse.caerulaarbor.item;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class SeaMeatItem extends Item {
    public SeaMeatItem() {
        super(new Item.Properties()
                .stacksTo(64)
                .rarity(Rarity.COMMON)
                .food(new FoodProperties.Builder()
                        .nutrition(2)
                        .saturationMod(0.1f)
                        .build()));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 40;
    }
}