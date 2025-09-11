package com.apocalypse.caerulaarbor.procedures;

//Deepseek不懂事写着玩的而且貌似会卡

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import java.util.List;
import java.util.stream.Collectors;

public class EntitySpawnHelper {

    // 定义实体标签的 TagKey（替换 your_mod_id 和 your_tag_name）
    private static final TagKey<EntityType<?>> ENTITY_TAG = TagKey.create(
            Registries.ENTITY_TYPE,
            new ResourceLocation("caerula_arbor", "izumik_discovers")
    );

    /**
     * 从标签中随机选择实体并生成
     *
     * @param level 世界对象（需为 ServerLevel）
     * @param x,y,z 生成位置
     * @return 是否生成成功
     */
    public static boolean spawnRandomEntityFromTag(ServerLevel level, double x, double y,double z) {
        // 1. 获取标签内的实体类型列表
        List<EntityType<?>> entityTypes = getEntitiesInTag(ENTITY_TAG, level);
        if (entityTypes.isEmpty()) return false;

        // 2. 随机选择一个实体类型
        RandomSource random = level.getRandom();
        EntityType<?> selectedType = entityTypes.get(random.nextInt(entityTypes.size()));

        // 3. 生成实体
        return spawnEntity(selectedType, level, x,y,z);
    }

    /**
     * 获取标签包含的所有实体类型
     */
    private static List<EntityType<?>> getEntitiesInTag(TagKey<EntityType<?>> tagKey, ServerLevel level) {
        return level.registryAccess()
                .registryOrThrow(Registries.ENTITY_TYPE) // 获取实体类型注册表
                .getTag(tagKey) // 获取标签
                .stream()
                .flatMap(tag -> tag.stream().map(Holder::value)) // 展开 Holder 到 EntityType
                .collect(Collectors.toList());
    }

    /**
     * 在指定位置生成实体
     */
    private static boolean spawnEntity(EntityType<?> entityType, ServerLevel level, double x, double y,double z) {
        Entity entity = entityType.create(level);
        if (entity == null) return false;

        // 设置位置（中心点坐标）
        entity.moveTo(x,y,z,
                level.getRandom().nextFloat() * 360.0F, // 随机朝向
                0.0F
        );

        // 添加到世界
        level.addFreshEntity(entity);
        return true;
    }
}