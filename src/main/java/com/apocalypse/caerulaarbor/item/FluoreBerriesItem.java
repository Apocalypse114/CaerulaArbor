
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class FluoreBerriesItem extends Item {
    public FluoreBerriesItem() {
        super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON).food((new FoodProperties.Builder()).nutrition(4).saturationMod(0.5f).alwaysEat().build()));
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 40;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {

        var cap = ModCapabilities.getPlayerVariables(entity);
        cap.light = Math.min(100, cap.light + 8);
        cap.syncPlayerVariables(entity);

        if (!entity.level().isClientSide()) {
            entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 0));
        }

        return super.finishUsingItem(itemstack, world, entity);
    }
}
