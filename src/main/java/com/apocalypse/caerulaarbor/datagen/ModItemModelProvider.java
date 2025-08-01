package com.apocalypse.caerulaarbor.datagen;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings({"ConstantConditions", "UnusedReturnValue", "SameParameterValue", "unused"})
public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CaerulaArborMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        // relics
        simpleItem(ModItems.FEATURED_CANNED_MEAT);
        simpleItem(ModItems.SEAWEED_SALAD);
        simpleItem(ModItems.ORANGE_STORM);
        simpleItem(ModItems.COFFEE_PLAINS_COFFEE_CANDY);
        simpleItem(ModItems.SCREAMING_CHERRY);
        simpleItem(ModItems.PITTS_ASSORTED_FRUITS);
        simpleItem(ModItems.EXTRA_PUNGENT_COFFEE_BEANS);

        simpleItem(ModItems.HOT_WATER_KETTLE);
        simpleItem(ModItems.VAMPIRES_BED);
        simpleItem(ModItems.PURE_GOLD_EXPEDITION);
        simpleItem(ModItems.PROOF_OF_LONGEVITY);
        simpleItem(ModItems.WEIRD_FLUTE);
        simpleItem(ModItems.DURIN_OVERGROUND_ODYSSEY);

        simpleItem(ModItems.HAND_OF_PULVERIZATION);

        simpleItem(ModItems.SURVIVOR_CONTRACT);

        // equipments
        handheldItem(ModItems.CHITIN_AXE);
        handheldItem(ModItems.CHITIN_HOE);
        handheldItem(ModItems.CHITIN_PICKAXE);
        handheldItem(ModItems.CHITIN_SHOVEL);
        handheldItem(ModItems.CHITIN_SWORD);
        simpleItem(ModItems.CHITIN_HELMET);
        simpleItem(ModItems.CHITIN_CHESTPLATE);
        simpleItem(ModItems.CHITIN_LEGGINGS);
        simpleItem(ModItems.CHITIN_BOOTS);
        handheldItem(ModItems.COMPLEX_CHITIN_AXE);
        handheldItem(ModItems.COMPLEX_CHITIN_HOE);
        handheldItem(ModItems.COMPLEX_CHITIN_PICKAXE);
        handheldItem(ModItems.COMPLEX_CHITIN_SHOVEL);
        handheldItem(ModItems.COMPLEX_CHITIN_SWORD);
        simpleItem(ModItems.COMPLEX_CHITIN_HELMET);
        simpleItem(ModItems.COMPLEX_CHITIN_CHESTPLATE);
        simpleItem(ModItems.COMPLEX_CHITIN_LEGGINGS);
        simpleItem(ModItems.COMPLEX_CHITIN_BOOTS);

        // misc
        simpleItem(ModItems.OBSIDIAN_BALL);
        simpleItem(ModItems.OCEAN_CHITIN);
        simpleItem(ModItems.COMPLEX_CHITIN);
        simpleItem(ModItems.REDSTONE_INGOT);

        // spawn eggs
        ModItems.SPAWN_EGGS.getEntries().forEach(this::spawnEggItem);

        // blocks
        evenSimplerBlockItem(ModBlocks.POOL_OF_PROCREATION);
        evenSimplerBlockItem(ModBlocks.NOURISHED_POOL_OF_PROCREATION);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return simpleItem(item, "");
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item, String location) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", CaerulaArborMod.loc("item/" + location + item.getId().getPath()));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item, String location, String renderType) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", CaerulaArborMod.loc("item/" + location + item.getId().getPath())).renderType(renderType);
    }

    public void evenSimplerBlockItem(RegistryObject<Block> block) {
        this.withExistingParent(CaerulaArborMod.MODID + ":" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath(),
                modLoc("block/" + ForgeRegistries.BLOCKS.getKey(block.get()).getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/handheld"))
                .texture("layer0", CaerulaArborMod.loc("item/" + item.getId().getPath()));
    }

    private ItemModelBuilder spawnEggItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/template_spawn_egg"));
    }
}
