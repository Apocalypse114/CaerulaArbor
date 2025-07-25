package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class CaramelCakePieceItem extends Item {
    public CaramelCakePieceItem() {
        super(new Item.Properties().food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.4f).build()));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
        if (!entity.level().isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 80, 1));
        }
        ModCapabilities.getSanityInjury(entity).heal(125);

        return super.finishUsingItem(itemstack, world, entity);
    }
}
