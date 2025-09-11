package com.apocalypse.caerulaarbor.mixin;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(net.minecraft.world.level.storage.loot.functions.ApplyBonusCount.class)
public class ApplyBonusCountMixin {

    @Unique
    private static final ThreadLocal<LootContext> caerulaarbor$context = new ThreadLocal<>();

    @Inject(method = "run", at = @At("HEAD"))
    private void caerulaarbor$captureContext(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        caerulaarbor$context.set(context);
    }

    @Redirect(
        method = "run",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getItemEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I"
        )
    )
    private int caerulaarbor$adjustFortuneLevel(Enchantment enchantment, ItemStack stack) {
        // Only intervene for Fortune
        if (enchantment == Enchantments.BLOCK_FORTUNE) {
            LootContext context = caerulaarbor$context.get();
            if (context != null) {
                Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
                if (entity instanceof Player player) {
                    var cap = ModCapabilities.getPlayerVariables(player);
                    if (cap.light < 50) {
                        int current = EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
                        return Math.max(0, current - 1);
                    }
                }
            }
        }
        return EnchantmentHelper.getItemEnchantmentLevel(enchantment, stack);
    }

    @Inject(method = "run", at = @At("RETURN"))
    private void caerulaarbor$clear(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cir) {
        caerulaarbor$context.remove();
    }
}