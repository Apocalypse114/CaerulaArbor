package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CookedPeduncleItem extends Item {
    public CookedPeduncleItem() {
        super(new Item.Properties().food((new FoodProperties.Builder()).nutrition(6).saturationMod(1f).meat().build()));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
        ModCapabilities.getSanityInjury(entity).heal(15);
        return super.finishUsingItem(itemstack, world, entity);
    }
}
