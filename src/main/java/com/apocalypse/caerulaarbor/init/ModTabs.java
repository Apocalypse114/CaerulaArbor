package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModTabs {

    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CaerulaArborMod.MODID);

    public static final RegistryObject<CreativeModeTab> CAERULA_ITEMS = REGISTRY.register("caerula_items",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.caerula_arbor.caerula_items"))
                    .icon(() -> new ItemStack(ModItems.CAERULA_RECORDER.get()))
                    .displayItems((parameters, tabData) -> {
                        tabData.accept(ModItems.CAERULA_RECORDER.get());
                        tabData.accept(ModItems.THERMOGRAPH.get());
                        tabData.accept(ModItems.TRAILED_WOODEN_SWORD.get());
                        tabData.accept(ModItems.TRAILED_STONE_SWORD.get());
                        tabData.accept(ModItems.TRAILED_IRON_SWORD.get());
                        tabData.accept(ModItems.TRAILED_DIAMOND_SWORD.get());
                        tabData.accept(ModItems.TRAILED_NETHERITE_SWORD.get());
                        tabData.accept(ModItems.TRAILED_GOLDEN_SWORD.get());
                        tabData.accept(ModItems.SWORD_OCEAN_CRYSTAL.get());
                        tabData.accept(ModItems.PICKAXE_OCEAN_CRYSTAL.get());
                        tabData.accept(ModItems.AXE_OCEAN_CRYSTAL.get());
                        tabData.accept(ModItems.SHOVEL_OCEAN_CRYSTAL.get());
                        tabData.accept(ModItems.HOE_OCEAN_CRYSTAL.get());
                        tabData.accept(ModItems.CHITIN_SWORD.get());
                        tabData.accept(ModItems.CHITIN_PICKAXE.get());
                        tabData.accept(ModItems.CHITIN_AXE.get());
                        tabData.accept(ModItems.CHITIN_SHOVEL.get());
                        tabData.accept(ModItems.CHITIN_HOE.get());
                        tabData.accept(ModItems.COMPLEX_CHITIN_SWORD.get());
                        tabData.accept(ModItems.COMPLEX_CHITIN_PICKAXE.get());
                        tabData.accept(ModItems.COMPLEX_CHITIN_AXE.get());
                        tabData.accept(ModItems.COMPLEX_CHITIN_SHOVEL.get());
                        tabData.accept(ModItems.COMPLEX_CHITIN_HOE.get());
                        tabData.accept(ModItems.LEGENDARY_SPEAR.get());
                        tabData.accept(ModItems.PHLOEM_BOW.get());
                        tabData.accept(ModItems.THE_SPEAR.get());
                        tabData.accept(ModItems.MUSIC_BOX_FIXED.get());
                        tabData.accept(ModItems.FLUORE_BERRIES.get());
                        tabData.accept(ModItems.RADIANT_BERRIES.get());
                        tabData.accept(ModBlocks.TRAIL_CAKE.get().asItem());
                        tabData.accept(ModItems.TRAIL_CAKE_PIECE.get());
                        tabData.accept(ModBlocks.CARAMEL_CAKE.get().asItem());
                        tabData.accept(ModItems.CARAMEL_CAKE_PIECE.get());
                        tabData.accept(ModBlocks.REDSTONE_IRIS.get().asItem());
                        tabData.accept(ModBlocks.REDSTONE_IRIS_SEEDING.get().asItem());
                        tabData.accept(ModBlocks.SEA_TRAIL_INIT.get().asItem());
                        tabData.accept(ModBlocks.SEA_TRAIL_GROWING.get().asItem());
                        tabData.accept(ModBlocks.SEA_TRAIL_GROWN.get().asItem());
                        tabData.accept(ModItems.WEARABLE_CROWN_HELMET.get());
                        tabData.accept(ModItems.WEARABLE_CHEST_CHESTPLATE.get());
                        tabData.accept(ModItems.CHITIN_HELMET.get());
                        tabData.accept(ModItems.CHITIN_CHESTPLATE.get());
                        tabData.accept(ModItems.CHITIN_LEGGINGS.get());
                        tabData.accept(ModItems.CHITIN_BOOTS.get());
                        tabData.accept(ModItems.COMPLEX_CHITIN_HELMET.get());
                        tabData.accept(ModItems.COMPLEX_CHITIN_CHESTPLATE.get());
                        tabData.accept(ModItems.COMPLEX_CHITIN_LEGGINGS.get());
                        tabData.accept(ModItems.COMPLEX_CHITIN_BOOTS.get());
                        tabData.accept(ModBlocks.KINGS_ARMOR.get().asItem());
                        tabData.accept(ModBlocks.BLOCK_CROWN.get().asItem());
                        tabData.accept(ModBlocks.BLOCK_SPEAR.get().asItem());
                        tabData.accept(ModBlocks.BLOCK_EXTENSION.get().asItem());
                        tabData.accept(ModBlocks.BLOCK_CRYSTAL.get().asItem());
                        tabData.accept(ModBlocks.BLOCK_FATE.get().asItem());
                        tabData.accept(ModBlocks.SCREAMING_CHERRY.get().asItem());
                        tabData.accept(ModBlocks.BLOCK_RECORDER.get().asItem());
                        tabData.accept(ModBlocks.TIDE_OBSERVATION.get().asItem());
                        tabData.accept(ModBlocks.BOMB_TRAILER.get().asItem());
                        tabData.accept(ModBlocks.ANCHOR_LOWER.get().asItem());
                        tabData.accept(ModBlocks.ANCHOR_MEDIUM.get().asItem());
                        tabData.accept(ModBlocks.ANCHOR_UPPER.get().asItem());
                        tabData.accept(ModBlocks.CHITIN_BLOCK.get().asItem());
                        tabData.accept(ModBlocks.OCEAN_CRYSTAL_BLOCK.get().asItem());
                        tabData.accept(ModBlocks.COMPLEX_CHITIN_BLOCK.get().asItem());
                        tabData.accept(ModBlocks.OCEAN_GLASS.get().asItem());
                        tabData.accept(ModBlocks.OCEAN_GLASSPANE.get().asItem());
                        tabData.accept(ModBlocks.SEA_TRAIL_SOLID.get().asItem());
                        tabData.accept(ModBlocks.TRAIL_BRICK.get().asItem());
                        tabData.accept(ModBlocks.TRAIL_TILE.get().asItem());
                        tabData.accept(ModBlocks.TRAIL_SLAB.get().asItem());
                        tabData.accept(ModBlocks.TRAIL_STAIR.get().asItem());
                        tabData.accept(ModBlocks.TRAIL_BUTTON.get().asItem());
                        tabData.accept(ModBlocks.TRAIL_PRESSURE_PLATE.get().asItem());
                        tabData.accept(ModBlocks.BLOCK_KETTLE.get().asItem());
                        tabData.accept(ModBlocks.ALLAY_BLOCK.get().asItem());
                        tabData.accept(ModBlocks.BLOCK_BATBED.get().asItem());
                        tabData.accept(ModItems.REDSTONE_INGOT.get());
                        tabData.accept(ModItems.COIN_OF_TRADE.get());
                        tabData.accept(ModItems.OCEAN_EYE.get());
                        tabData.accept(ModItems.FERMENTED_OCEAN_EYE.get());
                        tabData.accept(ModItems.OCEAN_MACHINE.get());
                        tabData.accept(ModItems.OCEAN_TRIM_TEMPLATE.get());
                        tabData.accept(ModItems.COMPLEX_CHITIN.get());
                        tabData.accept(ModItems.EMPTY_CAN.get());
                        tabData.accept(ModItems.CANNED_WATER.get());
                        tabData.accept(ModItems.CANNED_LAVA.get());
                        tabData.accept(ModItems.CANNED_BOILED_WATER.get());
                        tabData.accept(ModItems.PAPER_BAG.get());
                        tabData.accept(ModItems.CAFFEINE.get());
                        tabData.accept(ModItems.SEA_TRAIL_MOR.get());
                        tabData.accept(ModItems.COOKED_MOR.get());
                        tabData.accept(ModItems.CARAMEL_MOR.get());
                        tabData.accept(ModItems.BONE_SHARD.get());
                        tabData.accept(ModItems.OCEAN_PHLOEM.get());
                        tabData.accept(ModItems.OCEAN_FIBRE.get());
                        tabData.accept(ModItems.COOKED_FIBRE.get());
                        tabData.accept(ModItems.OCEAN_CRYSTAL.get());
                        tabData.accept(ModItems.OCEAN_CUTIN.get());
                        tabData.accept(ModItems.CUTIN_STICK.get());
                        tabData.accept(ModItems.OCEAN_CHITIN.get());
                        tabData.accept(ModItems.INSTANT_NOODLE.get());
                        tabData.accept(ModItems.OBSIDIAN_BALL.get());
                        tabData.accept(ModItems.FAKE_EGG.get());
                        tabData.accept(ModItems.REAL_EGG.get());
                        tabData.accept(ModItems.TRAIL_CREAM.get());
                        tabData.accept(ModItems.BROKEN_OCEAN_CELL.get());
                        tabData.accept(ModItems.BROKEN_CELL_CLUSTER.get());
                        tabData.accept(ModItems.OCEAN_CELL.get());
                        tabData.accept(ModItems.CELL_CLUSTER.get());
                        tabData.accept(ModItems.COOKED_BROKEN_CELL_CLUSTER.get());
                        tabData.accept(ModItems.COOKED_CELL_CLUSTER.get());
                        tabData.accept(ModItems.TRANSFORM_CELL.get());
                        tabData.accept(ModItems.OCEAN_PEDUNCLE.get());
                        tabData.accept(ModItems.COOKED_PEDUNCLE.get());
                        tabData.accept(ModItems.ELITE_PEDUNCLE.get());
                        tabData.accept(ModItems.OCEAN_ARROW.get());
                    }).build());

    public static final RegistryObject<CreativeModeTab> CAERULA_RELICS = REGISTRY.register("caerula_relics",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("item_group.caerula_arbor.caerula_relics"))
                    .icon(() -> new ItemStack(ModItems.LUMINOUS_CORPSE.get()))
                    .displayItems((param, output) -> ModItems.RELICS.getEntries().forEach(registryObject -> output.accept(registryObject.get())))
                    .withTabsBefore(CAERULA_ITEMS.getId())
                    .build());


    // 这一堆是藏品，放这看一下
//    tabData.accept(CaerulaArborModItems.RELIC_CURSE_EMELIGHT.get());
//                        tabData.accept(ModItems.LUMINOUS_CORPSE.get());
//				tabData.accept(CaerulaArborModItems.RELIC_CURSED_RESEARCH.get());
//				tabData.accept(CaerulaArborModItems.CAERULA_HEART.get());
//				tabData.accept(CaerulaArborModItems.RELIC_CROWN.get());
//				tabData.accept(CaerulaArborModItems.KINGS_ARMOUR.get());
//				tabData.accept(CaerulaArborModItems.KINGS_SPEAR.get());
//				tabData.accept(CaerulaArborModItems.KINGS_EXTENSION.get());
//				tabData.accept(CaerulaArborModItems.KINGS_CRYSTAL.get());
//				tabData.accept(CaerulaArborModItems.ARCHFIENDS_ARTIFACT.get());
//				tabData.accept(CaerulaArborModItems.ARCHFIENDS_FLAG.get());
//				tabData.accept(CaerulaArborModItems.ARCHFIENDS_BED.get());
//				tabData.accept(CaerulaArborModItems.ROYAL_FATE.get());
//				tabData.accept(CaerulaArborModItems.CRIMSON_TREATY.get());
//				tabData.accept(CaerulaArborModItems.CHITIN_KNIFE.get());
//				tabData.accept(CaerulaArborModItems.HAND_OF_THORNS.get());
//				tabData.accept(CaerulaArborModItems.HAND_OF_STRANGLE.get());
//				tabData.accept(CaerulaArborModItems.HAND_OF_FERTILIY.get());
//				tabData.accept(CaerulaArborModItems.HAND_OF_SPEED.get());
//				tabData.accept(CaerulaArborModItems.HAND_OF_BARREN.get());
//				tabData.accept(CaerulaArborModItems.HAND_OF_SPOTLESS.get());
//				tabData.accept(CaerulaArborModItems.HAND_OF_FIREWORK.get());
//				tabData.accept(CaerulaArborModItems.HAND_OF_ENGRAVE.get());
//				tabData.accept(CaerulaArborModItems.HAND_SWORD.get());
//				tabData.accept(CaerulaArborModItems.BOWL_SEAGRASS.get());
//				tabData.accept(CaerulaArborModItems.GOLDEN_STORM.get());
//				tabData.accept(CaerulaArborModItems.AROMATIC_COFFEE.get());
//				tabData.accept(CaerulaArborModItems.SOLO_MUSIC_BOX.get());
//				tabData.accept(CaerulaArborModItems.REDSTONE_IRIS_FLOWER.get());
//				tabData.accept(CaerulaArborModItems.RESCISSION.get());
//				tabData.accept(CaerulaArborModItems.SCORE.get());
//                        tabData.accept(ModItems.UNIVERSAL_KEY.get());
//                        tabData.accept(ModItems.BANSHEE_KISS.get());
//                        tabData.accept(ModItems.GAULISH_TOPONYM_ORIGINS.get());
//                        tabData.accept(ModItems.STONE_GARGOYLE.get());
//                        tabData.accept(ModItems.PROOF_OF_LONGEVITY.get());

    public static final RegistryObject<CreativeModeTab> CAERULA_LIVINGS = REGISTRY.register("caerula_livings",
            () -> CreativeModeTab.builder().title(Component.translatable("item_group.caerula_arbor.caerula_livings")).icon(() -> new ItemStack(ModItems.LUMINOUS_CORPSE.get()))
                    .displayItems((param, output) -> ModItems.SPAWN_EGGS.getEntries().forEach(registryObject -> output.accept(registryObject.get())))
                    .withTabsBefore(CAERULA_RELICS.getId()).build());

    @SubscribeEvent
    public static void buildTabContentsVanilla(BuildCreativeModeTabContentsEvent tabData) {
        if (tabData.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            tabData.accept(ModItems.DISPATCH_STICK.get());
        }
    }
}
