
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.init.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class CannedBoiledWaterItem extends Item {
    public CannedBoiledWaterItem() {
        super(new Item.Properties().durability(1200).rarity(Rarity.COMMON));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return -1;
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack stack) {
        return 40;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        entity.startUsingItem(hand);
        return ar;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity entity) {
        stack.shrink(1);
        if (entity instanceof Player player) {
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.EMPTY_CAN.get()));
        }

        for (int i = 0; i < 8; i++) {
            CaerulaArborMod.queueServerWork(i * 10, () -> entity.hurt(new DamageSource(world.registryAccess()
                    .registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(DamageTypes.DROWN)
            ), 8));
        }

        return super.finishUsingItem(stack, world, entity);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);

        stack.setDamageValue(stack.getDamageValue() + 1);
        if (stack.getDamageValue() >= 1199 && entity instanceof Player player) {
            player.getInventory().clearOrCountMatchingItems(p -> ModItems.CANNED_BOILED_WATER.get() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.CANNED_WATER.get()));
        }
    }
}
