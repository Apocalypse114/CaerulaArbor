package com.apocalypse.caerulaarbor.datagen;

import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.init.ModItems;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Stream;

public class ModEntityLootProvider extends EntityLootSubProvider {

    public ModEntityLootProvider() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        this.add(ModEntities.SHELL_SEA_RUNNER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem.lootTableItem(ModItems.BONE_SHARD.get())
                                        // 从固定1个改为1-3个
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.DEEP_SEA_SLIDER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        // 从固定1个改为1-3个
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                        .setWeight(70)
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_PEDUNCLE.get())
                                        // 从固定1个改为1-3个
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                        .setWeight(30)
                                )
                )
        );
        this.add(ModEntities.SUPER_SLIDER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_PEDUNCLE.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.RIDGE_SEA_SPITTER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_FIBRE.get())
                                        // 从固定1个改为1-3个
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.FLOATING_SEA_DRIFTER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_FIBRE.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.BASIN_SEA_REAPER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_FIBRE.get())
                                        .setWeight(65)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        .setWeight(35)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.BONE_SHARD.get())
                                        .setWeight(25)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_PEDUNCLE.get())
                                        .setWeight(35)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_EYE.get())
                                        .setWeight(20)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.SEA_TRAIL_MOR.get())
                                        .setWeight(80)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.POCKET_SEA_CRAWLER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_CRYSTAL.get())
                                        .setWeight(32)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.PRIMAL_SEA_PIERCER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_FIBRE.get())
                                        .setWeight(65)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        .setWeight(45)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.BONE_SHARD.get())
                                        .setWeight(25)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_PEDUNCLE.get())
                                        .setWeight(35)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_CRYSTAL.get())
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.NETHERSEA_FOUNDER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        .setWeight(50)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_CHITIN.get())
                                        .setWeight(50)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(Items.LAPIS_LAZULI)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.NETHERSEA_PREDATOR.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        .setWeight(40)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_CHITIN.get())
                                        .setWeight(60)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(Items.TUBE_CORAL_FAN)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.NETHERSEA_BRANDGUIDER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        .setWeight(33)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.SEA_TRAIL_MOR.get())
                                        .setWeight(66)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(Items.TUBE_CORAL_FAN)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.NETHERSEA_SPEWER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_CHITIN.get())
                                        .setWeight(55)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_FIBRE.get())
                                        .setWeight(44)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.BONE_SHARD.get())
                                        .setWeight(22)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_PEDUNCLE.get())
                                        .setWeight(32)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.SEA_TRAIL_MOR.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.NETHERSEA_SWARMCALLER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        .setWeight(66)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.SEA_TRAIL_MOR.get())
                                        .setWeight(44)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_FIBRE.get())
                                        .setWeight(44)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(Items.TUBE_CORAL)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.NETHERSEA_REEFBREAKER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_CHITIN.get())
                                        .setWeight(44)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_FIBRE.get())
                                        .setWeight(55)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 5)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_CRYSTAL.get())
                                        .setWeight(44)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(Items.TUBE_CORAL)
                                        .setWeight(50)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.SEA_TRAIL_MOR.get())
                                        .setWeight(30)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(Items.NETHERITE_SCRAP)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.UNICELLULAR_PREDATOR.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem.lootTableItem(ModItems.BROKEN_OCEAN_CELL.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.BONE_SEA_DRIFTER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem.lootTableItem(ModItems.BONE_SHARD.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.OCEAN_STONECUTTER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_CHITIN.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(ConstantValue.exactly(1.0f))
                                .add(LootItem.lootTableItem(Items.STONECUTTER)
                                        .when(LootItemRandomChanceCondition.randomChance(0.05f))
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.RETCHING_BROODMOTHER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_FIBRE.get())
                                        .setWeight(66)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        .setWeight(44)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.REAL_EGG.get())
                                        .setWeight(11)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, 1)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.BALEFUL_BROODLING.get(), LootTable.lootTable());
        this.add(ModEntities.SKIMMING_SEA_DRIFTER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 2))
                                .setBonusRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_FIBRE.get())
                                        .setWeight(44)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        .setWeight(44)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_CRYSTAL.get())
                                        .setWeight(33)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(ModItems.BONE_SHARD.get())
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.PATH_SHAPER.get(),
                LootTable.lootTable().withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(1, 3))
                                .setBonusRolls(UniformGenerator.between(0, 2))
                                .add(LootItem.lootTableItem(ModItems.OCEAN_PHLOEM.get())
                                        .setWeight(55)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(5, 8)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_FIBRE.get())
                                        .setWeight(55)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(4, 9)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(ModItems.OCEAN_CRYSTAL.get())
                                        .setWeight(33)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                ).withPool(
                        LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                                .add(LootItem.lootTableItem(Items.HEART_OF_THE_SEA)
                                        .setWeight(10)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(Items.PRISMARINE_CRYSTALS)
                                        .setWeight(50)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                ).add(LootItem.lootTableItem(Items.TUBE_CORAL)
                                        .setWeight(40)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0f)))
                                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
                                )
                )
        );
        this.add(ModEntities.PATHSHAPER_FRACTAL.get(), LootTable.lootTable());

        // TODO 补齐战利品表
        this.add(ModEntities.MULTICELLULAR_HERALD.get(), LootTable.lootTable());
        this.add(ModEntities.MATROCELLULAR_NURSE.get(), LootTable.lootTable());
        this.add(ModEntities.EXOCELLULAR_DEPOSITER.get(), LootTable.lootTable());
        this.add(ModEntities.DIVICELLULAR_HOARDER.get(), LootTable.lootTable());
        this.add(ModEntities.TOXOCELLULAR_DRIFTER.get(), LootTable.lootTable());
        // 新增：为 THE_ABANDONED 提供空战利品表，避免数据生成校验缺失
        this.add(ModEntities.THE_ABANDONED.get(), LootTable.lootTable());
        // 补回：QUINTUS 为生物实体，需要战利品表
        this.add(ModEntities.QUINTUS.get(), LootTable.lootTable());
        // 移除对各类弹体/特效实体（不应有实体战利品表）的添加：ABANDONED_SHOOT、FISH_SHOOT、FISH_SPLASH、FAKERGG_SHOOT、FLEEFISH_BULLET
        // this.add(ModEntities.ABANDONED_SHOOT.get(), LootTable.lootTable());
        // this.add(ModEntities.FISH_SHOOT.get(), LootTable.lootTable());
        // this.add(ModEntities.FISH_SPLASH.get(), LootTable.lootTable());
        // this.add(ModEntities.FAKERGG_SHOOT.get(), LootTable.lootTable());
        // this.add(ModEntities.FLEEFISH_BULLET.get(), LootTable.lootTable());
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {
        // 仅返回“需要实体战利品表”的生物实体，过滤掉 MobCategory.MISC（弹体、特效、载具等）
        return ModEntities.ENTITY_TYPES.getEntries().stream()
                .map(RegistryObject::get)
                .filter(type -> type.getCategory() != net.minecraft.world.entity.MobCategory.MISC);
    }
}
