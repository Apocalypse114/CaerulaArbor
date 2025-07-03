package com.apocalypse.caerulaarbor.block;

import com.apocalypse.caerulaarbor.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class SeaTrailBaseBlock extends Block implements SimpleWaterloggedBlock, BonemealableBlock {
    private static final VoxelShape SHAPE = box(0, 0, 0, 16, 0.625, 16);
    protected static final int MAX_AGE = 30;
    protected static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final IntegerProperty GROW_AGE = IntegerProperty.create("grow_age", 0, MAX_AGE);

    public SeaTrailBaseBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public abstract boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState blockstate, boolean clientSide);

    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState blockstate) {
        return true;
    }

    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState blockstate) {
        this.grow(level, pos, blockstate, 8);
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        boolean flag = context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
        return super.getStateForPlacement(context).setValue(GROW_AGE, 0).setValue(WATERLOGGED, flag);
    }

    public boolean canSurvive(BlockState blockstate, LevelReader levelReader, BlockPos pos) {
        if (!blockstate.isFaceSturdy(levelReader, pos, Direction.UP)){
            return false;
        }
        if (levelReader.getBlockState(pos.below()).is(ModBlocks.SEA_TRAIL_SOLID.get())) {
            return false;
        }
        return super.canSurvive(blockstate, levelReader, pos);
    }

    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public BlockState updateShape(BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }

        return !state.canSurvive(world, currentPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(state, facing, facingState, world, currentPos, facingPos);
    }

    public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(blockstate, world, pos, oldState, moving);
        world.scheduleTick(pos, this, 20);
    }

    public void tick(BlockState blockstate, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(blockstate, level, pos, random);
        int growSpeed = level.isThundering() ? 2 : 1;
        this.grow(level, pos, blockstate, growSpeed);
        level.scheduleTick(pos, this, 20);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED, GROW_AGE);
    }

    protected abstract void onGrown(ServerLevel level, BlockPos pos, BlockState state);

    protected void grow(ServerLevel level, BlockPos pos, BlockState blockstate, int amount) {
        if (blockstate.getValue(GROW_AGE) == MAX_AGE) return;
        int newAge = Math.min(blockstate.getValue(GROW_AGE) + amount, MAX_AGE);
        level.setBlock(pos, blockstate.setValue(GROW_AGE, newAge), 2);
        if (newAge == MAX_AGE) onGrown(level, pos, blockstate);
    }
    public void onEntityDeathNearby(ServerLevel level, BlockPos pos, BlockState state) {
        grow(level, pos, state, 4);
    }
}
