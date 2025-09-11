# 2025-09-06
2025-09-06 + 新增：update.md（集中维护更新日志与测试要点）

# 2025-09-08
2025-09-08 + 新增：SEA 替换规则（仅当 Y∈[58,65]，将地表 SAL 一一映射替换为 SEA）
2025-09-08 + 新增：邻水检测与替换映射方法 isNearWater(...)、getSalToSeaReplacement(...)
2025-09-08 + 新增：可侵蚀标签纳入原版砂/砂岩与全部 SAL/SEA 砂、砂岩
2025-09-08 - 移除：旧的“近海/平坦度/推进范围”等生成判定
2025-09-08 - 移除：errodable.json 中与可侵蚀无关的条目

# 2025-09-09
2025-09-09 + 新增：asset_check.py 轻量资源增量校验脚本（读取 update.md 最新“修改文件”列表，仅检查相关资源）
2025-09-09 * 使用方法：在本日期条目下维护“修改文件”清单，运行 `python asset_check.py` 即可增量校验
2025-09-09 * 修改文件：
- asset_check.py
- asset_check_ignore.txt
- CaerulaArbor-remake/src/main/resources/data/caerula_arbor/recipes/ocean_planks_from_ocean_log.json
- CaerulaArbor-remake/src/main/resources/data/minecraft/tags/items/planks.json
- CaerulaArbor-remake/src/main/resources/data/minecraft/tags/items/logs.json
- CaerulaArbor-remake/src/main/resources/data/minecraft/tags/items/logs_that_burn.json
- CaerulaArbor-remake/src/main/resources/data/minecraft/tags/blocks/planks.json
- CaerulaArbor-remake/src/main/resources/data/minecraft/tags/blocks/logs.json
- CaerulaArbor-remake/src/main/resources/data/minecraft/tags/blocks/logs_that_burn.json

# 2025-09-10
2025-09-10 + 新增：生物群系“幽海丛林”（ocean_forest）基础框架
2025-09-10 * 资源键：caerula_arbor:ocean_forest
2025-09-10 * TerraBlender 注入：在 ModOverworldRegion 中映射近海暖湿点位（WARM/WET, continentalness≈-0.95）
2025-09-10 * 标签：加入 data/minecraft/tags/worldgen/biome/is_overworld.json
2025-09-10 * 修改文件：
- CaerulaArbor-remake/src/main/java/com/apocalypse/caerulaarbor/world/level/biome/ModOverworldRegion.java
- CaerulaArbor-remake/src/main/resources/data/caerula_arbor/worldgen/biome/ocean_forest.json
- CaerulaArbor-remake/src/main/resources/data/minecraft/tags/worldgen/biome/is_overworld.json