package com.apocalypse.caerulaarbor.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SourceEssItem extends Item {
    public SourceEssItem() {
        super(new Item.Properties().stacksTo(64).rarity(Rarity.EPIC)); // Purple name via EPIC rarity
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        // Green description tooltip
        list.add(Component.translatable("item.caerula_arbor.source_ess.description_0").withStyle(ChatFormatting.GREEN));
    }
}