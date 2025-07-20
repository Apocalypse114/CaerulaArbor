
package com.apocalypse.caerulaarbor.item.relic.rare;

import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.item.relic.RelicItem;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KingsNewLance extends RelicItem {
    public KingsNewLance() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.kings_new_lance.description_0"));
        list.add(Component.translatable("item.caerula_arbor.kings_new_lance.description_1"));
    }

    @Override
    public @Nullable Relic getRelic() {
        return Relic.KING_SPEAR;
    }

    @Override
    public @NotNull SoundEvent getGainSound() {
        return SoundEvents.TOTEM_USE;
    }

    @Override
    public @NotNull ParticleOptions getGainParticle() {
        return ParticleTypes.ENCHANTED_HIT;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        super.useOn(context);

        var entity = context.getPlayer();
        if (entity == null) return InteractionResult.PASS;

        var world = context.getLevel();
        var pos = context.getClickedPos();

        BlockState blockstate = context.getLevel().getBlockState(pos);
        ItemStack itemstack = context.getItemInHand();
        if (blockstate.getBlock() == Blocks.DEEPSLATE_BRICK_SLAB) {
            world.setBlock(pos, ModBlocks.BLOCK_SPEAR.get().defaultBlockState(), 3);

            Direction direction = entity.getDirection().getOpposite();
            BlockState _bs = world.getBlockState(pos);

            world.setBlock(pos, _bs.setValue(BlockStateProperties.FACING, direction), 3);

            itemstack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
