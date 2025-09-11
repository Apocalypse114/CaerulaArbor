package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.world.level.levelgen.feature.SaltyDesertBeachShaperFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.FEATURES, CaerulaArborMod.MODID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SALTYDESERT_BEACH_SHAPER =
            REGISTRY.register("saltydesert_beach_shaper", () -> new SaltyDesertBeachShaperFeature(NoneFeatureConfiguration.CODEC));
}