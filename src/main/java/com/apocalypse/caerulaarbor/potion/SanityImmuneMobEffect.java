package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class SanityImmuneMobEffect extends InvisibleMobEffect {

    public SanityImmuneMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -3342337);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
