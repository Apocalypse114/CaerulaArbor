
package com.apocalypse.caerulaarbor.block;

import com.apocalypse.caerulaarbor.procedures.PokeSlightlyProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.FluidState;

public class TrailButtonBlock extends ButtonBlock {
	public TrailButtonBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.SCULK_CATALYST).strength(1f, 12f).dynamicShape(), BlockSetType.STONE, 20, false);
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState blockstate, Level world, BlockPos pos, Player entity, boolean willHarvest, FluidState fluid) {
		boolean retval = super.onDestroyedByPlayer(blockstate, world, pos, entity, willHarvest, fluid);
		PokeSlightlyProcedure.execute(world, pos.getX(), pos.getY(), pos.getZ(), entity);
		return retval;
	}
}
