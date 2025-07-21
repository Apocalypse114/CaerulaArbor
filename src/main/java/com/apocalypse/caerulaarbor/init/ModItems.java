package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.item.*;
import com.apocalypse.caerulaarbor.item.CaerulaAnimusItem;
import com.apocalypse.caerulaarbor.item.HandOfBrandingItem;
import com.apocalypse.caerulaarbor.item.equipment.*;
import com.apocalypse.caerulaarbor.item.relic.epic.*;
import com.apocalypse.caerulaarbor.item.relic.rare.*;
import com.apocalypse.caerulaarbor.item.relic.normal.*;
import com.apocalypse.caerulaarbor.item.relic.cursed.*;
import com.apocalypse.caerulaarbor.tiers.ModArmorMaterial;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CaerulaArborMod.MODID);

    public static final RegistryObject<Item> CAERULA_RECORDER = ITEMS.register("caerula_recorder", CaerulaRecorderItem::new);
    public static final RegistryObject<Item> RELIC_CURSE_EMELIGHT = ITEMS.register("relic_curse_emelight", RelicCurseEMELIGHTItem::new);
    public static final RegistryObject<Item> EMERGENCY_LIGHT = block(ModBlocks.EMERGENCY_LIGHT);
    public static final RegistryObject<Item> LUMINOUS_CORPSE = ITEMS.register("luminous_cprpse", LuminousCorpseItem::new);
    public static final RegistryObject<Item> RELIC_CURSED_RESEARCH = ITEMS.register("relic_cursed_research", RelicCursedRESEARCHItem::new);
    public static final RegistryObject<Item> KINGS_CROWN = ITEMS.register("kings_crown", KingsCrownItem::new);
    public static final RegistryObject<Item> KINGS_ARMOUR = ITEMS.register("kings_armour", KingsArmourItem::new);
    public static final RegistryObject<Item> KINGS_ARMOR = block(ModBlocks.KINGS_ARMOR);
    public static final RegistryObject<Item> BLOCK_CROWN = block(ModBlocks.BLOCK_CROWN);
    public static final RegistryObject<Item> KINGS_NEW_LANCE = ITEMS.register("kings_new_lance", KingsNewLance::new);
    public static final RegistryObject<Item> BLOCK_SPEAR = block(ModBlocks.BLOCK_SPEAR);
    public static final RegistryObject<Item> KINGS_LEGACY = ITEMS.register("kings_legacy", KingsLegacyItem::new);
    public static final RegistryObject<Item> BLOCK_EXTENSION = block(ModBlocks.BLOCK_EXTENSION);
    public static final RegistryObject<Item> KINGS_CRYSTAL = ITEMS.register("kings_crystal", KingsCrystalItem::new);
    public static final RegistryObject<Item> BLOCK_CRYSTAL = block(ModBlocks.BLOCK_CRYSTAL);
    public static final RegistryObject<Item> REDSTONE_INGOT = ITEMS.register("redstone_ingot", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> THE_SPEAR = ITEMS.register("the_spear", TheSpearItem::new);
    public static final RegistryObject<Item> WEARABLE_CHEST_CHESTPLATE = ITEMS.register("wearable_chest_chestplate", WearableChestItem.Chestplate::new);
    public static final RegistryObject<WearableCrownItem> WEARABLE_CROWN_HELMET = ITEMS.register("wearable_crown_helmet", () -> new WearableCrownItem(ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));
    public static final RegistryObject<Item> FLUORE_BERRIES = ITEMS.register("fluore_berries", FluoreBerriesItem::new);
    public static final RegistryObject<Item> RADIANT_BERRIES = ITEMS.register("radiant_berries", RadiantBerriesItem::new);
    public static final RegistryObject<Item> KING_OF_SARKAZ_VESSEL = ITEMS.register("king_of_sarkaz_vessel", KingOfSarkazVesselItem::new);
    public static final RegistryObject<Item> SARKAZ_KINGS_TORN_BANNER = ITEMS.register("sarkaz_kings_torn_banner", SarkazKingsTornBannerItem::new);
    public static final RegistryObject<Item> SARKAZ_KINGS_REGAL_REST = ITEMS.register("sarkaz_kings_regal_rest", SarkazKingsRegalRestItem::new);
    public static final RegistryObject<Item> ROYAL_FATE = ITEMS.register("royal_fate", RoyalFateItem::new);
    public static final RegistryObject<Item> BLOCK_FATE = block(ModBlocks.BLOCK_FATE);
    public static final RegistryObject<Item> CRIMSON_TREATY = ITEMS.register("crimson_treaty", CrimsonTreatyItem::new);
    public static final RegistryObject<Item> MEAT_CAN = ITEMS.register("meat_can", FeaturedCannedMeatItem::new);
    public static final RegistryObject<Item> EMPTY_CAN = ITEMS.register("empty_can", EmptyCanItem::new);
    public static final RegistryObject<Item> PAPER_BAG = ITEMS.register("paper_bag", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CAFFEINE = ITEMS.register("caffeine", CaffeineItem::new);
    public static final RegistryObject<Item> SOLO_MUSIC_BOX = ITEMS.register("solo_music_box", SoloMusicBoxItem::new);
    public static final RegistryObject<Item> MUSIC_BOX_FIXED = ITEMS.register("music_box_fixed", MusicBoxFixedItem::new);
    public static final RegistryObject<Item> REDSTONE_IRIS = block(ModBlocks.REDSTONE_IRIS);
    public static final RegistryObject<Item> ORIGINIUM_IRIS = ITEMS.register("originium_iris", OriginiumIrisItem::new);
    public static final RegistryObject<Item> REDSTONEIRIS_SEEDING = block(ModBlocks.REDSTONE_IRIS_SEEDING);
    public static final RegistryObject<Item> THERMOGRAPH = ITEMS.register("thermograph", ThermographItem::new);
    public static final RegistryObject<Item> GAULISH_TOPONYM_ORIGINS = ITEMS.register("gaulish_toponym_origins", GaulishToponymOriginsItem::new);

    public static final RegistryObject<Item> SEA_TRAIL_INIT = block(ModBlocks.SEA_TRAIL_INIT);
    public static final RegistryObject<Item> SEA_TRAIL_GROWING = block(ModBlocks.SEA_TRAIL_GROWING);
    public static final RegistryObject<Item> SEA_TRAIL_GROWN = block(ModBlocks.SEA_TRAIL_GROWN);
    public static final RegistryObject<Item> SEA_TRAIL_MOR = ITEMS.register("sea_trail_mor", SeaTrailMorItem::new);
    public static final RegistryObject<Item> BOMB_TRAILER = block(ModBlocks.BOMB_TRAILER);
    public static final RegistryObject<Item> CANNED_WATER = ITEMS.register("canned_water", CannedWaterItem::new);
    public static final RegistryObject<Item> CANNED_LAVA = ITEMS.register("canned_lava", CannedLavaItem::new);
    public static final RegistryObject<Item> BONE_SHARD = ITEMS.register("bone_shard", BoneShardItem::new);
    public static final RegistryObject<Item> OCEAN_PHLOEM = ITEMS.register("ocean_phloem", OceanPhloemItem::new);
    public static final RegistryObject<Item> OCEAN_FIBRE = ITEMS.register("ocean_fibre", OceanFibreItem::new);
    public static final RegistryObject<Item> OCEAN_EYE = ITEMS.register("ocean_eye", OceanEyeItem::new);
    public static final RegistryObject<Item> OCEAN_CRYSTAL = ITEMS.register("ocean_crystal", OceanCrystalItem::new);
    public static final RegistryObject<Item> OCEAN_CUTIN = ITEMS.register("ocean_cutin", OceanCutinItem::new);
    public static final RegistryObject<Item> COOKED_FIBRE = ITEMS.register("cooked_fibre", CookedFibreItem::new);
    public static final RegistryObject<Item> OCEAN_GLASS = block(ModBlocks.OCEAN_GLASS);
    public static final RegistryObject<Item> OCEAN_GLASSPANE = block(ModBlocks.OCEAN_GLASSPANE);
    public static final RegistryObject<Item> OCEAN_CHITIN = ITEMS.register("ocean_chitin", OceanChitinItem::new);
    public static final RegistryObject<Item> CHITIN_BLOCK = block(ModBlocks.CHITIN_BLOCK);
    public static final RegistryObject<Item> COOKED_MOR = ITEMS.register("cooked_mor", CookedMorItem::new);
    public static final RegistryObject<Item> SWORD_OCEAN_CRYSTAL = ITEMS.register("sword_ocean_crystal", SwordOceanCrystalItem::new);
    public static final RegistryObject<Item> CHITIN_HELMET = ITEMS.register("chitin_helmet", () -> new ArmorItem(ModArmorMaterial.CHITIN, ArmorItem.Type.HELMET, new Item.Properties()));
    public static final RegistryObject<Item> CHITIN_CHESTPLATE = ITEMS.register("chitin_chestplate", () -> new ArmorItem(ModArmorMaterial.CHITIN, ArmorItem.Type.CHESTPLATE, new Item.Properties()));
    public static final RegistryObject<Item> CHITIN_LEGGINGS = ITEMS.register("chitin_leggings", () -> new ArmorItem(ModArmorMaterial.CHITIN, ArmorItem.Type.LEGGINGS, new Item.Properties()));
    public static final RegistryObject<Item> CHITIN_BOOTS = ITEMS.register("chitin_boots", () -> new ArmorItem(ModArmorMaterial.CHITIN, ArmorItem.Type.BOOTS, new Item.Properties()));
    public static final RegistryObject<Item> CHITIN_PICKAXE = ITEMS.register("chitin_pickaxe", ChitinPickaxeItem::new);
    public static final RegistryObject<Item> CHITIN_AXE = ITEMS.register("chitin_axe", ChitinAxeItem::new);
    public static final RegistryObject<Item> CHITIN_SHOVEL = ITEMS.register("chitin_shovel", ChitinShovelItem::new);
    public static final RegistryObject<Item> CHITIN_HOE = ITEMS.register("chitin_hoe", ChitinHoeItem::new);
    public static final RegistryObject<Item> CHITIN_SWORD = ITEMS.register("chitin_sword", ChitinSwordItem::new);
    public static final RegistryObject<Item> PICKAXE_OCEAN_CRYSTAL = ITEMS.register("pickaxe_ocean_crystal", OceanthornPickaxeItem::new);
    public static final RegistryObject<Item> AXE_OCEAN_CRYSTAL = ITEMS.register("axe_ocean_crystal", OceanthornAxeItem::new);
    public static final RegistryObject<Item> SHOVEL_OCEAN_CRYSTAL = ITEMS.register("shovel_ocean_crystal", OceanthornShovelItem::new);
    public static final RegistryObject<Item> HOE_OCEAN_CRYSTAL = ITEMS.register("hoe_ocean_crystal", OceanthornHoeItem::new);
    public static final RegistryObject<Item> CUTIN_STICK = ITEMS.register("cutin_stick", CutinStickItem::new);
    public static final RegistryObject<Item> BLOCK_KETTLE = block(ModBlocks.BLOCK_KETTLE);
    public static final RegistryObject<Item> INSTANT_NOODLE = ITEMS.register("instant_noodle", InstantNoodleItem::new);
    public static final RegistryObject<Item> CANNED_NOODLE = ITEMS.register("canned_noodle", CannedNoodleItem::new);
    public static final RegistryObject<Item> CANNED_BOILED_WATER = ITEMS.register("canned_boiled_water", CannedBoiledWaterItem::new);
    public static final RegistryObject<Item> OBSIDIAN_BALL = ITEMS.register("obsidian_ball", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHITINOUS_RIPPER = ITEMS.register("chitin_knife", ChitinousRipperItem::new);
    public static final RegistryObject<Item> COMPLEX_CHITIN = ITEMS.register("complex_chitin", () -> new Item(new Item.Properties().fireResistant().rarity(Rarity.UNCOMMON)));
    public static final RegistryObject<Item> COMPLEX_CHITIN_SWORD = ITEMS.register("complex_chitin_sword", ComplexChitinSwordItem::new);
    public static final RegistryObject<Item> OCEAN_TRIM_TEMPLATE = ITEMS.register("ocean_trim_template", OceanTrimTemplateItem::new);
    public static final RegistryObject<Item> COMPLEX_CHITIN_HELMET = ITEMS.register("complex_chitin_helmet", () -> new ComplexChitinArmorItem(ArmorItem.Type.HELMET));
    public static final RegistryObject<Item> COMPLEX_CHITIN_CHESTPLATE = ITEMS.register("complex_chitin_chestplate", () -> new ComplexChitinArmorItem(ArmorItem.Type.CHESTPLATE));
    public static final RegistryObject<Item> COMPLEX_CHITIN_LEGGINGS = ITEMS.register("complex_chitin_leggings", () -> new ComplexChitinArmorItem(ArmorItem.Type.LEGGINGS));
    public static final RegistryObject<Item> COMPLEX_CHITIN_BOOTS = ITEMS.register("complex_chitin_boots", () -> new ComplexChitinArmorItem(ArmorItem.Type.BOOTS));
    public static final RegistryObject<Item> COMPLEX_CHITIN_PICKAXE = ITEMS.register("complex_chitin_pickaxe", ComplexChitinPickaxeItem::new);
    public static final RegistryObject<Item> COMPLEX_CHITIN_AXE = ITEMS.register("complex_chitin_axe", ComplexChitinAxeItem::new);
    public static final RegistryObject<Item> COMPLEX_CHITIN_SHOVEL = ITEMS.register("complex_chitin_shovel", ComplexChitinShovelItem::new);
    public static final RegistryObject<Item> COMPLEX_CHITIN_HOE = ITEMS.register("complex_chitin_hoe", ComplexChitinHoeItem::new);
    public static final RegistryObject<Item> OCEAN_FARMLAND = block(ModBlocks.OCEAN_FARMLAND);
    public static final RegistryObject<Item> PHLOEM_BOW = ITEMS.register("phloem_bow", PhloemBowItem::new);
    public static final RegistryObject<Item> LEGENDARY_SPEAR = ITEMS.register("legendary_spear", LegendarySpearItem::new);
    public static final RegistryObject<Item> ALLAY_SCULPTURE = ITEMS.register("allay_sculpture", StoneGargoyleItem::new);
    public static final RegistryObject<Item> ALLAY_BLOCK = block(ModBlocks.ALLAY_BLOCK);
    public static final RegistryObject<Item> BLOCK_BATBED = block(ModBlocks.BLOCK_BATBED);
    public static final RegistryObject<Item> BATBED_UPPER = block(ModBlocks.BATBED_UPPER);
    public static final RegistryObject<Item> UNIVERSAL_KEY = ITEMS.register("universal_key", UniversalKeyItem::new);
    public static final RegistryObject<Item> SCORE = ITEMS.register("score", AntiquatedSheetMusicItem::new);
    public static final RegistryObject<Item> LETTER_TERMINATION_CONTRACT = ITEMS.register("rescission", RescissionItem::new);
    public static final RegistryObject<Item> LANGUAGE_KEY = ITEMS.register("language_key", LanguageKeyItem::new);
    public static final RegistryObject<Item> TRAIL_CAKE = block(ModBlocks.TRAIL_CAKE);
    public static final RegistryObject<Item> TRAIL_CAKE_PIECE = ITEMS.register("trail_cake_piece", TrailCakePieceItem::new);
    public static final RegistryObject<Item> CARAMEL_CAKE = block(ModBlocks.CARAMEL_CAKE);
    public static final RegistryObject<Item> CARAMEL_CAKE_PIECE = ITEMS.register("caramel_cake_piece", CaramelCakePieceItem::new);
    public static final RegistryObject<Item> CARAMEL_MOR = ITEMS.register("caramel_mor", CaramelMorItem::new);
    public static final RegistryObject<Item> FAKE_EGG = ITEMS.register("fake_egg", FakeEggItem::new);
    public static final RegistryObject<Item> REAL_EGG = ITEMS.register("real_egg", RealEggItem::new);
    public static final RegistryObject<Item> FERMENTED_OCEAN_EYE = ITEMS.register("fermented_ocean_eye", FermentedOceanEyeItem::new);
    public static final RegistryObject<Item> OCEAN_MACHINE = ITEMS.register("ocean_machine", OceanMachineItem::new);
    public static final RegistryObject<Item> OCEAN_CRYSTAL_BLOCK = block(ModBlocks.OCEAN_CRYSTAL_BLOCK);
    public static final RegistryObject<Item> COMPLEX_CHITIN_BLOCK = block(ModBlocks.COMPLEX_CHITIN_BLOCK);
    public static final RegistryObject<Item> BANSHEE_KISS = ITEMS.register("banshee_kiss", BansheeKissItem::new);
    public static final RegistryObject<Item> HAND_SWORD = ITEMS.register("hand_sword", HandSwordItem::new);
    public static final RegistryObject<Item> TIDE_OBSERVATION = block(ModBlocks.TIDE_OBSERVATION);
    public static final RegistryObject<Item> SAMPLE_SUBSISTING = ITEMS.register("sample_subsisting", SampleSubsistingItem::new);
    public static final RegistryObject<Item> SAMPLE_GROW = ITEMS.register("sample_grow", SampleGrowItem::new);
    public static final RegistryObject<Item> SAMPLE_BREED = ITEMS.register("sample_breed", SampleBreedItem::new);
    public static final RegistryObject<Item> SAMPLE_MIGRATION = ITEMS.register("sample_migration", SampleMigrationItem::new);
    public static final RegistryObject<Item> ANCHOR_LOWER = block(ModBlocks.ANCHOR_LOWER);
    public static final RegistryObject<Item> ANCHOR_MEDIUM = block(ModBlocks.ANCHOR_MEDIUM);
    public static final RegistryObject<Item> ANCHOR_UPPER = block(ModBlocks.ANCHOR_UPPER);
    public static final RegistryObject<Item> TRAILED_WOODEN_SWORD = ITEMS.register("trailed_wooden_sword", TrailedWoodenSwordItem::new);
    public static final RegistryObject<Item> TRAILED_STONE_SWORD = ITEMS.register("trailed_stone_sword", TrailedStoneSwordItem::new);
    public static final RegistryObject<Item> TRAILED_IRON_SWORD = ITEMS.register("trailed_iron_sword", TrailedIronSwordItem::new);
    public static final RegistryObject<Item> TRAILED_DIAMOND_SWORD = ITEMS.register("trailed_diamond_sword", TrailedDiamondSwordItem::new);
    public static final RegistryObject<Item> TRAILED_NETHERITE_SWORD = ITEMS.register("trailed_netherite_sword", TrailedNetheriteSwordItem::new);
    public static final RegistryObject<Item> TRAILED_GOLDEN_SWORD = ITEMS.register("trailed_golden_sword", TrailedGoldenSwordItem::new);
    public static final RegistryObject<Item> TRAIL_CREAM = ITEMS.register("trail_cream", TrailCreamItem::new);
    public static final RegistryObject<Item> DISPATCH_STICK = ITEMS.register("dispatch_stick", DispatchStickItem::new);
    public static final RegistryObject<Item> BROKEN_OCEAN_CELL = ITEMS.register("broken_ocean_cell", BrokenOceanCellItem::new);
    public static final RegistryObject<Item> BROKEN_CELL_CLUSTER = ITEMS.register("broken_cell_cluster", BrokenCellClusterItem::new);
    public static final RegistryObject<Item> OCEAN_CELL = ITEMS.register("ocean_cell", OceanCellItem::new);
    public static final RegistryObject<Item> CELL_CLUSTER = ITEMS.register("cell_cluster", CellClusterItem::new);
    public static final RegistryObject<Item> COOKED_BROKEN_CELL_CLUSTER = ITEMS.register("cooked_broken_cell_cluster", CookedBrokenCellClusterItem::new);
    public static final RegistryObject<Item> COOKED_CELL_CLUSTER = ITEMS.register("cooked_cell_cluster", CookedCellClusterItem::new);
    public static final RegistryObject<Item> TRANSFORM_CELL = ITEMS.register("transform_cell", TransformCellItem::new);
    public static final RegistryObject<Item> BLOCK_RECORDER = block(ModBlocks.BLOCK_RECORDER);
    public static final RegistryObject<Item> SEA_TRAIL_SOLID = block(ModBlocks.SEA_TRAIL_SOLID);
    public static final RegistryObject<Item> TRAIL_BRICK = block(ModBlocks.TRAIL_BRICK);
    public static final RegistryObject<Item> TRAIL_SLAB = block(ModBlocks.TRAIL_SLAB);
    public static final RegistryObject<Item> TRAIL_STAIR = block(ModBlocks.TRAIL_STAIR);
    public static final RegistryObject<Item> TRAIL_BUTTON = block(ModBlocks.TRAIL_BUTTON);
    public static final RegistryObject<Item> TRAIL_PRESSURE_PLATE = block(ModBlocks.TRAIL_PRESSURE_PLATE);
    public static final RegistryObject<Item> TRAIL_TILE = block(ModBlocks.TRAIL_TILE);
    public static final RegistryObject<Item> CAERULA_ANIMUS = ITEMS.register("caerula_animus", CaerulaAnimusItem::new);
    public static final RegistryObject<Item> COIN_OF_TRADE = ITEMS.register("coin_of_trade", CoinOfTradeItem::new);
    public static final RegistryObject<Item> OCEAN_PEDUNCLE = ITEMS.register("ocean_peduncle", OceanPeduncleItem::new);
    public static final RegistryObject<Item> ELITE_PEDUNCLE = ITEMS.register("elite_peduncle", ElitePeduncleItem::new);
    public static final RegistryObject<Item> COOKED_PEDUNCLE = ITEMS.register("cooked_peduncle", CookedPeduncleItem::new);
    public static final RegistryObject<Item> OCEAN_ARROW = ITEMS.register("ocean_arrow", OceanArrowItem::new);

    /**
     * Relics
     */
    public static final DeferredRegister<Item> RELICS = DeferredRegister.create(ForgeRegistries.ITEMS, CaerulaArborMod.MODID);

    public static final RegistryObject<Item> FEATURED_CANNED_MEAT = RELICS.register("featured_canned_meat", FeaturedCannedMeatItem::new);
    public static final RegistryObject<Item> SEAWEED_SALAD = RELICS.register("seaweed_salad", SeaweedSaladItem::new);
    public static final RegistryObject<Item> ORANGE_STORM = ITEMS.register("orange_storm", OrangeStormItem::new);
    public static final RegistryObject<Item> COFFEE_PLAINS_COFFEE_CANDY = RELICS.register("coffee_plains_coffee_candy", CoffeePlainsCoffeeCandyItem::new);
    public static final RegistryObject<Item> SCREAMING_CHERRY = RELICS.register("screaming_cherry", ScreamingCherryItem::new);
    public static final RegistryObject<Item> PITTS_ASSORTED_FRUITS = RELICS.register("pitts_assorted_fruits", PittsAssortedFruitsItem::new);
    public static final RegistryObject<Item> EXTRA_PUNGENT_COFFEE_BEANS = RELICS.register("extra_pungent_coffee_beans", ExtraPungentCoffeeBeansItem::new);

    public static final RegistryObject<Item> HOT_WATER_KETTLE = RELICS.register("hot_water_kettle", HotWaterKettleItem::new);
    public static final RegistryObject<Item> VAMPIRES_BED = RELICS.register("vampires_bed", VampiresBedItem::new);

    public static final RegistryObject<Item> PURE_GOLD_EXPEDITION = RELICS.register("pure_gold_expedition", PureGoldExpeditionItem::new);
    public static final RegistryObject<Item> PROOF_OF_LONGEVITY = RELICS.register("proof_of_longevity", ProofOfLongevityItem::new);
    public static final RegistryObject<Item> WEIRD_FLUTE = RELICS.register("weird_flute", WeirdFluteItem::new);
    public static final RegistryObject<Item> DURIN_OVERGROUND_ODYSSEY = RELICS.register("durin_overground_odyssey", DurinOvergroundOdysseyItem::new);

    public static final RegistryObject<Item> HAND_OF_SPOTLESS = RELICS.register("hand_of_spotless", HandOfSwipeItem::new);
    public static final RegistryObject<Item> HAND_OF_FIREWORK = RELICS.register("hand_of_firework", HandOfFireworkItem::new);
    public static final RegistryObject<Item> HAND_OF_BRANDING = RELICS.register("hand_of_branding", HandOfBrandingItem::new);
    public static final RegistryObject<Item> HAND_OF_SPIKES = RELICS.register("hand_of_spikes", HandOfSpikesItem::new);
    public static final RegistryObject<Item> HAND_OF_CHOKER = RELICS.register("hand_of_choker", HandOfChokerItem::new);
    public static final RegistryObject<Item> HAND_OF_FERTILIY = RELICS.register("hand_of_fertiliy", HandOfFertiliyItem::new);
    public static final RegistryObject<Item> HAND_OF_SUPERSPEED = RELICS.register("hand_of_superspeed", HandOfSuperspeedItem::new);
    public static final RegistryObject<Item> HAND_OF_PULVERIZATION = RELICS.register("hand_of_pulverization", HandOfPulverizationItem::new);

    public static final RegistryObject<Item> SURVIVOR_CONTRACT = RELICS.register("survivor_contract", SurvivorContractItem::new);

    /**
     * Spawn Eggs
     */
    public static final DeferredRegister<Item> SPAWN_EGGS = DeferredRegister.create(ForgeRegistries.ITEMS, CaerulaArborMod.MODID);

    public static final RegistryObject<Item> SHELL_SEA_RUNNER_SPAWN_EGG = SPAWN_EGGS.register("shell_sea_runner_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SHELL_SEA_RUNNER, -16777012, -3355444, new Item.Properties()));
    public static final RegistryObject<Item> DEEP_SEA_SLIDER_SPAWN_EGG = SPAWN_EGGS.register("deep_sea_slider_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.DEEP_SEA_SLIDER, -13421569, -3355444, new Item.Properties()));
    public static final RegistryObject<Item> SUPER_SLIDER_SPAWN_EGG = SPAWN_EGGS.register("super_slider_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SUPER_SLIDER, -13434778, -3355393, new Item.Properties()));
    public static final RegistryObject<Item> RIDGE_SEA_SPITTER_SPAWN_EGG = SPAWN_EGGS.register("ridge_sea_spitter_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.RIDGE_SEA_SPITTER, -13434676, -3355444, new Item.Properties()));
    public static final RegistryObject<Item> FLOATING_SEA_DRIFTER_SPAWN_EGG = SPAWN_EGGS.register("floating_sea_drifter_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.FLOATING_SEA_DRIFTER, -16737895, -3355444, new Item.Properties()));
    public static final RegistryObject<Item> BASIN_SEA_REAPER_SPAWN_EGG = SPAWN_EGGS.register("basin_sea_reaper_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.BASIN_SEA_REAPER, -6710785, -3355444, new Item.Properties()));
    public static final RegistryObject<Item> POCKET_SEA_CRAWLER_SPAWN_EGG = SPAWN_EGGS.register("pocket_sea_crawler_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.POCKET_SEA_CRAWLER, -10092442, -3342337, new Item.Properties()));
    public static final RegistryObject<Item> PRIMAL_SEA_PIERCER_SPAWN_EGG = SPAWN_EGGS.register("primal_sea_piercer_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.PRIMAL_SEA_PIERCER, -16750900, -1, new Item.Properties()));
    public static final RegistryObject<Item> NETHERSEA_FOUNDER_SPAWN_EGG = SPAWN_EGGS.register("nethersea_founder_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.NETHERSEA_FOUNDER, -16776961, -2056595, new Item.Properties()));
    public static final RegistryObject<Item> NETHERSEA_PREDATOR_SPAWN_EGG = SPAWN_EGGS.register("nethersea_predator_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.NETHERSEA_PREDATOR, -16777063, -2056595, new Item.Properties()));
    public static final RegistryObject<Item> NETHERSEA_BRANDGUIDER_SPAWN_EGG = SPAWN_EGGS.register("nethersea_brandguider_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.NETHERSEA_BRANDGUIDER, -13434778, -2056595, new Item.Properties()));
    public static final RegistryObject<Item> NETHERSEA_SPEWER_SPAWN_EGG = SPAWN_EGGS.register("nethersea_spewer_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.NETHERSEA_SPEWER, -16777012, -2056595, new Item.Properties()));
    public static final RegistryObject<Item> NETHERSEA_SWARMCALLER_SPAWN_EGG = SPAWN_EGGS.register("nethersea_swarmcaller_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.NETHERSEA_SWARMCALLER, -10066177, -2056595, new Item.Properties()));
    public static final RegistryObject<Item> NETHERSEA_REEFBREAKER_SPAWN_EGG = SPAWN_EGGS.register("nethersea_reefbreaker_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.NETHERSEA_REEFBREAKER, -16777012, -2056595, new Item.Properties()));
    public static final RegistryObject<Item> UNICELLULAR_PREDATOR_SPAWN_EGG = SPAWN_EGGS.register("unicellular_predator_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.UNICELLULAR_PREDATOR, -16776961, -16316408, new Item.Properties()));
    public static final RegistryObject<Item> BONE_SEA_DRIFTER_SPAWN_EGG = SPAWN_EGGS.register("bone_sea_drifter_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.BONE_SEA_DRIFTER, -10066177, -1, new Item.Properties()));
    public static final RegistryObject<Item> OCEAN_STONECUTTER_SPAWN_EGG = SPAWN_EGGS.register("ocean_stonecutter_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.OCEAN_STONECUTTER, -10053121, -1, new Item.Properties()));
    public static final RegistryObject<Item> RETCHING_BROODMOTHER_SPAWN_EGG = SPAWN_EGGS.register("retching_broodmother_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.RETCHING_BROODMOTHER, -10066177, -1, new Item.Properties()));
    public static final RegistryObject<Item> BALEFUL_BROODLING_SPAWN_EGG = SPAWN_EGGS.register("baleful_broodling_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.BALEFUL_BROODLING, -856596, -6711916, new Item.Properties()));
    public static final RegistryObject<Item> SKIMMING_SEA_DRIFTER_SPAWN_EGG = SPAWN_EGGS.register("skimming_sea_drifter_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SKIMMING_SEA_DRIFTER, -16764007, -1, new Item.Properties()));
    public static final RegistryObject<Item> ROUTE_SHAPER_SPAWN_EGG = SPAWN_EGGS.register("path_shaper_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.PATH_SHAPER, -16764007, -10040065, new Item.Properties()));
    public static final RegistryObject<Item> ROUTE_FRACTAL_SPAWN_EGG = SPAWN_EGGS.register("pathshaper_fractal_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.PATHSHAPER_FRACTAL, -16777165, -16764109, new Item.Properties()));

    private static RegistryObject<Item> block(RegistryObject<Block> block) {
        return ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
        RELICS.register(bus);
        SPAWN_EGGS.register(bus);
    }

    @SubscribeEvent
    public static void clientLoad(FMLClientSetupEvent event) {
        event.enqueueWork(() -> ItemProperties.register(MUSIC_BOX_FIXED.get(), new ResourceLocation("caerula_arbor:music_box_fixed_playing"),
                (stack, clientWorld, entity, itemEntityId) -> {
                    if (entity instanceof Player player && player.getCooldowns().isOnCooldown(stack.getItem())) {
                        return 1;
                    }
                    return 0;
                }));
    }
}
