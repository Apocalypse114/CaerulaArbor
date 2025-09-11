package com.apocalypse.caerulaarbor.world.level.levelgen.feature;

import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.init.ModTags;
import com.apocalypse.caerulaarbor.world.level.biome.ModOverworldRegion;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.tags.BiomeTags;

public class SaltyDesertBeachShaperFeature extends Feature<NoneFeatureConfiguration> {

    public SaltyDesertBeachShaperFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        LevelAccessor level = ctx.level();
        BlockPos origin = ctx.origin();
        // 仅在 saltydesert 生物群系内运行
        if (!level.getBiome(origin).is(ModOverworldRegion.SALTY_DESERT)) {
            return false;
        }

        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        int chunkMinX = (origin.getX() >> 4) << 4;
        int chunkMinZ = (origin.getZ() >> 4) << 4;
        int chunkMaxX = chunkMinX + 15;
        int chunkMaxZ = chunkMinZ + 15;

        final int seaLevel = level.getSeaLevel();
        final int minY = level.getMinBuildHeight();
        
        // 基础方块定义
        final BlockState SAL_SAND = ModBlocks.SAL_VIENTO_SAND.get().defaultBlockState();
        final BlockState SEA_SAND = ModBlocks.SEA_VIENTO_SAND.get().defaultBlockState();

        final BlockState SAL_SANDSTONE_BASE = ModBlocks.SAL_VIENTO_SANDSTONE.get().defaultBlockState();
        final BlockState[] SAL_SANDSTONE_OTHERS = new BlockState[] {
                ModBlocks.SMOOTH_SAL_VIENTO_SANDSTONE.get().defaultBlockState(),
                ModBlocks.CUT_SAL_VIENTO_SANDSTONE.get().defaultBlockState(),
                ModBlocks.CRACKED_SAL_VIENTO_SANDSTONE.get().defaultBlockState(),
                ModBlocks.CHISELED_SAL_VIENTO_SANDSTONE.get().defaultBlockState(),
                ModBlocks.SQUARE_PATTERN_SAL_VIENTO_SANDSTONE.get().defaultBlockState()
        };
        final BlockState SEA_SANDSTONE_BASE = ModBlocks.SEA_VIENTO_SANDSTONE.get().defaultBlockState();
        final BlockState[] SEA_SANDSTONE_OTHERS = new BlockState[] {
                ModBlocks.SMOOTH_SEA_VIENTO_SANDSTONE.get().defaultBlockState(),
                ModBlocks.CUT_SEA_VIENTO_SANDSTONE.get().defaultBlockState(),
                ModBlocks.CRACKED_SEA_VIENTO_SANDSTONE.get().defaultBlockState(),
                ModBlocks.CHISELED_SEA_VIENTO_SANDSTONE.get().defaultBlockState(),
                ModBlocks.SQUARE_PATTERN_SEA_VIENTO_SANDSTONE.get().defaultBlockState()
        };

        // 搜索半径与地形检测参数
        final int SEARCH_RADIUS = 6;         // 环形搜索海洋半径
        final int FLATNESS_RADIUS = 3;       // 平坦度检测半径
        final int MAX_HEIGHT_DIFF = 2;       // 最大允许高度差
        final int AREA_CHECK_RADIUS = 4;     // 覆盖率检测半径
        final double MIN_AREA_COVERAGE = 0.6;// 最小覆盖率

        // 厚度与权重（主方块占多数）
        final int SAL_TOP_SAND_LAYERS = 3;   // SAL 顶层沙厚度
        final int SAL_SUB_VARIANT_LAYERS = 3;// SAL 里层砂岩厚度
        final int SEA_TOP_SAND_LAYERS = 2;   // SEA 顶层沙厚度（海滩通常稍薄）
        final int SEA_SUB_VARIANT_LAYERS = 2;// SEA 里层砂岩厚度
        final int BASE_WEIGHT = 8;           // 主砂岩权重
        final int OTHER_WEIGHT = 1;          // 其他变体权重

        // 第一阶段：在整个 saltydesert 内铺设并加厚 SAL（避开水下与水边）
        for (int x = chunkMinX; x <= chunkMaxX; x++) {
            for (int z = chunkMinZ; z <= chunkMaxZ; z++) {
                if (!level.getBiome(new BlockPos(x, seaLevel, z)).is(ModOverworldRegion.SALTY_DESERT)) {
                    continue;
                }
                int surfaceY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x, z) - 1;
                if (surfaceY < minY) continue;

                // 避开海底/水边：要求上方无液体，且周围不邻水
                BlockPos aboveDryCheck = new BlockPos(x, surfaceY + 1, z);
                if (!level.getFluidState(aboveDryCheck).isEmpty()) continue; // 上方有水 => 视为海底或水中，跳过
                if (isNearWater(level, x, surfaceY, z, 1, 1)) continue; // 水边：跳过

                // 顶层：向下铺设 SAL 沙若干层（仅在干燥表面）
                int y = surfaceY;
                for (int i = 0; i < SAL_TOP_SAND_LAYERS && y >= minY; i++) {
                    cursor.set(x, y, z);
                    BlockState st = level.getBlockState(cursor);
                    if (!isTopReplaceable(st)) break;
                    if (st != SAL_SAND) level.setBlock(cursor, SAL_SAND, Block.UPDATE_CLIENTS);
                    y--;
                }

                // 里层：铺设 SAL 砂岩（主变体占多数）
                int placed = 0;
                while (placed < SAL_SUB_VARIANT_LAYERS && y >= minY) {
                    cursor.set(x, y, z);
                    BlockState st = level.getBlockState(cursor);
                    if (!isInnerReplaceable(st)) break;
                    BlockState variant = chooseWeightedVariant(SAL_SANDSTONE_BASE, SAL_SANDSTONE_OTHERS, x, y, z, BASE_WEIGHT, OTHER_WEIGHT);
                    if (st != variant) level.setBlock(cursor, variant, Block.UPDATE_CLIENTS);
                    placed++;
                    y--;
                }
            }
        }

        // 第二阶段：在 y=58-65 范围内，将已存在的 SAL 系列方块替换为对应的 SEA 系列方块
        for (int x = chunkMinX; x <= chunkMaxX; x++) {
            for (int z = chunkMinZ; z <= chunkMaxZ; z++) {
                if (!level.getBiome(new BlockPos(x, seaLevel, z)).is(ModOverworldRegion.SALTY_DESERT)) {
                    continue;
                }
        
                // 在 y=58-65 范围内扫描并替换 SAL -> SEA
                for (int y = 58; y <= 65; y++) {
                    if (y < minY) continue;
                    
                    cursor.set(x, y, z);
                    BlockState currentState = level.getBlockState(cursor);
                    
                    // 检测并替换 SAL 系列方块为对应的 SEA 系列方块
                    BlockState replacement = getSalToSeaReplacement(currentState);
                    if (replacement != null && currentState != replacement) {
                        level.setBlock(cursor, replacement, Block.UPDATE_CLIENTS);
                    }
                }
            }
        }
        return true;
    }

    // 检测指定位置周围是否为平坦区域（避免地势起伏大的地方）
    private static boolean isFlatArea(LevelAccessor level, int centerX, int centerZ, int centerY, int radius, int maxHeightDiff) {
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                if (dx == 0 && dz == 0) continue; // 跳过中心点
                int checkY = level.getHeight(Heightmap.Types.WORLD_SURFACE_WG, centerX + dx, centerZ + dz) - 1;
                if (Math.abs(checkY - centerY) > maxHeightDiff) {
                    return false; // 高度差超过阈值
                }
            }
        }
        return true;
    }
    
    // 覆盖率检测（带早停优化）
    private static boolean isLargeAreaSuitableOptimized(LevelAccessor level, int centerX, int centerZ, int seaLevel, int checkRadius, int searchRadius, double minCoverage) {
        int totalPoints = 0;
        int suitablePoints = 0;
        int needed;
        
        for (int dx = -checkRadius; dx <= checkRadius; dx++) {
            for (int dz = -checkRadius; dz <= checkRadius; dz++) {
                totalPoints++;
                int checkX = centerX + dx;
                int checkZ = centerZ + dz;

                // 生物群系快速过滤：仅在 saltydesert 内计入
                if (!level.getBiome(new BlockPos(checkX, seaLevel, checkZ)).is(ModOverworldRegion.SALTY_DESERT)) {
                    // 仍需计算早停上界
                } else {
                    int od = nearestOceanDistanceAtSeaLevel(level, checkX, checkZ, seaLevel, searchRadius);
                    if (od > 0 && od <= 3) suitablePoints++;
                }

                // 早停：若即使剩余全部满足也无法达到阈值，直接失败
                needed = (int)Math.ceil(minCoverage * totalPoints);
                int remaining = (2 * checkRadius + 1) * (2 * checkRadius + 1) - totalPoints;
                if (suitablePoints + remaining < (int)Math.ceil(minCoverage * ((2 * checkRadius + 1) * (2 * checkRadius + 1)))) {
                    return false;
                }
                // 早停：已达到阈值即可返回成功
                if (suitablePoints >= (int)Math.ceil(minCoverage * ((2 * checkRadius + 1) * (2 * checkRadius + 1)))) {
                    return true;
                }
            }
        }
        double coverage = (double) suitablePoints / ((2 * checkRadius + 1) * (2 * checkRadius + 1));
        return coverage >= minCoverage;
    }

    private static BlockState chooseWeightedVariant(BlockState base, BlockState[] others, int x, int y, int z, int baseWeight, int otherWeight) {
        int h = Mth.murmurHash3Mixer(x * 73428767 ^ z * 912931 ^ (y << 4));
        int total = baseWeight + others.length * otherWeight;
        int r = Math.abs(h) % total;
        if (r < baseWeight) return base;
        int idx = (r - baseWeight) / otherWeight;
        if (idx < 0) idx = 0;
        if (idx >= others.length) idx = others.length - 1;
        return others[idx];
    }

    private static boolean isTopReplaceable(BlockState state) {
        // 顶层：允许替换空气、可替换方块、植被、雪层、松散可侵蚀材料，但避免在水体上方直接放置
        return (state.isAir() || state.canBeReplaced() || state.is(ModTags.Blocks.ERRODABLE)) && state.getFluidState().isEmpty();
    }

    private static boolean isTopReplaceableBaseline(BlockState state) {
        // 基线阶段：允许在“上方可能有水”的情况下替换顶层，用于形成水下盐沙底
        return (state.isAir() || state.canBeReplaced() || state.is(ModTags.Blocks.ERRODABLE));
    }

    private static boolean isInnerReplaceable(BlockState state) {
        // 里层：允许替换沙/土/草/砂岩等松散或可侵蚀材料，避免穿透水体与坚硬结构
        return (state.canBeReplaced() || state.is(ModTags.Blocks.ERRODABLE)) && state.getFluidState().isEmpty();
    }

    // 基于海平面检测最近海洋生物群系的水平距离：按曼哈顿距离环形逐步外扩
    private static int nearestOceanDistanceAtSeaLevel(LevelAccessor level, int x, int z, int seaLevel, int radius) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int d = 0; d <= radius; d++) {
            for (int dx = -d; dx <= d; dx++) {
                int dz1 = d - Math.abs(dx);
                for (int sign = -1; sign <= 1; sign += 2) {
                    int cz = z + dz1 * sign;
                    int cx = x + dx;
                    pos.set(cx, seaLevel, cz);
                    if (level.getBiome(pos).is(BiomeTags.IS_OCEAN)) return d;
                }
            }
        }
        return -1;
    }

    // 新增：检测 (x,z) 周围是否存在实际水体（避免仅因“靠近海洋生物群系”而误判内陆为沿海）
    private static boolean hasAdjacentWater(LevelAccessor level, int x, int z, int seaLevel, int radius) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                // 仅在海平面上下1格内检查，提高鲁棒性
                for (int dy = -1; dy <= 1; dy++) {
                    pos.set(x + dx, seaLevel + dy, z + dz);
                    if (!level.getFluidState(pos).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // 新增：获取 SAL 到 SEA 的方块替换映射
    private static BlockState getSalToSeaReplacement(BlockState salState) {
        // SAL 沙子系列 -> SEA 沙子系列
        if (salState.is(ModBlocks.SAL_VIENTO_SAND.get())) {
            return ModBlocks.SEA_VIENTO_SAND.get().defaultBlockState();
        }
        
        // SAL 砂岩系列 -> SEA 砂岩系列
        if (salState.is(ModBlocks.SAL_VIENTO_SANDSTONE.get())) {
            return ModBlocks.SEA_VIENTO_SANDSTONE.get().defaultBlockState();
        }
        if (salState.is(ModBlocks.SMOOTH_SAL_VIENTO_SANDSTONE.get())) {
            return ModBlocks.SMOOTH_SEA_VIENTO_SANDSTONE.get().defaultBlockState();
        }
        if (salState.is(ModBlocks.CUT_SAL_VIENTO_SANDSTONE.get())) {
            return ModBlocks.CUT_SEA_VIENTO_SANDSTONE.get().defaultBlockState();
        }
        if (salState.is(ModBlocks.CRACKED_SAL_VIENTO_SANDSTONE.get())) {
            return ModBlocks.CRACKED_SEA_VIENTO_SANDSTONE.get().defaultBlockState();
        }
        if (salState.is(ModBlocks.CHISELED_SAL_VIENTO_SANDSTONE.get())) {
            return ModBlocks.CHISELED_SEA_VIENTO_SANDSTONE.get().defaultBlockState();
        }
        if (salState.is(ModBlocks.SQUARE_PATTERN_SAL_VIENTO_SANDSTONE.get())) {
            return ModBlocks.SQUARE_PATTERN_SEA_VIENTO_SANDSTONE.get().defaultBlockState();
        }
        
        return null; // 不是 SAL 系列方块，无需替换
    }

    // 新增：在给定(y±dyRange)高度范围内检测 (x,z) 周围是否存在水体，用于"不影响水边"判定
    private static boolean isNearWater(LevelAccessor level, int x, int y, int z, int radius, int dyRange) {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dz = -radius; dz <= radius; dz++) {
                for (int dy = -dyRange; dy <= dyRange; dy++) {
                    pos.set(x + dx, y + dy, z + dz);
                    if (!level.getFluidState(pos).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}