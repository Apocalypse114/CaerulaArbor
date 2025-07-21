package com.apocalypse.caerulaarbor.compat.jei;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.init.ModItems;
import com.apocalypse.caerulaarbor.init.ModPotions;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IJeiBrewingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@JeiPlugin
public class CAJeiPlugin implements IModPlugin {

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return CaerulaArborMod.loc("jei_plugin");
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        IVanillaRecipeFactory factory = registration.getJeiHelpers().getVanillaRecipeFactory();
        registration.addRecipes(RecipeTypes.ANVIL, getAnvilRecipes(factory));
        registration.addRecipes(RecipeTypes.BREWING, getBrewingRecipes(factory));
    }

    private static List<IJeiAnvilRecipe> getAnvilRecipes(IVanillaRecipeFactory factory) {
        List<IJeiAnvilRecipe> anvilRecipes = new ArrayList<>();

        anvilRecipes.add(factory.createAnvilRecipe(new ItemStack(Items.IRON_HELMET), List.of(new ItemStack(ModItems.KINGS_CROWN.get())),
                List.of(new ItemStack(ModItems.WEARABLE_CROWN_HELMET.get())), CaerulaArborMod.loc("wearable_crown_helmet")));
        anvilRecipes.add(factory.createAnvilRecipe(new ItemStack(Items.IRON_CHESTPLATE), List.of(new ItemStack(ModItems.KINGS_ARMOUR.get())),
                List.of(new ItemStack(ModItems.WEARABLE_CHEST_CHESTPLATE.get())), CaerulaArborMod.loc("wearable_chestplate")));
        anvilRecipes.add(factory.createAnvilRecipe(new ItemStack(ModItems.SOLO_MUSIC_BOX.get()), List.of(new ItemStack(Items.COPPER_INGOT)),
                List.of(new ItemStack(ModItems.MUSIC_BOX_FIXED.get())), CaerulaArborMod.loc("music_box_fixed")));

        return anvilRecipes;
    }

    private static List<IJeiBrewingRecipe> getBrewingRecipes(IVanillaRecipeFactory factory) {
        List<IJeiBrewingRecipe> brewingRecipes = new ArrayList<>();

        brewingRecipes.add(factory.createBrewingRecipe(List.of(new ItemStack(Items.SWEET_BERRIES)),
                PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                new ItemStack(ModItems.SCREAMING_CHERRY.get()),
                CaerulaArborMod.loc("screaming_cherry")));
        brewingRecipes.add(factory.createBrewingRecipe(List.of(new ItemStack(ModItems.FERMENTED_OCEAN_EYE.get())),
                PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.INST_SANITY.get()),
                CaerulaArborMod.loc("inst_sanity")));
        brewingRecipes.add(factory.createBrewingRecipe(List.of(new ItemStack(ModItems.CARAMEL_MOR.get())),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.INST_SANITY.get()),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.SANITY_CURE.get()),
                CaerulaArborMod.loc("sanity_cure")));
        brewingRecipes.add(factory.createBrewingRecipe(List.of(new ItemStack(Items.GLOWSTONE_DUST)),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.INST_SANITY.get()),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.INST_SANITY_II.get()),
                CaerulaArborMod.loc("inst_sanity_ii")));
        brewingRecipes.add(factory.createBrewingRecipe(List.of(new ItemStack(Items.GLOWSTONE_DUST)),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.SANITY_CURE.get()),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.SANITY_CURE_II.get()),
                CaerulaArborMod.loc("sanity_cure_ii")));

        return brewingRecipes;
    }
}
