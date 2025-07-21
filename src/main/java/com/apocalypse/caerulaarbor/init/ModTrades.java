package com.apocalypse.caerulaarbor.init;

import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.BasicItemListing;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModTrades {

    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.LIBRARIAN) {
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ModItems.REDSTONE_INGOT.get(), 8), new ItemStack(Items.PAPER), new ItemStack(ModItems.LETTER_TERMINATION_CONTRACT.get()), 4, 4, 0.02f));
        }
        if (event.getType() == VillagerProfession.TOOLSMITH) {
            event.getTrades().get(3).add(new BasicItemListing(new ItemStack(ModItems.REDSTONE_INGOT.get(), 12), new ItemStack(Items.EMERALD, 8), new ItemStack(ModItems.UNIVERSAL_KEY.get()), 2, 8, 0.05f));
        }
        if (event.getType() == ModVillagers.CANNOT_GOODENOUGH.get()) {
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(ModItems.REDSTONE_INGOT.get(), 24), new ItemStack(Blocks.RED_WOOL, 16), new ItemStack(ModItems.SARKAZ_KINGS_TORN_BANNER.get()), 1, 12, 0.04f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(ModItems.REDSTONE_INGOT.get(), 16), new ItemStack(Items.WHITE_BED), new ItemStack(ModItems.SARKAZ_KINGS_REGAL_REST.get()), 1, 8, 0.04f));
            event.getTrades().get(4).add(new BasicItemListing(new ItemStack(ModItems.REDSTONE_INGOT.get(), 16), new ItemStack(Items.GOLD_INGOT, 8), new ItemStack(ModItems.KINGS_LEGACY.get()), 1, 8, 0.04f));
            event.getTrades().get(5).add(new BasicItemListing(new ItemStack(ModItems.REDSTONE_INGOT.get(), 24), new ItemStack(Items.NETHERITE_SCRAP), new ItemStack(ModItems.KINGS_NEW_LANCE.get()), 10, 5, 0.04f));
            event.getTrades().get(1).add(new BasicItemListing(new ItemStack(ModItems.REDSTONE_INGOT.get(), 12), new ItemStack(Items.PRISMARINE_SHARD, 16), new ItemStack(ModItems.WEIRD_FLUTE.get()), 10, 5, 0.04f));
            event.getTrades().get(2).add(new BasicItemListing(new ItemStack(ModItems.REDSTONE_INGOT.get(), 16), new ItemStack(Blocks.GRASS_BLOCK, 32), new ItemStack(ModBlocks.REDSTONE_IRIS_SEEDING.get()), 4, 8, 0.04f));
        }
    }
}
