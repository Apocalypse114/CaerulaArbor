
package com.apocalypse.caerulaarbor.block;

import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import com.apocalypse.caerulaarbor.procedures.OpenStraGUIProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class TideObservationBlock extends Block {
    public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 1);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public TideObservationBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.METAL)
                .strength(3f, 24f)
                .lightLevel(s -> s.getValue(BLOCKSTATE) == 1 ? 8 : 4)
                .pushReaction(PushReaction.BLOCK)
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
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
        builder.add(FACING, BLOCKSTATE);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite());
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

        if (MapVariables.get(world).strategySilence > 0) {
            world.setBlock(pos, blockstate.setValue(BLOCKSTATE, 1), 3);
        } else {
            world.setBlock(pos, blockstate.setValue(BLOCKSTATE, 0), 3);
        }
        world.scheduleTick(pos, this, 10);
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
        super.use(blockstate, world, pos, entity, hand, hit);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        OpenStraGUIProcedure.execute(world, x, y, z, entity);
        return InteractionResult.SUCCESS;
    }
}
