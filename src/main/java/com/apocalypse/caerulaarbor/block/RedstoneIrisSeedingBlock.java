
package com.apocalypse.caerulaarbor.block;

import com.apocalypse.caerulaarbor.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class RedstoneIrisSeedingBlock extends FlowerBlock implements BonemealableBlock {
    public RedstoneIrisSeedingBlock() {
        super(() -> MobEffects.REGENERATION, 80, BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).sound(SoundType.GRASS).instabreak().noCollission().offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY));
    }

    @Override
    public int getEffectDuration() {
        return 80;
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 100;
    }

    @Override
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 60;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean mayPlaceOn(BlockState groundState, BlockGetter worldIn, BlockPos pos) {
        return super.mayPlaceOn(groundState, worldIn, pos) || groundState.is(BlockTags.REDSTONE_ORES);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, LevelReader worldIn, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState groundState = worldIn.getBlockState(blockpos);
        return this.mayPlaceOn(groundState, worldIn, blockpos);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void randomTick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
        grow(world, pos);
    }

    private void grow(ServerLevel world, BlockPos pos) {
        if (Math.random() >= 0.05) return;
        world.setBlock(pos, ModBlocks.REDSTONE_IRIS.get().defaultBlockState(), 3);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isValidBonemealTarget(LevelReader world, BlockPos pos, BlockState blockstate, boolean clientSide) {
        return world.getBlockState(pos.below()).is(BlockTags.REDSTONE_ORES);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean isBonemealSuccess(Level world, RandomSource random, BlockPos pos, BlockState blockstate) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void performBonemeal(ServerLevel world, RandomSource random, BlockPos pos, BlockState blockstate) {
        grow(world, pos);
    }
}
