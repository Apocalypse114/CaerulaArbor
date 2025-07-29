
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.procedures.DeductPlayerSanityProcedure;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TrailedGoldenSwordItem extends SwordItem {
    public TrailedGoldenSwordItem() {
        super(new Tier() {
            public int getUses() {
                return 32;
            }

            public float getSpeed() {
                return 12f;
            }

            public float getAttackDamageBonus() {
                return 0f;
            }

            public int getLevel() {
                return 0;
            }

            public int getEnchantmentValue() {
                return 28;
            }

            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of(new ItemStack(Items.GOLD_INGOT));
            }
        }, 3, -2.4f, new Item.Properties());
    }

    @Override
    public boolean hurtEnemy(ItemStack itemstack, LivingEntity entity, @NotNull LivingEntity sourceentity) {
        var world = entity.level();
        double dam = 85 + 20 * itemstack.getEnchantmentLevel(Enchantments.SHARPNESS);
        DeductPlayerSanityProcedure.execute(entity, dam);

        for (int i = 0; i < 5; i++) {
            CaerulaArborMod.queueServerWork(i, () -> {
                if (world instanceof ServerLevel server) {
                    server.sendParticles(
                            ParticleTypes.ELECTRIC_SPARK,
                            entity.getX(),
                            entity.getY() + entity.getBbHeight() * 0.5,
                            entity.getZ(),
                            12, 1.2, 1.5, 1.2, 0.1
                    );
                }
            });
        }

        return super.hurtEnemy(itemstack, entity, sourceentity);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
        ItemStack stack = new ItemStack(this);
        stack.setDamageValue(itemstack.getDamageValue() + 1);
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack itemstack) {
        return false;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.trailed_golden_sword.description_0"));
        list.add(Component.translatable("item.caerula_arbor.trailed_golden_sword.description_1"));
    }
}
