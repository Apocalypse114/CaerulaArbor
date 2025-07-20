
package com.apocalypse.caerulaarbor.item.relic.epic;

import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.item.relic.RelicItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class KingsCrownItem extends RelicItem {
    public KingsCrownItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.kings_crown.description_0").withStyle(ChatFormatting.AQUA));
        list.add(Component.translatable("item.caerula_arbor.kings_crown.description_1").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public @Nullable Relic getRelic() {
        return Relic.KING_CROWN;
    }

    @Override
    public @Nullable ParticleOptions getGainParticle() {
        return ParticleTypes.ENCHANTED_HIT;
    }

    @Override
    public @Nullable SoundEvent getGainSound() {
        return SoundEvents.TOTEM_USE;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        super.useOn(context);
        var entity = context.getPlayer();
        if (entity == null) return InteractionResult.PASS;

        var world = context.getLevel();
        var pos = context.getClickedPos();
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        BlockState blockstate = context.getLevel().getBlockState(pos);
        ItemStack itemstack = context.getItemInHand();
        if (blockstate.getBlock() == Blocks.DEEPSLATE_BRICK_SLAB) {
            world.setBlock(BlockPos.containing(x, y, z), ModBlocks.BLOCK_CROWN.get().defaultBlockState(), 3);
            {
                Direction _dir = ((entity.getDirection()).getOpposite());
                BlockPos _pos = BlockPos.containing(x, y, z);
                BlockState _bs = world.getBlockState(_pos);
                Property<?> _property = _bs.getBlock().getStateDefinition().getProperty("facing");
                if (_property instanceof DirectionProperty _dp && _dp.getPossibleValues().contains(_dir)) {
                    world.setBlock(_pos, _bs.setValue(_dp, _dir), 3);
                } else {
                    _property = _bs.getBlock().getStateDefinition().getProperty("axis");
                    if (_property instanceof EnumProperty _ap && _ap.getPossibleValues().contains(_dir.getAxis()))
                        world.setBlock(_pos, _bs.setValue(_ap, _dir.getAxis()), 3);
                }
            }
            itemstack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }
}
