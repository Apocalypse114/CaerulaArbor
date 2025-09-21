package com.apocalypse.caerulaarbor.datagen;

import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.init.ModItems;
import com.mojang.datafixers.util.Pair;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class ModBlockLootProvider extends BlockLootSubProvider {

    public ModBlockLootProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        this.dropSelf(ModBlocks.ALLAY_BLOCK.get());
        this.dropSelf(ModBlocks.ANCHOR_LOWER.get());
        this.dropSelf(ModBlocks.ANCHOR_MEDIUM.get());
        this.dropSelf(ModBlocks.ANCHOR_UPPER.get());
        this.dropSelf(ModBlocks.BLOCK_BATBED.get());
        this.dropSelf(ModBlocks.BLOCK_CROWN.get());
        this.dropSelf(ModBlocks.BLOCK_CRYSTAL.get());
        this.dropSelf(ModBlocks.BLOCK_EXTENSION.get());
        this.dropSelf(ModBlocks.BLOCK_FATE.get());
        this.dropSelf(ModBlocks.BLOCK_KETTLE.get());
        this.dropSelf(ModBlocks.BLOCK_RECORDER.get());
        this.dropSelf(ModBlocks.BLOCK_SPEAR.get());
        this.dropSelf(ModBlocks.BOMB_TRAILER.get());
        this.dropSelf(ModBlocks.CHITIN_BLOCK.get());
        this.dropSelf(ModBlocks.COMPLEX_CHITIN_BLOCK.get());
        this.dropSelf(ModBlocks.KINGS_ARMOR.get());
        this.dropSelf(ModBlocks.OCEAN_CRYSTAL_BLOCK.get());
        this.dropSelf(ModBlocks.OCEAN_GLASS.get());
        this.dropSelf(ModBlocks.OCEAN_GLASSPANE.get());
        this.dropSelf(ModBlocks.REDSTONE_IRIS.get());
        this.dropSelf(ModBlocks.REDSTONE_IRIS_SEEDING.get());
        this.dropSelf(ModBlocks.TIDE_OBSERVATION_STATION.get());
        this.dropSelf(ModBlocks.NETHERSEA_BLOCK.get());

        this.add(ModBlocks.BATBED_UPPER.get(), noDrop());
        this.add(ModBlocks.CARAMEL_CAKE.get(), noDrop());
        this.add(ModBlocks.TRAIL_CAKE.get(), noDrop());
        this.add(ModBlocks.NETHERSEA_BRAND_INIT.get(), noDrop());
        this.add(ModBlocks.NETHERSEA_BRAND_GROWING.get(), block ->
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SEA_TRAIL_MOR.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                .apply(ApplyExplosionDecay.explosionDecay()))));
        this.add(ModBlocks.NETHERSEA_BRAND_GROWN.get(), block ->
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(ModItems.SEA_TRAIL_MOR.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 3.0F)))
                                .apply(ApplyExplosionDecay.explosionDecay()))));

        this.dropOther(ModBlocks.SCREAMING_CHERRY.get(), ModItems.SCREAMING_CHERRY.get());
        this.dropOther(ModBlocks.EMERGENCY_LIGHT.get(), ModItems.RELIC_CURSE_EMELIGHT.get());
        this.dropOther(ModBlocks.OCEAN_FARMLAND.get(), Items.DIRT);

        // TODO 修改成正确的战利品表
        this.dropSelf(ModBlocks.POOL_OF_PROCREATION.get());
        this.dropSelf(ModBlocks.NOURISHED_POOL_OF_PROCREATION.get());

        this.dropSelf(ModBlocks.NETHERSEA_BRICKS.get());
        this.dropSelf(ModBlocks.NETHERSEA_BRICK_STAIRS.get());
        this.add(ModBlocks.NETHERSEA_BRICK_SLAB.get(), block -> createSlabItemTable(ModBlocks.NETHERSEA_BRICK_SLAB.get()));
        this.dropSelf(ModBlocks.NETHERSEA_BRICK_WALL.get());
        this.dropSelf(ModBlocks.NETHERSEA_TILES.get());
        this.dropSelf(ModBlocks.NETHERSEA_TILE_STAIRS.get());
        this.add(ModBlocks.NETHERSEA_TILE_SLAB.get(), block -> createSlabItemTable(ModBlocks.NETHERSEA_TILE_SLAB.get()));
        this.dropSelf(ModBlocks.NETHERSEA_TILE_WALL.get());
        this.dropSelf(ModBlocks.NETHERSEA_PRESSURE_PLATE.get());
        this.dropSelf(ModBlocks.NETHERSEA_BUTTON.get());

        this.dropSelf(ModBlocks.SAL_VIENTO_SAND.get());
        this.dropSelf(ModBlocks.SAL_VIENTO_SANDSTONE.get());
        this.dropSelf(ModBlocks.SAL_VIENTO_SANDSTONE_STAIRS.get());
        this.add(ModBlocks.SAL_VIENTO_SANDSTONE_SLAB.get(), block -> createSlabItemTable(ModBlocks.SAL_VIENTO_SANDSTONE_SLAB.get()));
        this.dropSelf(ModBlocks.SAL_VIENTO_SANDSTONE_WALL.get());
        this.dropSelf(ModBlocks.CHISELED_SAL_VIENTO_SANDSTONE.get());
        this.dropSelf(ModBlocks.CHISELED_SAL_VIENTO_SANDSTONE_WALL.get());
        this.dropSelf(ModBlocks.SMOOTH_SAL_VIENTO_SANDSTONE.get());
        this.dropSelf(ModBlocks.SMOOTH_SAL_VIENTO_SANDSTONE_STAIRS.get());
        this.add(ModBlocks.SMOOTH_SAL_VIENTO_SANDSTONE_SLAB.get(), block -> createSlabItemTable(ModBlocks.SMOOTH_SAL_VIENTO_SANDSTONE_SLAB.get()));
        this.dropSelf(ModBlocks.SQUARE_PATTERN_SAL_VIENTO_SANDSTONE.get());
        this.dropSelf(ModBlocks.SQUARE_PATTERN_SAL_VIENTO_SANDSTONE_STAIRS.get());
        this.add(ModBlocks.SQUARE_PATTERN_SAL_VIENTO_SANDSTONE_SLAB.get(), block -> createSlabItemTable(ModBlocks.SQUARE_PATTERN_SAL_VIENTO_SANDSTONE_SLAB.get()));
        this.dropSelf(ModBlocks.CUT_SAL_VIENTO_SANDSTONE.get());
        this.dropSelf(ModBlocks.CUT_SAL_VIENTO_SANDSTONE_STAIRS.get());
        this.add(ModBlocks.CUT_SAL_VIENTO_SANDSTONE_SLAB.get(), block -> createSlabItemTable(ModBlocks.CUT_SAL_VIENTO_SANDSTONE_SLAB.get()));
        this.dropSelf(ModBlocks.CUT_SAL_VIENTO_SANDSTONE_WALL.get());
        this.dropSelf(ModBlocks.CRACKED_SAL_VIENTO_SANDSTONE.get());
        this.dropSelf(ModBlocks.CRACKED_SAL_VIENTO_SANDSTONE_STAIRS.get());
        this.add(ModBlocks.CRACKED_SAL_VIENTO_SANDSTONE_SLAB.get(), block -> createSlabItemTable(ModBlocks.CRACKED_SAL_VIENTO_SANDSTONE_SLAB.get()));
        this.dropSelf(ModBlocks.CRACKED_SAL_VIENTO_SANDSTONE_WALL.get());
        this.dropSelf(ModBlocks.SAL_VIENTO_PILLAR.get());

        // Sea Viento Series 海蓝方块
        this.dropSelf(ModBlocks.MAR_VIENTO_SAND.get());
        this.dropSelf(ModBlocks.MAR_VIENTO_SANDSTONE.get());
        this.dropSelf(ModBlocks.CHISELED_MAR_VIENTO_SANDSTONE.get());
        this.dropSelf(ModBlocks.SMOOTH_MAR_VIENTO_SANDSTONE.get());
        this.dropSelf(ModBlocks.CUT_MAR_VIENTO_SANDSTONE.get());
        this.dropSelf(ModBlocks.MAR_VIENTO_PILLAR.get());

        // Sea Log Series 海原木系列方块
        this.dropSelf(ModBlocks.SEA_LOG.get());
        this.dropSelf(ModBlocks.SEA_PLANKS.get());
        this.dropSelf(ModBlocks.SEA_LOG_LEAVES.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    public LootTable.Builder createCopyNBTDrops(Block pBlock, List<Pair<String, String>> paths) {
        var pool = LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(pBlock));
        if (!paths.isEmpty()) {
            var copy = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY);
            for (var path : paths) {
                copy.copy(path.getFirst(), path.getSecond());
            }
            pool.apply(copy);
        }
        return LootTable.lootTable().withPool(this.applyExplosionCondition(pBlock, pool));
    }
}
