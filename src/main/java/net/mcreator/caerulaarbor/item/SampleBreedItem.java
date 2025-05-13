
package net.mcreator.caerulaarbor.item;

import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.level.Level;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Item;
import net.minecraft.network.chat.Component;

import java.util.List;

public class SampleBreedItem extends Item {
	public SampleBreedItem() {
		super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public boolean isFoil(ItemStack itemstack) {
		return true;
	}

	@Override
	public void appendHoverText(ItemStack itemstack, Level level, List<Component> list, TooltipFlag flag) {
		super.appendHoverText(itemstack, level, list, flag);
		list.add(Component.translatable("item.caerula_arbor.sample_breed.description_0"));
		list.add(Component.translatable("item.caerula_arbor.sample_breed.description_1"));
		list.add(Component.translatable("item.caerula_arbor.sample_breed.description_2"));
		list.add(Component.translatable("item.caerula_arbor.sample_breed.description_3"));
		list.add(Component.translatable("item.caerula_arbor.sample_breed.description_4"));
		list.add(Component.translatable("item.caerula_arbor.sample_breed.description_5"));
		list.add(Component.translatable("item.caerula_arbor.sample_breed.description_6"));
		list.add(Component.translatable("item.caerula_arbor.sample_breed.description_7"));
		list.add(Component.translatable("item.caerula_arbor.sample_breed.description_8"));
		list.add(Component.translatable("item.caerula_arbor.sample_breed.description_9"));
	}
}
