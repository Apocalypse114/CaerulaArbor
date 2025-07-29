package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.block.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CaerulaArborMod.MODID);

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
	public static final RegistryObject<Block> SEA_TRAIL_INIT = BLOCKS.register("sea_trail_init", SeaTrailInitBlock::new);
	public static final RegistryObject<Block> SEA_TRAIL_GROWING = BLOCKS.register("sea_trail_growing", SeaTrailGrowingBlock::new);
	public static final RegistryObject<Block> SEA_TRAIL_GROWN = BLOCKS.register("sea_trail_grown", SeaTrailGrownBlock::new);
	public static final RegistryObject<Block> BOMB_TRAILER = BLOCKS.register("bomb_trailer", BombTrailerBlock::new);
	public static final RegistryObject<Block> OCEAN_GLASS = BLOCKS.register("ocean_glass", OceanGlassBlock::new);
	public static final RegistryObject<Block> OCEAN_GLASSPANE = BLOCKS.register("ocean_glasspane", OceanGlasspaneBlock::new);
	public static final RegistryObject<Block> CHITIN_BLOCK = BLOCKS.register("chitin_block", ChitinBlockBlock::new);
	public static final RegistryObject<Block> BLOCK_KETTLE = BLOCKS.register("block_kettle", BlockKettleBlock::new);
	public static final RegistryObject<Block> OCEAN_FARMLAND = BLOCKS.register("ocean_farmland", OceanFarmlandBlock::new);
	public static final RegistryObject<Block> ALLAY_BLOCK = BLOCKS.register("allay_block", AllayBlockBlock::new);
	public static final RegistryObject<Block> BLOCK_BATBED = BLOCKS.register("block_batbed", BlockBatbedBlock::new);
	public static final RegistryObject<Block> BATBED_UPPER = BLOCKS.register("batbed_upper", BatbedUpperBlock::new);
	public static final RegistryObject<Block> TRAIL_CAKE = BLOCKS.register("trail_cake", TrailCakeBlock::new);
	public static final RegistryObject<Block> CARAMEL_CAKE = BLOCKS.register("caramel_cake", CaramelCakeBlock::new);
	public static final RegistryObject<Block> OCEAN_CRYSTAL_BLOCK = BLOCKS.register("ocean_crystal_block", OceanCrystalBlockBlock::new);
	public static final RegistryObject<Block> COMPLEX_CHITIN_BLOCK = BLOCKS.register("complex_chitin_block", ComplexChitinBlockBlock::new);
	public static final RegistryObject<Block> TIDE_OBSERVATION = BLOCKS.register("tide_observation", TideObservationBlock::new);
	public static final RegistryObject<Block> ANCHOR_LOWER = BLOCKS.register("anchor_lower", AnchorLowerBlock::new);
	public static final RegistryObject<Block> ANCHOR_MEDIUM = BLOCKS.register("anchor_medium", AnchorMediumBlock::new);
	public static final RegistryObject<Block> ANCHOR_UPPER = BLOCKS.register("anchor_upper", AnchorUpperBlock::new);
	public static final RegistryObject<Block> BLOCK_RECORDER = BLOCKS.register("block_recorder", BlockRecorderBlock::new);
	public static final RegistryObject<Block> SEA_TRAIL_SOLID = BLOCKS.register("sea_trail_solid", SeaTrailSolidBlock::new);
	public static final RegistryObject<Block> TRAIL_BRICK = BLOCKS.register("trail_brick", TrailBrickBlock::new);
	public static final RegistryObject<Block> TRAIL_SLAB = BLOCKS.register("trail_slab", TrailSlabBlock::new);
	public static final RegistryObject<Block> TRAIL_STAIR = BLOCKS.register("trail_stair", TrailStairBlock::new);
	public static final RegistryObject<Block> TRAIL_BUTTON = BLOCKS.register("trail_button", TrailButtonBlock::new);
	public static final RegistryObject<Block> TRAIL_PRESSURE_PLATE = BLOCKS.register("trail_pressure_plate", TrailPressurePlateBlock::new);
	public static final RegistryObject<Block> TRAIL_TILE = BLOCKS.register("trail_tile", TrailTileBlock::new);
}
