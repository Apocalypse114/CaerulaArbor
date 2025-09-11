package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import java.util.function.Supplier;

/**
 * 稳定的全局战利品修饰器，用于替代 ApplyBonusCountMixin 的功能
 * 当玩家 light 能力值低于 50 时，降低时运效果
 * 这样避免了对 Mixin 机制的依赖，提供更稳定的 Mod 运行体验
 */
public class EnhancedFortuneModifier extends LootModifier {
    
    public static final Supplier<Codec<EnhancedFortuneModifier>> CODEC = Suppliers
            .memoize(() -> RecordCodecBuilder.create(instance -> codecStart(instance).apply(instance, EnhancedFortuneModifier::new)));

    protected EnhancedFortuneModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // 检查是否涉及玩家挖掘且工具带有时运附魔
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        
        if (entity instanceof Player player && tool != null) {
            int fortuneLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, tool);
            
            if (fortuneLevel > 0) {
                var cap = ModCapabilities.getPlayerVariables(player);
                
                // 当玩家 light 能力值低于 50 时，减少掉落物品数量（模拟时运等级减少）
                if (cap != null && cap.light < 50) {
                    // 计算时运惩罚倍率 (light 越低，惩罚越重)
                    double penalty = 0.3 + (cap.light / 50.0) * 0.4; // 30%-70% 的掉落率
                    
                    // 应用惩罚：移除部分掉落物品
                    ObjectArrayList<ItemStack> adjustedLoot = new ObjectArrayList<>();
                    for (ItemStack stack : generatedLoot) {
                        if (stack.isEmpty()) continue;
                        
                        int newCount = Math.max(1, (int) (stack.getCount() * penalty));
                        if (newCount != stack.getCount()) {
                            ItemStack adjusted = stack.copy();
                            adjusted.setCount(newCount);
                            adjustedLoot.add(adjusted);
                        } else {
                            adjustedLoot.add(stack);
                        }
                    }
                    return adjustedLoot;
                }
            }
        }
        
        // 不影响的情况下，直接返回原掉落列表
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}