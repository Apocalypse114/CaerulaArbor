package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {

    public static final DeferredRegister<Potion> REGISTRY = DeferredRegister.create(ForgeRegistries.POTIONS, CaerulaArborMod.MODID);

    public static final RegistryObject<Potion> INST_SANITY = REGISTRY.register("inst_sanity", () -> new Potion(new MobEffectInstance(ModMobEffects.INSTANT_SANITY.get(), 1, 0, false, true)));
    public static final RegistryObject<Potion> SANITY_CURE = REGISTRY.register("sanity_cure", () -> new Potion(new MobEffectInstance(ModMobEffects.SANITY_HEAL.get(), 1, 0, false, true)));
    public static final RegistryObject<Potion> INST_SANITY_II = REGISTRY.register("inst_sanity_ii", () -> new Potion(new MobEffectInstance(ModMobEffects.INSTANT_SANITY.get(), 1, 1, false, true)));
    public static final RegistryObject<Potion> SANITY_CURE_II = REGISTRY.register("sanity_cure_ii", () -> new Potion(new MobEffectInstance(ModMobEffects.SANITY_HEAL.get(), 1, 1, false, true)));
}
