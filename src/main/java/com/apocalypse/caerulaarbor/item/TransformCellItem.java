
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class TransformCellItem extends Item {
    public TransformCellItem() {
        super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON).food((new FoodProperties.Builder()).nutrition(7).saturationMod(0.4f).alwaysEat().meat().build()));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.transform_cell.description_0"));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
        var cap = ModCapabilities.getPlayerVariables(entity);
        if (cap.seabornization < 3 && !entity.level().isClientSide()) {
            entity.addEffect(new MobEffectInstance(ModMobEffects.INFESTED.get(), 6000, cap.seabornization));
        }
        return super.finishUsingItem(itemstack, world, entity);
    }
}
