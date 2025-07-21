package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ArmorBreakingEffect extends MobEffect {

    public ArmorBreakingEffect() {
        super(MobEffectCategory.HARMFUL, -3342337);
        this.addAttributeModifier(Attributes.ARMOR, "b13a9de8-fdc7-3dc6-badd-339eef5041ec", -1, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
