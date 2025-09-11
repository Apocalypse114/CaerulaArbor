package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.entity.*;
import com.apocalypse.caerulaarbor.entity.bullets.FakerggShootEntity;
import com.apocalypse.caerulaarbor.entity.bullets.FishShootEntity;
import com.apocalypse.caerulaarbor.entity.bullets.FishSplashEntity;
import com.apocalypse.caerulaarbor.entity.bullets.FleefishBulletEntity;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.DungeonHooks;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CaerulaArborMod.MODID);

    // Living Entities
    public static final RegistryObject<EntityType<ShellSeaRunnerEntity>> SHELL_SEA_RUNNER = register("shell_sea_runner",
            EntityType.Builder.<ShellSeaRunnerEntity>of(ShellSeaRunnerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(ShellSeaRunnerEntity::new)
                    .sized(0.4f, 0.5f));
    public static final RegistryObject<EntityType<DeepSeaSliderEntity>> DEEP_SEA_SLIDER = register("deep_sea_slider",
            EntityType.Builder.<DeepSeaSliderEntity>of(DeepSeaSliderEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(DeepSeaSliderEntity::new)
                    .sized(0.5f, 0.8f));
    public static final RegistryObject<EntityType<SuperSliderEntity>> SUPER_SLIDER = register("super_slider",
            EntityType.Builder.<SuperSliderEntity>of(SuperSliderEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(SuperSliderEntity::new)
                    .sized(1.8f, 4.8f));
    public static final RegistryObject<EntityType<RidgeSeaSpitterEntity>> RIDGE_SEA_SPITTER = register("ridge_sea_spitter",
            EntityType.Builder.<RidgeSeaSpitterEntity>of(RidgeSeaSpitterEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(RidgeSeaSpitterEntity::new)
                    .sized(0.6f, 1.2f));
    public static final RegistryObject<EntityType<FloatingSeaDrifterEntity>> FLOATING_SEA_DRIFTER = register("floating_sea_drifter",
            EntityType.Builder.<FloatingSeaDrifterEntity>of(FloatingSeaDrifterEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(FloatingSeaDrifterEntity::new)
                    .sized(0.6f, 0.9f));
    public static final RegistryObject<EntityType<BasinSeaReaperEntity>> BASIN_SEA_REAPER = register("basin_sea_reaper",
            EntityType.Builder.<BasinSeaReaperEntity>of(BasinSeaReaperEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(BasinSeaReaperEntity::new)
                    .sized(1.2f, 2.8f));
    public static final RegistryObject<EntityType<PocketSeaCrawlerEntity>> POCKET_SEA_CRAWLER = register("pocket_sea_crawler",
            EntityType.Builder.<PocketSeaCrawlerEntity>of(PocketSeaCrawlerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(PocketSeaCrawlerEntity::new)
                    .sized(0.8f, 1.5f));
    public static final RegistryObject<EntityType<PrimalSeaPiercerEntity>> PRIMAL_SEA_PIERCER = register("primal_sea_piercer",
            EntityType.Builder.<PrimalSeaPiercerEntity>of(PrimalSeaPiercerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(PrimalSeaPiercerEntity::new)
                    .sized(0.9f, 2.7f));
    public static final RegistryObject<EntityType<NetherseaFounderEntity>> NETHERSEA_FOUNDER = register("nethersea_founder",
            EntityType.Builder.<NetherseaFounderEntity>of(NetherseaFounderEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(NetherseaFounderEntity::new)
                    .sized(0.7f, 1.5f));
    public static final RegistryObject<EntityType<NetherseaPredatorEntity>> NETHERSEA_PREDATOR = register("nethersea_predator",
            EntityType.Builder.<NetherseaPredatorEntity>of(NetherseaPredatorEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(NetherseaPredatorEntity::new)
                    .sized(0.6f, 1.4f));
    public static final RegistryObject<EntityType<NetherseaBrandguiderEntity>> NETHERSEA_BRANDGUIDER = register("nethersea_brandguider",
            EntityType.Builder.<NetherseaBrandguiderEntity>of(NetherseaBrandguiderEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(NetherseaBrandguiderEntity::new)
                    .sized(1f, 2.5f));
    public static final RegistryObject<EntityType<NetherseaSpewerEntity>> NETHERSEA_SPEWER = register("nethersea_spewer",
            EntityType.Builder.<NetherseaSpewerEntity>of(NetherseaSpewerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(NetherseaSpewerEntity::new)
                    .sized(0.6f, 1.1f));
    public static final RegistryObject<EntityType<NetherseaSwarmcallerEntity>> NETHERSEA_SWARMCALLER = register("nethersea_swarmcaller",
            EntityType.Builder.<NetherseaSwarmcallerEntity>of(NetherseaSwarmcallerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(NetherseaSwarmcallerEntity::new)
                    .sized(0.8f, 1.5f));
    public static final RegistryObject<EntityType<NetherseaReefbreakerEntity>> NETHERSEA_REEFBREAKER = register("nethersea_reefbreaker",
            EntityType.Builder.<NetherseaReefbreakerEntity>of(NetherseaReefbreakerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(NetherseaReefbreakerEntity::new)
                    .sized(0.7f, 1.8f));
    public static final RegistryObject<EntityType<UnicellularPredatorEntity>> UNICELLULAR_PREDATOR = register("unicellular_predator",
            EntityType.Builder.<UnicellularPredatorEntity>of(UnicellularPredatorEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(32).setUpdateInterval(3).setCustomClientFactory(UnicellularPredatorEntity::new)
                    .sized(0.5f, 0.5f));
    public static final RegistryObject<EntityType<BoneSeaDrifterEntity>> BONE_SEA_DRIFTER = register("bone_sea_drifter",
            EntityType.Builder.<BoneSeaDrifterEntity>of(BoneSeaDrifterEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(24).setUpdateInterval(3).setCustomClientFactory(BoneSeaDrifterEntity::new)
                    .sized(0.7f, 0.7f));
    public static final RegistryObject<EntityType<OceanStonecutterEntity>> OCEAN_STONECUTTER = register("ocean_stonecutter",
            EntityType.Builder.<OceanStonecutterEntity>of(OceanStonecutterEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(OceanStonecutterEntity::new)
                    .sized(0.6f, 0.6f));
    public static final RegistryObject<EntityType<RetchingBroodmotherEntity>> RETCHING_BROODMOTHER = register("retching_broodmother",
            EntityType.Builder.<RetchingBroodmotherEntity>of(RetchingBroodmotherEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(RetchingBroodmotherEntity::new)
                    .sized(0.7f, 1.1f));
    public static final RegistryObject<EntityType<BalefulBroodlingEntity>> BALEFUL_BROODLING = register("baleful_broodling",
            EntityType.Builder.<BalefulBroodlingEntity>of(BalefulBroodlingEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(BalefulBroodlingEntity::new)
                    .sized(0.6f, 0.6f));
    public static final RegistryObject<EntityType<SkimmingSeaDrifterEntity>> SKIMMING_SEA_DRIFTER = register("skimming_sea_drifter",
            EntityType.Builder.<SkimmingSeaDrifterEntity>of(SkimmingSeaDrifterEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(SkimmingSeaDrifterEntity::new)
                    .sized(0.8f, 1.1f));
    public static final RegistryObject<EntityType<PathShaperEntity>> PATH_SHAPER = register("path_shaper",
            EntityType.Builder.<PathShaperEntity>of(PathShaperEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(PathShaperEntity::new)
                    .sized(1.8f, 4f));
    public static final RegistryObject<EntityType<PathshaperFractalEntity>> PATHSHAPER_FRACTAL = register("pathshaper_fractal",
            EntityType.Builder.<PathshaperFractalEntity>of(PathshaperFractalEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(PathshaperFractalEntity::new)
                    .sized(0.7f, 1.5f));
    public static final RegistryObject<EntityType<MulticellularHeraldEntity>> MULTICELLULAR_HERALD = register("multicellular_herald",
            EntityType.Builder.<MulticellularHeraldEntity>of(MulticellularHeraldEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(MulticellularHeraldEntity::new)
                    .sized(0.6f, 1.7f));
    public static final RegistryObject<EntityType<MatrocellularNurseEntity>> MATROCELLULAR_NURSE = register("matrocellular_nurse",
            EntityType.Builder.<MatrocellularNurseEntity>of(MatrocellularNurseEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(MatrocellularNurseEntity::new)
                    .sized(0.8f, 1.1f));
    public static final RegistryObject<EntityType<ExocellularDepositerEntity>> EXOCELLULAR_DEPOSITER = register("exocellular_depositer",
            EntityType.Builder.<ExocellularDepositerEntity>of(ExocellularDepositerEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(22).setUpdateInterval(3).setCustomClientFactory(ExocellularDepositerEntity::new)
                    .sized(0.625f, 1f));
    public static final RegistryObject<EntityType<DivicellularHoarderEntity>> DIVICELLULAR_HOARDER = register("divicellular_hoarder",
            EntityType.Builder.<DivicellularHoarderEntity>of(DivicellularHoarderEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(DivicellularHoarderEntity::new)
                    .sized(0.5f, 1f));
    public static final RegistryObject<EntityType<ToxocellularDrifterEntity>> TOXOCELLULAR_DRIFTER = register("toxocellular_drifter",
            EntityType.Builder.<ToxocellularDrifterEntity>of(ToxocellularDrifterEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(ToxocellularDrifterEntity::new)
                    .sized(0.6f, 1.5f));
    public static final RegistryObject<EntityType<TheAbandonedEntity>> THE_ABANDONED = register("the_abandoned",
            EntityType.Builder.<TheAbandonedEntity>of(TheAbandonedEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(TheAbandonedEntity::new)
                    .sized(0.7f, 2.25f));
    public static final RegistryObject<EntityType<QuintusEntity>> QUINTUS = register("quintus",
            EntityType.Builder.<QuintusEntity>of(QuintusEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(QuintusEntity::new)
                    .sized(7f, 11f));
    public static final RegistryObject<EntityType<FilialGenerationEntity>> FILIAL_GENERATION = register("filial_generation",
            EntityType.Builder.<FilialGenerationEntity>of(FilialGenerationEntity::new, MobCategory.MONSTER).setShouldReceiveVelocityUpdates(true).setTrackingRange(16).setUpdateInterval(3).setCustomClientFactory(FilialGenerationEntity::new)
                    .sized(0.5f, 0.5f));

    // Projectiles
    public static final RegistryObject<EntityType<FishShootEntity>> FISH_SHOOT = register("fish_shoot",
            EntityType.Builder.<FishShootEntity>of(FishShootEntity::new, MobCategory.MISC).setCustomClientFactory(FishShootEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.3f, 0.3f));
    public static final RegistryObject<EntityType<FishSplashEntity>> FISH_SPLASH = register("fish_splash",
            EntityType.Builder.<FishSplashEntity>of(FishSplashEntity::new, MobCategory.MISC).setCustomClientFactory(FishSplashEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.3f, 0.3f));
    public static final RegistryObject<EntityType<FakerggShootEntity>> FAKERGG_SHOOT = register("fakergg_shoot",
            EntityType.Builder.<FakerggShootEntity>of(FakerggShootEntity::new, MobCategory.MISC).setCustomClientFactory(FakerggShootEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.4f, 0.4f));
    public static final RegistryObject<EntityType<FleefishBulletEntity>> FLEEFISH_BULLET = register("fleefish_bullet",
            EntityType.Builder.<FleefishBulletEntity>of(FleefishBulletEntity::new, MobCategory.MISC).setCustomClientFactory(FleefishBulletEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.4f, 0.4f));
    public static final RegistryObject<EntityType<AbandonedShootEntity>> ABANDONED_SHOOT = register("abandoned_shoot",
            EntityType.Builder.<AbandonedShootEntity>of(AbandonedShootEntity::new, MobCategory.MISC).setCustomClientFactory(AbandonedShootEntity::new).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(1).sized(0.3f, 0.3f));

    private static <T extends Entity> RegistryObject<EntityType<T>> register(String name, EntityType.Builder<T> entityTypeBuilder) {
        return ENTITY_TYPES.register(name, () -> entityTypeBuilder.build(name));
    }

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            DungeonHooks.addDungeonMob(ModEntities.DEEP_SEA_SLIDER.get(), 25);
            DungeonHooks.addDungeonMob(ModEntities.NETHERSEA_FOUNDER.get(), 25);
            DungeonHooks.addDungeonMob(ModEntities.NETHERSEA_SPEWER.get(), 25);
            DungeonHooks.addDungeonMob(ModEntities.NETHERSEA_SWARMCALLER.get(), 25);
            DungeonHooks.addDungeonMob(ModEntities.SKIMMING_SEA_DRIFTER.get(), 25);
        });
    }

    @SubscribeEvent
    public static void onRegisterSpawnPlacement(SpawnPlacementRegisterEvent event) {
        // Weather-restricted ground monsters
        registerWeatherRestrictedGroundMonster(ModEntities.SHELL_SEA_RUNNER.get(), event);
        registerWeatherRestrictedGroundMonster(ModEntities.DEEP_SEA_SLIDER.get(), event);
        registerWeatherRestrictedGroundMonster(ModEntities.RIDGE_SEA_SPITTER.get(), event);

        // Others keep original rules
        registerBasicSeaMonster(ModEntities.FLOATING_SEA_DRIFTER.get(), event);
        registerBasicSeaMonster(ModEntities.BASIN_SEA_REAPER.get(), event);
        registerBasicSeaMonster(ModEntities.POCKET_SEA_CRAWLER.get(), event);
        registerBasicSeaMonster(ModEntities.PRIMAL_SEA_PIERCER.get(), event);
        registerBasicSeaMonster(ModEntities.NETHERSEA_FOUNDER.get(), event);
        registerBasicSeaMonster(ModEntities.NETHERSEA_PREDATOR.get(), event);
        registerBasicSeaMonster(ModEntities.NETHERSEA_BRANDGUIDER.get(), event);
        registerBasicSeaMonster(ModEntities.NETHERSEA_SPEWER.get(), event);
        registerBasicSeaMonster(ModEntities.NETHERSEA_SWARMCALLER.get(), event);
        registerBasicSeaMonster(ModEntities.NETHERSEA_REEFBREAKER.get(), event);
        registerWaterSeaMonster(ModEntities.UNICELLULAR_PREDATOR.get(), event);
        registerWaterSeaMonster(ModEntities.BONE_SEA_DRIFTER.get(), event);
        registerBasicSeaMonster(ModEntities.OCEAN_STONECUTTER.get(), event);
        registerBasicSeaMonster(ModEntities.RETCHING_BROODMOTHER.get(), event);
        registerBasicSeaMonster(ModEntities.SKIMMING_SEA_DRIFTER.get(), event);
        registerBasicSeaMonster(ModEntities.PATHSHAPER_FRACTAL.get(), event);
        registerWaterSeaMonster(ModEntities.EXOCELLULAR_DEPOSITER.get(), event);
        registerWaterSeaMonster(ModEntities.DIVICELLULAR_HOARDER.get(), event);
        registerWaterSeaMonster(ModEntities.TOXOCELLULAR_DRIFTER.get(), event);
        registerWaterSeaMonster(ModEntities.MULTICELLULAR_HERALD.get(), event);
        registerWaterSeaMonster(ModEntities.MATROCELLULAR_NURSE.get(), event);
    }

    public static <T extends Mob> void registerBasicSeaMonster(EntityType<T> type, SpawnPlacementRegisterEvent event) {
        event.register(type, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, reason, pos, random) -> (world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Mob.checkMobSpawnRules(entityType, world, reason, pos, random)),
                SpawnPlacementRegisterEvent.Operation.OR);
    }

    // New: biome- and weather-aware ground spawn rule
    public static <T extends Mob> void registerWeatherRestrictedGroundMonster(EntityType<T> type, SpawnPlacementRegisterEvent event) {
        event.register(type, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, reason, pos, random) -> {
                    if (world.getDifficulty() == Difficulty.PEACEFUL) return false;

                    Holder<Biome> biome = world.getBiome(pos);
                    boolean isMushroom = biome.is(Biomes.MUSHROOM_FIELDS);
                    boolean inOcean = biome.is(BiomeTags.IS_OCEAN);
                    boolean inBeach = biome.is(BiomeTags.IS_BEACH);
                    boolean inStonyShore = biome.is(Biomes.STONY_SHORE);
                    boolean inBaseBiomes = (inOcean || inBeach || inStonyShore) && !isMushroom;

                    boolean inPlains = biome.is(Biomes.PLAINS) || biome.is(Biomes.SUNFLOWER_PLAINS);

                    // Only allow in specified biomes
                    if (!(inBaseBiomes || inPlains)) return false;

                    // Keep vanilla placement checks (solid ground, space, etc.)
                    if (!Mob.checkMobSpawnRules(entityType, world, reason, pos, random)) return false;

                    // Monsters should still respect darkness
                    if (!Monster.isDarkEnoughToSpawn(world, pos, random)) return false;

                    // Plains variants only spawn during rain/thunder with chance control
                    if (inPlains) {
                        boolean thundering = world.getLevelData().isThundering();
                        boolean raining = world.getLevelData().isRaining() && biome.value().hasPrecipitation();
                        if (!thundering && !raining) return false;
                        float chance = thundering ? 1.0f : 0.5f;
                        return random.nextFloat() < chance;
                    }

                    // Base biomes: spawn under normal conditions (no weather gating)
                    return true;
                },
                SpawnPlacementRegisterEvent.Operation.OR);
    }

    public static <T extends Mob> void registerWaterSeaMonster(EntityType<T> type, SpawnPlacementRegisterEvent event) {
        event.register(type, SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                (entityType, world, reason, pos, random) -> (world.getBlockState(pos).is(Blocks.WATER) && world.getBlockState(pos.above()).is(Blocks.WATER)),
                SpawnPlacementRegisterEvent.Operation.OR);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SHELL_SEA_RUNNER.get(), ShellSeaRunnerEntity.createAttributes().build());
        event.put(DEEP_SEA_SLIDER.get(), DeepSeaSliderEntity.createAttributes().build());
        event.put(SUPER_SLIDER.get(), SuperSliderEntity.createAttributes().build());
        event.put(RIDGE_SEA_SPITTER.get(), RidgeSeaSpitterEntity.createAttributes().build());
        event.put(FLOATING_SEA_DRIFTER.get(), FloatingSeaDrifterEntity.createAttributes().build());
        event.put(BASIN_SEA_REAPER.get(), BasinSeaReaperEntity.createAttributes().build());
        event.put(POCKET_SEA_CRAWLER.get(), PocketSeaCrawlerEntity.createAttributes().build());
        event.put(PRIMAL_SEA_PIERCER.get(), PrimalSeaPiercerEntity.createAttributes().build());
        event.put(NETHERSEA_FOUNDER.get(), NetherseaFounderEntity.createAttributes().build());
        event.put(NETHERSEA_PREDATOR.get(), NetherseaPredatorEntity.createAttributes().build());
        event.put(NETHERSEA_BRANDGUIDER.get(), NetherseaBrandguiderEntity.createAttributes().build());
        event.put(NETHERSEA_SPEWER.get(), NetherseaSpewerEntity.createAttributes().build());
        event.put(NETHERSEA_SWARMCALLER.get(), NetherseaSwarmcallerEntity.createAttributes().build());
        event.put(NETHERSEA_REEFBREAKER.get(), NetherseaReefbreakerEntity.createAttributes().build());
        event.put(UNICELLULAR_PREDATOR.get(), UnicellularPredatorEntity.createAttributes().build());
        event.put(BONE_SEA_DRIFTER.get(), BoneSeaDrifterEntity.createAttributes().build());
        event.put(OCEAN_STONECUTTER.get(), OceanStonecutterEntity.createAttributes().build());
        event.put(RETCHING_BROODMOTHER.get(), RetchingBroodmotherEntity.createAttributes().build());
        event.put(BALEFUL_BROODLING.get(), BalefulBroodlingEntity.createAttributes().build());
        event.put(SKIMMING_SEA_DRIFTER.get(), SkimmingSeaDrifterEntity.createAttributes().build());
        event.put(PATH_SHAPER.get(), PathShaperEntity.createAttributes().build());
        event.put(PATHSHAPER_FRACTAL.get(), PathshaperFractalEntity.createAttributes().build());
        //event.put(DIVICELLULAR_CLONE.get(),DivicellularCloneEntity.createAttributes().build());
        event.put(EXOCELLULAR_DEPOSITER.get(),ExocellularDepositerEntity.createAttributes().build());
        event.put(DIVICELLULAR_HOARDER.get(),DivicellularHoarderEntity.createAttributes().build());
        event.put(TOXOCELLULAR_DRIFTER.get(),ToxocellularDrifterEntity.createAttributes().build());
        event.put(MATROCELLULAR_NURSE.get(), MatrocellularNurseEntity.createAttributes().build());
        event.put(MULTICELLULAR_HERALD.get(), MulticellularHeraldEntity.createAttributes().build());
        event.put(THE_ABANDONED.get(), TheAbandonedEntity.createAttributes().build());
        event.put(QUINTUS.get(), QuintusEntity.createAttributes().build());
        event.put(FILIAL_GENERATION.get(), FilialGenerationEntity.createAttributes().build());
    }
}
