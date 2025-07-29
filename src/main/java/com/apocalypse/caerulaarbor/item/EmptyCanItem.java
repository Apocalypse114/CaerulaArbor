
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class EmptyCanItem extends Item {
    public EmptyCanItem() {
        super(new Item.Properties().stacksTo(64).rarity(Rarity.COMMON));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        ItemStack itemstack = ar.getObject();
        var lookingBlock = world.getFluidState(BlockPos.containing(x + entity.getLookAngle().x, y + entity.getLookAngle().y, z + entity.getLookAngle().z)).createLegacyBlock().getBlock();

        if (Blocks.WATER == lookingBlock) {
            itemstack.shrink(1);
            ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ModItems.CANNED_WATER.get()));

            if (!world.isClientSide()) {
                world.playSound(null, entity.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1, 1, false);
            }

        } else if (Blocks.LAVA == lookingBlock) {
            itemstack.shrink(1);
            ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ModItems.CANNED_LAVA.get()));

            if (!world.isClientSide()) {
                world.playSound(null, entity.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1, 1, false);
            }
        }
        return ar;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        super.useOn(context);
        var player = context.getPlayer();
        if (player == null) return InteractionResult.PASS;

        var world = context.getLevel();
        var pos = context.getClickedPos();
        var direction = context.getClickedFace();
        var targetPos = pos.relative(direction);
        var mainHandItem = context.getItemInHand();

        if (Blocks.WATER == (world.getFluidState(targetPos).createLegacyBlock()).getBlock()) {
            mainHandItem.shrink(1);
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.CANNED_WATER.get()).copyWithCount(1));

            if (!world.isClientSide()) {
                world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1, 1);
            } else {
                world.playLocalSound(player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1, 1, false);
            }
            return InteractionResult.SUCCESS;
        } else if (Blocks.LAVA == (world.getFluidState(targetPos).createLegacyBlock()).getBlock()) {
            mainHandItem.shrink(1);
            ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(ModItems.CANNED_LAVA.get()).copyWithCount(1));

            if (!world.isClientSide()) {
                world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1, 1);
            } else {
                world.playLocalSound(player.blockPosition(), SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1, 1, false);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
