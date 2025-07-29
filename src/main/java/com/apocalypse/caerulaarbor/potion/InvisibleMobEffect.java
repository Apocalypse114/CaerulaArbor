package com.apocalypse.caerulaarbor.potion;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;

import java.util.function.Consumer;

public abstract class InvisibleMobEffect extends MobEffect {
    private static final IClientMobEffectExtensions INVISIBLE_EXTENSIONS = new IClientMobEffectExtensions (){
        @Override
        public boolean isVisibleInGui(MobEffectInstance instance) {
            return false;
        }

        @Override
        public boolean isVisibleInInventory(MobEffectInstance instance) {
            return false;
        }
    };

    protected InvisibleMobEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void initializeClient(Consumer<IClientMobEffectExtensions> consumer) {
        consumer.accept(INVISIBLE_EXTENSIONS);
    }
}
