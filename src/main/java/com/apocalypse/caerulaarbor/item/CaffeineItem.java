
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.client.font.ModFontHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class CaffeineItem extends Item {
    public CaffeineItem() {
        super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.caffeine.description_0"));
        list.add(Component.literal("BREED SUBSISTING GROW MIGRATION").withStyle(ModFontHelper.SEABORN_LANGUAGE));
        list.add(Component.literal("BREED SUBSISTING GROW MIGRATION").withStyle(ModFontHelper.SEABORN_LANGUAGE_INVERTED));
    }
}
