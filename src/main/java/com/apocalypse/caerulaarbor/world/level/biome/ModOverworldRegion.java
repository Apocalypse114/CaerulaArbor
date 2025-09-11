package com.apocalypse.caerulaarbor.world.level.biome;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.core.Registry;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

import static terrablender.api.ParameterUtils.*;

public class ModOverworldRegion extends Region {
    public static final ResourceKey<Biome> SALTY_DESERT = ResourceKey.create(Registries.BIOME, CaerulaArborMod.loc("saltydesert"));
    // 新增：幽海丛林 Biome Key（注册名：ocean_forest）
    public static final ResourceKey<Biome> OCEAN_FOREST = ResourceKey.create(Registries.BIOME, CaerulaArborMod.loc("ocean_forest"));

    public ModOverworldRegion() {
        // Lower weight from 5 -> 2 to ensure vanilla deserts retain prevalence and our biome co-exists
        super(new ResourceLocation(CaerulaArborMod.MODID, "overworld"), RegionType.OVERWORLD, 2);
    }

    // Narrow weirdness slice to further reduce coverage (roughly 25% of full range)
    private static final Climate.Parameter COASTAL_WEIRDNESS = Climate.Parameter.span(-1.0F, -0.5F);

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        // 将 saltydesert 固定在指定气候参数点：HOT/ARID，continentalness=-1.0，erosion=0，weirdness=0，depth=0
        mapper.accept(Pair.of(
                Climate.parameters(
                        Temperature.HOT.parameter(),
                        Humidity.ARID.parameter(),
                        Climate.Parameter.point(-1.0F), // continentalness
                        Climate.Parameter.point(0.0F),  // erosion
                        Climate.Parameter.point(0.0F),  // depth
                        Climate.Parameter.point(0.0F),  // weirdness
                        0.0F
                ),
                SALTY_DESERT
        ));

        // 将 ocean_forest（幽海丛林）注入近海暖湿带：WARM/WET，continentalness≈-0.95，其他维持 0 的基线
        // 后续可根据实机观察微调至一段区间（span）以扩展覆盖范围
        mapper.accept(Pair.of(
                Climate.parameters(
                        Temperature.WARM.parameter(),
                        Humidity.WET.parameter(),
                        Climate.Parameter.point(-0.95F), // continentalness（近海）
                        Climate.Parameter.point(0.0F),   // erosion
                        Climate.Parameter.point(0.0F),   // depth
                        Climate.Parameter.point(0.2F),   // weirdness 略偏正，避免与 saltydesert 重叠
                        0.0F
                ),
                OCEAN_FOREST
        ));
    }

    // 移除旧的带型注入方法，避免产生范围覆盖
    // private void addBand(...) { }
    // private static void add(...) { }
}