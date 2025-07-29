package com.apocalypse.caerulaarbor.tiers;

import com.apocalypse.caerulaarbor.init.ModItems;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public enum ModItemTier implements Tier {
    CHITIN(3, 1220, 6.0f, 4.0f, 11, () -> Ingredient.of(ModItems.OCEAN_CHITIN.get())),
    COMPLEX_CHITIN(3, 3374, 15f, 8.0f, 18, () -> Ingredient.of(ModItems.COMPLEX_CHITIN.get())),
    ;

    private final int level;
    private final int uses;
    private final float speed;
    private final float damage;
    private final int enchantmentValue;
    private final LazyLoadedValue<Ingredient> repairIngredient;

    ModItemTier(int level, int uses, float speed, float damage, int enchantmentValue, Supplier<Ingredient> repairIngredient) {
        this.level = level;
        this.uses = uses;
        this.speed = speed;
        this.damage = damage;
        this.enchantmentValue = enchantmentValue;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getEnchantmentValue() {
        return enchantmentValue;
    }

    @Override
    public int getUses() {
        return uses;
    }

    @Override
    public float getSpeed() {
        return speed;
    }

    @Override
    public float getAttackDamageBonus() {
        return damage;
    }


    @Override
    public @NotNull Ingredient getRepairIngredient() {
        return repairIngredient.get();
    }
}
