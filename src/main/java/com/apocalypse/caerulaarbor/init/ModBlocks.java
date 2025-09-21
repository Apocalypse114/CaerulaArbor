package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.block.*;
import com.apocalypse.caerulaarbor.init.ModTabs;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

    public static final BlockSetType NETHERSEA = BlockSetType.register(new BlockSetType("nethersea"));

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CaerulaArborMod.MODID);

    public static final RegistryObject<Block> CHITIN_BLOCK = BLOCKS.register("chitin_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.BONE_BLOCK).strength(18f, 75f).requiresCorrectToolForDrops()));
    public static final RegistryObject<Block> COMPLEX_CHITIN_BLOCK = BLOCKS.register("complex_chitin_block", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.BONE_BLOCK).strength(24f, 95f).requiresCorrectToolForDrops()));

    public static final RegistryObject<Block> TIDE_OBSERVATION_STATION = BLOCKS.register("tide_observation_station", TideObservationStationBlock::new);

    public static final RegistryObject<Block> EMERGENCY_LIGHT = BLOCKS.register("emergency_light", EmergencyLightBlock::new);
    public static final RegistryObject<Block> KINGS_ARMOR = BLOCKS.register("kings_armor", KingsArmorBlock::new);
    public static final RegistryObject<Block> BLOCK_CROWN = BLOCKS.register("block_crown", BlockCrownBlock::new);
    public static final RegistryObject<Block> BLOCK_SPEAR = BLOCKS.register("block_spear", BlockSpearBlock::new);
    public static final RegistryObject<Block> BLOCK_EXTENSION = BLOCKS.register("block_extension", BlockExtensionBlock::new);
    public static final RegistryObject<Block> BLOCK_CRYSTAL = BLOCKS.register("block_crystal", BlockCrystalBlock::new);
    public static final RegistryObject<Block> BLOCK_FATE = BLOCKS.register("block_fate", BlockFateBlock::new);
    public static final RegistryObject<Block> SCREAMING_CHERRY = BLOCKS.register("screaming_cherry", ScreamingCherryBlock::new);
    public static final RegistryObject<Block> REDSTONE_IRIS = BLOCKS.register("redstone_iris", RedstoneIrisBlock::new);
    public static final RegistryObject<Block> REDSTONE_IRIS_SEEDING = BLOCKS.register("redstone_iris_seeding", RedstoneIrisSeedingBlock::new);
    public static final RegistryObject<Block> NETHERSEA_BRAND_INIT = BLOCKS.register("nethersea_brand_init", NetherseaBrandInitInitBlock::new);
    public static final RegistryObject<Block> NETHERSEA_BRAND_GROWING = BLOCKS.register("nethersea_brand_growing", NetherseaBrandGrowingBlock::new);
    public static final RegistryObject<Block> NETHERSEA_BRAND_GROWN = BLOCKS.register("nethersea_brand_grown", NetherseaBrandGrownBlock::new);
    public static final RegistryObject<Block> BOMB_TRAILER = BLOCKS.register("bomb_trailer", BombTrailerBlock::new);
    public static final RegistryObject<Block> OCEAN_GLASS = BLOCKS.register("ocean_glass", OceanGlassBlock::new);
    public static final RegistryObject<Block> OCEAN_GLASSPANE = BLOCKS.register("ocean_glasspane", OceanGlasspaneBlock::new);
    public static final RegistryObject<Block> BLOCK_KETTLE = BLOCKS.register("block_kettle", BlockKettleBlock::new);
    public static final RegistryObject<Block> OCEAN_FARMLAND = BLOCKS.register("ocean_farmland", OceanFarmlandBlock::new);
    public static final RegistryObject<Block> ALLAY_BLOCK = BLOCKS.register("allay_block", AllayBlockBlock::new);
    public static final RegistryObject<Block> BLOCK_BATBED = BLOCKS.register("block_batbed", BlockBatbedBlock::new);
    public static final RegistryObject<Block> BATBED_UPPER = BLOCKS.register("batbed_upper", BatbedUpperBlock::new);
    public static final RegistryObject<Block> TRAIL_CAKE = BLOCKS.register("trail_cake", TrailCakeBlock::new);
    public static final RegistryObject<Block> CARAMEL_CAKE = BLOCKS.register("caramel_cake", CaramelCakeBlock::new);
    public static final RegistryObject<Block> OCEAN_CRYSTAL_BLOCK = BLOCKS.register("ocean_crystal_block", OceanCrystalBlockBlock::new);
    public static final RegistryObject<Block> ANCHOR_LOWER = BLOCKS.register("anchor_lower", AnchorLowerBlock::new);
    public static final RegistryObject<Block> ANCHOR_MEDIUM = BLOCKS.register("anchor_medium", AnchorMediumBlock::new);
    public static final RegistryObject<Block> ANCHOR_UPPER = BLOCKS.register("anchor_upper", AnchorUpperBlock::new);
    public static final RegistryObject<Block> BLOCK_RECORDER = BLOCKS.register("block_recorder", BlockRecorderBlock::new);
    public static final RegistryObject<Block> NETHERSEA_BLOCK = BLOCKS.register("nethersea_block", NetherseaBlockBlock::new);

    public static final RegistryObject<Block> POOL_OF_PROCREATION = BLOCKS.register("pool_of_procreation", PoolOfProcreationBlock::new);
    public static final RegistryObject<Block> NOURISHED_POOL_OF_PROCREATION = BLOCKS.register("nourished_pool_of_procreation", NourishedPoolOfProcreationBlock::new);

    // Nethersea Series 深溟方块
    public static final RegistryObject<Block> NETHERSEA_BRICKS = BLOCKS.register("nethersea_bricks", () -> new Block(BlockBehaviour.Properties.of().sound(SoundType.SCULK_CATALYST).mapColor(MapColor.DEEPSLATE).friction(0.9f).speedFactor(0.9f).requiresCorrectToolForDrops().strength(3f, 12f)));
    public static final RegistryObject<Block> NETHERSEA_BRICK_STAIRS = BLOCKS.register("nethersea_brick_stairs", () -> new StairBlock(() -> NETHERSEA_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.copy(NETHERSEA_BRICKS.get())));
    public static final RegistryObject<Block> NETHERSEA_BRICK_SLAB = BLOCKS.register("nethersea_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().sound(SoundType.SCULK_CATALYST).mapColor(MapColor.DEEPSLATE).friction(0.9f).speedFactor(0.9f).requiresCorrectToolForDrops().strength(3f, 12f)));
    public static final RegistryObject<Block> NETHERSEA_BRICK_WALL = BLOCKS.register("nethersea_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(NETHERSEA_BRICKS.get()).forceSolidOn()));
    public static final RegistryObject<Block> NETHERSEA_TILES = BLOCKS.register("nethersea_tiles", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE_TILES).friction(0.9f).speedFactor(0.9f).requiresCorrectToolForDrops().strength(3f, 12f)));
    public static final RegistryObject<Block> NETHERSEA_TILE_STAIRS = BLOCKS.register("nethersea_tile_stairs", () -> new StairBlock(() -> NETHERSEA_TILES.get().defaultBlockState(), BlockBehaviour.Properties.copy(NETHERSEA_TILES.get())));
    public static final RegistryObject<Block> NETHERSEA_TILE_SLAB = BLOCKS.register("nethersea_tile_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).sound(SoundType.DEEPSLATE_TILES).friction(0.9f).speedFactor(0.9f).requiresCorrectToolForDrops().strength(3f, 12f)));
    public static final RegistryObject<Block> NETHERSEA_TILE_WALL = BLOCKS.register("nethersea_tile_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(NETHERSEA_TILES.get()).forceSolidOn()));
    public static final RegistryObject<Block> NETHERSEA_PRESSURE_PLATE = BLOCKS.register("nethersea_pressure_plate", () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.MOBS, BlockBehaviour.Properties.of().mapColor(MapColor.DEEPSLATE).sound(SoundType.SCULK_CATALYST).forceSolidOn().friction(0.9f).speedFactor(0.9f).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().noCollission().strength(0.5F).pushReaction(PushReaction.DESTROY), NETHERSEA));
    public static final RegistryObject<Block> NETHERSEA_BUTTON = BLOCKS.register("nethersea_button", () -> new ButtonBlock(BlockBehaviour.Properties.of().noCollission().strength(0.5F).mapColor(MapColor.DEEPSLATE).sound(SoundType.SCULK_CATALYST).pushReaction(PushReaction.DESTROY), NETHERSEA, 20, false));

    // Sal Viento Series 盐风方块
    public static final RegistryObject<Block> SAL_VIENTO_SAND = BLOCKS.register("sal_viento_sand", () -> new SandBlock(0xdbdad3, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
    public static final RegistryObject<Block> SAL_VIENTO_SANDSTONE = BLOCKS.register("sal_viento_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final RegistryObject<Block> SAL_VIENTO_SANDSTONE_STAIRS = BLOCKS.register("sal_viento_sandstone_stairs", () -> new StairBlock(() -> SAL_VIENTO_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(SAL_VIENTO_SANDSTONE.get())));
    public static final RegistryObject<Block> SAL_VIENTO_SANDSTONE_SLAB = BLOCKS.register("sal_viento_sandstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> SAL_VIENTO_SANDSTONE_WALL = BLOCKS.register("sal_viento_sandstone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(SAL_VIENTO_SANDSTONE.get()).forceSolidOn()));
    public static final RegistryObject<Block> CHISELED_SAL_VIENTO_SANDSTONE = BLOCKS.register("chiseled_sal_viento_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final RegistryObject<Block> CHISELED_SAL_VIENTO_SANDSTONE_WALL = BLOCKS.register("chiseled_sal_viento_sandstone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(CHISELED_SAL_VIENTO_SANDSTONE.get()).forceSolidOn()));
    public static final RegistryObject<Block> SMOOTH_SAL_VIENTO_SANDSTONE = BLOCKS.register("smooth_sal_viento_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> SMOOTH_SAL_VIENTO_SANDSTONE_STAIRS = BLOCKS.register("smooth_sal_viento_sandstone_stairs", () -> new StairBlock(() -> SMOOTH_SAL_VIENTO_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(SMOOTH_SAL_VIENTO_SANDSTONE.get())));
    public static final RegistryObject<Block> SMOOTH_SAL_VIENTO_SANDSTONE_SLAB = BLOCKS.register("smooth_sal_viento_sandstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> SQUARE_PATTERN_SAL_VIENTO_SANDSTONE = BLOCKS.register("square_pattern_sal_viento_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> SQUARE_PATTERN_SAL_VIENTO_SANDSTONE_STAIRS = BLOCKS.register("square_pattern_sal_viento_sandstone_stairs", () -> new StairBlock(() -> SQUARE_PATTERN_SAL_VIENTO_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(SQUARE_PATTERN_SAL_VIENTO_SANDSTONE.get())));
    public static final RegistryObject<Block> SQUARE_PATTERN_SAL_VIENTO_SANDSTONE_SLAB = BLOCKS.register("square_pattern_sal_viento_sandstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> CUT_SAL_VIENTO_SANDSTONE = BLOCKS.register("cut_sal_viento_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final RegistryObject<Block> CUT_SAL_VIENTO_SANDSTONE_STAIRS = BLOCKS.register("cut_sal_viento_sandstone_stairs", () -> new StairBlock(() -> CUT_SAL_VIENTO_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(CUT_SAL_VIENTO_SANDSTONE.get())));
    public static final RegistryObject<Block> CUT_SAL_VIENTO_SANDSTONE_SLAB = BLOCKS.register("cut_sal_viento_sandstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> CUT_SAL_VIENTO_SANDSTONE_WALL = BLOCKS.register("cut_sal_viento_sandstone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(CUT_SAL_VIENTO_SANDSTONE.get()).forceSolidOn()));
    public static final RegistryObject<Block> CRACKED_SAL_VIENTO_SANDSTONE = BLOCKS.register("cracked_sal_viento_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final RegistryObject<Block> CRACKED_SAL_VIENTO_SANDSTONE_STAIRS = BLOCKS.register("cracked_sal_viento_sandstone_stairs", () -> new StairBlock(() -> CRACKED_SAL_VIENTO_SANDSTONE.get().defaultBlockState(), BlockBehaviour.Properties.copy(CRACKED_SAL_VIENTO_SANDSTONE.get())));
    public static final RegistryObject<Block> CRACKED_SAL_VIENTO_SANDSTONE_SLAB = BLOCKS.register("cracked_sal_viento_sandstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> CRACKED_SAL_VIENTO_SANDSTONE_WALL = BLOCKS.register("cracked_sal_viento_sandstone_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(CRACKED_SAL_VIENTO_SANDSTONE.get()).forceSolidOn()));
    public static final RegistryObject<Block> SAL_VIENTO_PILLAR = BLOCKS.register("sal_viento_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    // Mar Viento Series 海风方块
    public static final RegistryObject<Block> MAR_VIENTO_SAND = BLOCKS.register("mar_viento_sand", () -> new SandBlock(0xdbdad3, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
    public static final RegistryObject<Block> MAR_VIENTO_SANDSTONE = BLOCKS.register("mar_viento_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final RegistryObject<Block> CHISELED_MAR_VIENTO_SANDSTONE = BLOCKS.register("chiseled_mar_viento_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final RegistryObject<Block> SMOOTH_MAR_VIENTO_SANDSTONE = BLOCKS.register("smooth_mar_viento_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(2.0F, 6.0F)));
    public static final RegistryObject<Block> CUT_MAR_VIENTO_SANDSTONE = BLOCKS.register("cut_mar_viento_sandstone", () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));
    public static final RegistryObject<Block> MAR_VIENTO_PILLAR = BLOCKS.register("mar_viento_pillar", () -> new RotatedPillarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().strength(0.8F)));

    // Sea Log Series 海原木系列
    public static final RegistryObject<Block> SEA_LOG = BLOCKS.register("sea_log",
            () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));
    public static final RegistryObject<Block> SEA_PLANKS = BLOCKS.register("sea_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final RegistryObject<Block> SEA_LOG_LEAVES = BLOCKS.register("sea_log_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)));
}
