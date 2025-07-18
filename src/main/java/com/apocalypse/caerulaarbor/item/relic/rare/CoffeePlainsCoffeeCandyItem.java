package com.apocalypse.caerulaarbor.item.relic.rare;

import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.init.ModItems;
import com.apocalypse.caerulaarbor.item.relic.RelicItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class CoffeePlainsCoffeeCandyItem extends RelicItem {

    public CoffeePlainsCoffeeCandyItem() {
        super(new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)
                .food((new FoodProperties.Builder()).nutrition(2).saturationMod(2f)
                        .effect(() -> new MobEffectInstance(MobEffects.DIG_SPEED, 240, 1), 1.0f)
                        .alwaysEat().build()));
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 30;
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("item.caerula_arbor.coffee_plains_coffee_candy.des_1").withStyle(ChatFormatting.AQUA));
        list.add(Component.translatable("item.caerula_arbor.coffee_plains_coffee_candy.des_2").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
    }

    @Override
    public @NotNull Relic getRelic() {
        return Relic.COFFEE_PLAINS_COFFEE_CANDY;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        Relic.modify(entity, cap -> this.getRelic().gain(entity));

        ItemStack rt = new ItemStack(ModItems.PAPER_BAG.get());
        if (super.finishUsingItem(stack, world, entity).isEmpty()) {
            return rt;
        } else {
            if (entity instanceof Player player && !player.getAbilities().instabuild) {
                if (!player.getInventory().add(rt)) {
                    player.drop(rt, false);
                }
            }
            return stack;
        }
    }
}
