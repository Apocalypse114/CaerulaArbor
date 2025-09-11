package com.apocalypse.caerulaarbor;

import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.config.CommonConfig;
import com.apocalypse.caerulaarbor.config.ServerConfig;
import com.apocalypse.caerulaarbor.init.*;
import com.apocalypse.caerulaarbor.network.ModNetwork;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import terrablender.api.Regions;
import com.apocalypse.caerulaarbor.world.level.biome.ModOverworldRegion;
import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.api.RegionType;
import terrablender.api.SurfaceRuleManager;
import net.minecraft.world.level.block.Blocks;

@Mod(CaerulaArborMod.MODID)
public class CaerulaArborMod {

    public static final String MODID = "caerula_arbor";
    public static final Logger LOGGER = LogManager.getLogger(CaerulaArborMod.class);
    public static final String ATTRIBUTE_MODIFIER = "caerula_arbor_attribute_modifier";

    public CaerulaArborMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.init());
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ServerConfig.init());

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModSounds.REGISTRY.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModBlockEntityTypes.BLOCK_ENTITIES.register(bus);

        ModItems.register(bus);
        ModEntities.ENTITY_TYPES.register(bus);
        ModEnchantments.REGISTRY.register(bus);
        ModTabs.REGISTRY.register(bus);

        ModMobEffects.REGISTRY.register(bus);
        ModPotions.REGISTRY.register(bus);

        ModParticleTypes.REGISTRY.register(bus);
        ModVillagers.register(bus);
        ModMenus.REGISTRY.register(bus);
        ModAttributes.REGISTRY.register(bus);
        
        // 注册自定义地形特性（用于 saltydesert 海岸平滑过渡与4格海沙带）
        ModFeatures.REGISTRY.register(bus);

        ModCommandArguments.COMMAND_ARGUMENT_TYPES.register(bus);
        ModLootModifier.LOOT_MODIFIERS.register(bus);

        bus.addListener(this::onCommonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private static final Collection<AbstractMap.SimpleEntry<Runnable, Integer>> workQueue = new ConcurrentLinkedQueue<>();

    public static void queueServerWork(int tick, Runnable action) {
        if (Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER)
            workQueue.add(new AbstractMap.SimpleEntry<>(action, tick));
    }

    @SubscribeEvent
    public void tick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            List<AbstractMap.SimpleEntry<Runnable, Integer>> actions = new ArrayList<>();
            workQueue.forEach(work -> {
                work.setValue(work.getValue() - 1);
                if (work.getValue() == 0)
                    actions.add(work);
            });
            actions.forEach(e -> e.getKey().run());
            workQueue.removeAll(actions);
        }
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MODID, path);
    }

    public void onCommonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                Ingredient.of(ModItems.FERMENTED_OCEAN_EYE.get()), PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.INST_SANITY.get())));
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.INST_SANITY.get())),
                Ingredient.of(ModItems.CARAMEL_MOR.get()), PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.SANITY_CURE.get())));
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                Ingredient.of(Items.SWEET_BERRIES), new ItemStack(ModItems.SCREAMING_CHERRY.get())));
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.INST_SANITY.get())),
                Ingredient.of(Items.GLOWSTONE_DUST), PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.INST_SANITY_II.get())));
        event.enqueueWork(() -> BrewingRecipeRegistry.addRecipe(Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.SANITY_CURE.get())),
                Ingredient.of(Items.GLOWSTONE_DUST), PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.SANITY_CURE_II.get())));
        event.enqueueWork(Relic::onRegisterItem);

        // 注册 TerraBlender 区域以将 saltydesert 注入主世界气候参数
        event.enqueueWork(() -> Regions.register(new ModOverworldRegion()));

        // 为 saltydesert 注入地表规则：SAL-only 基线（BeachShaper 负责沿水改写为 SEA 并加厚）
        event.enqueueWork(() -> {
            SurfaceRules.RuleSource saltyDesertRules = SurfaceRules.ifTrue(
                    SurfaceRules.isBiome(ModOverworldRegion.SALTY_DESERT),
                    SurfaceRules.sequence(
                            // 顶层：SAL 沙
                            SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(ModBlocks.SAL_VIENTO_SAND.get().defaultBlockState())),
                            // 里层：SAL 砂岩
                            SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.state(ModBlocks.SAL_VIENTO_SANDSTONE.get().defaultBlockState()))
                    )
            );
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, saltyDesertRules);
        });

        // 为 ocean_forest 注入地表规则：表层为海洋草方块，里层为泥土（标准森林群系风格）
        event.enqueueWork(() -> {
            SurfaceRules.RuleSource oceanForestRules = SurfaceRules.ifTrue(
                    SurfaceRules.isBiome(ModOverworldRegion.OCEAN_FOREST),
                    SurfaceRules.sequence(
                            // 顶层：海洋草方块
                            SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.state(ModBlocks.OCEAN_GRASS_BLOCK.get().defaultBlockState())),
                            // 里层：泥土
                            SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, SurfaceRules.state(Blocks.DIRT.defaultBlockState()))
                    )
            );
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, oceanForestRules);
        });

        ModNetwork.register();
    }
}
