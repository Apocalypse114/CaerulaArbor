package com.apocalypse.caerulaarbor.block;

import com.apocalypse.caerulaarbor.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class AnchorMediumBlock extends Block {
    public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 1);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public AnchorMediumBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.NETHERITE_BLOCK)
                .strength(12f, 45f)
                .lightLevel(s -> 8)
                .pushReaction(PushReaction.BLOCK)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVATED, false));
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 15;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return box(0, 0, 0, 16, 16, 16);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, ACTIVATED, BLOCKSTATE);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(ACTIVATED, false);
    }

    public @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    public @NotNull BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
        super.onPlace(blockstate, world, pos, oldState, moving);
        world.scheduleTick(pos, this, 10);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
        super.tick(blockstate, world, pos, random);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        // TODO 优化伊莎玛拉现实稳定锚机制（到时候把机制内联一下）
//		DetectActivityProcedure.onAnvilUpdate(world, x, y, z, blockstate);
        world.scheduleTick(pos, this, 10);
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
        super.use(blockstate, world, pos, entity, hand, hit);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        Direction direction = hit.getDirection();
        InteractionResult result = InteractionResult.SUCCESS;
        boolean finished = false;

        if (entity.getMainHandItem().isEmpty()
                && entity.getOffhandItem().isEmpty()
                && direction == blockstate.getValue(BlockStateProperties.FACING)
        ) {
            if (blockstate.getValue(ACTIVATED)) {
                if (!world.isClientSide()) {
                    world.playSound(null, pos, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.NEUTRAL, 2, 1);
                } else {
                    world.playLocalSound(x, y, z, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.NEUTRAL, 2, 1, false);
                }
                world.setBlock(pos, blockstate.setValue(BLOCKSTATE, 0), 3);
                world.setBlock(pos, blockstate.setValue(ACTIVATED, false), 3);

                finished = true;
            } else if (world.getBlockState(pos.above()).getBlock() == ModBlocks.ANCHOR_UPPER.get()
                    && world.getBlockState(pos.above()).getValue(FACING) == blockstate.getValue(BlockStateProperties.FACING)
                    && world.getBlockState(pos.below()).getBlock() == ModBlocks.ANCHOR_LOWER.get()
                    && world.getBlockState(pos.below()).getValue(FACING) == blockstate.getValue(BlockStateProperties.FACING)
            ) {
                if (!world.isClientSide()) {
                    world.playSound(null, pos, SoundEvents.CONDUIT_ACTIVATE, SoundSource.NEUTRAL, 2, 1);
                } else {
                    world.playLocalSound(x, y, z, SoundEvents.CONDUIT_ACTIVATE, SoundSource.NEUTRAL, 2, 1, false);
                }

                world.setBlock(pos, blockstate.setValue(BLOCKSTATE, 1), 3);
                world.setBlock(pos, blockstate.setValue(ACTIVATED, true), 3);

                finished = true;
            } else {
                result = InteractionResult.FAIL;
                finished = true;
            }
        }
        if (!finished) {
            result = InteractionResult.PASS;
        }

        return result;
    }
}