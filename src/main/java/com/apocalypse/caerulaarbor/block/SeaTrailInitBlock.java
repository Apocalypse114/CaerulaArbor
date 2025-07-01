
package com.apocalypse.caerulaarbor.block;

import com.apocalypse.caerulaarbor.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SeaTrailInitBlock extends SeaTrailBaseBlock {
	private static final VoxelShape SHAPE = box(2, 0, 2, 14, 1, 14);
	public SeaTrailInitBlock() {
		super(BlockBehaviour.Properties.of()
				.sound(SoundType.SCULK_VEIN)
				.strength(0.5f, 0f)
				.friction(0.5f)
				.speedFactor(0.8f)
				.noOcclusion()
				.pushReaction(PushReaction.DESTROY)
				.isRedstoneConductor((bs, br, bp) -> false)
				.dynamicShape()
				.offsetType(Block.OffsetType.XZ));
		this.registerDefaultState(this.stateDefinition.any().setValue(GROW_AGE, 0).setValue(WATERLOGGED, false));
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState blockstate, boolean clientSide) {
		return blockstate.getValue(GROW_AGE) < MAX_AGE;
	}

	@Override
	protected void onGrown(ServerLevel level, BlockPos pos, BlockState state) {
		boolean waterlogged = getFluidState(state).getType() == Fluids.WATER;
		level.setBlock(pos, ModBlocks.SEA_TRAIL_GROWING.get().defaultBlockState()
				.setValue(WATERLOGGED, waterlogged), 3);
		level.playSound(null, pos, SoundEvents.SCULK_VEIN_STEP, SoundSource.BLOCKS, 1, 1);
	}

	@Override
	public float getMaxHorizontalOffset() {
		return 0.125F;
	}

	@Override
	public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		Vec3 offset = state.getOffset(world, pos);
		return SHAPE.move(offset.x, .0D, offset.z);
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 3;
	}

	@Override
	public void onPlace(BlockState blockstate, Level world, BlockPos pos, BlockState oldState, boolean moving) {
		super.onPlace(blockstate, world, pos, oldState, moving);
		world.scheduleTick(pos, this, 20);
	}
}
