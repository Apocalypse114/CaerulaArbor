package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PalsyMobEffect extends MobEffect {
    public PalsyMobEffect() {
        super(MobEffectCategory.HARMFUL, -1);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }
}
