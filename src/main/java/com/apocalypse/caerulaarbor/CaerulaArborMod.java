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

        ModNetwork.register();
    }
}
