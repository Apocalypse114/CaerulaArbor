
package com.apocalypse.caerulaarbor.block;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.procedures.DeductPlayerSanityProcedure;
import com.apocalypse.caerulaarbor.procedures.GiveTrailBuffProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.IPlantable;

import javax.annotation.ParametersAreNonnullByDefault;

public class SeaTrailSolidBlock extends Block {
	public SeaTrailSolidBlock() {
		super(BlockBehaviour.Properties.of().sound(SoundType.SCULK).strength(4f, 6f).lightLevel(s -> 1).friction(0.7f).speedFactor(0.7f).jumpFactor(0.7f));
	}

	@Override
	public float[] getBeaconColorMultiplier(BlockState state, LevelReader world, BlockPos pos, BlockPos beaconPos) {
		return new float[]{0.2509803922f, 0.3215686275f, 0.3921568627f};
	}

	@Override
	@ParametersAreNonnullByDefault
	public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
		return 8;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
		return 1;
	}

	@Override
	@ParametersAreNonnullByDefault
	public boolean canSustainPlant(BlockState state, BlockGetter world, BlockPos pos, Direction direction, IPlantable plantable) {
		return true;
	}

	@Override
	public boolean onDestroyedByPlayer(BlockState blockstate, Level world, BlockPos pos, Player entity, boolean willHarvest, FluidState fluid) {
		double x = pos.getX();
		double y = pos.getY();
		double z = pos.getZ();
		if (entity != null) {
			if ((ModCapabilities.getPlayerVariables(entity)).seabornization < 2.85) {
				DeductPlayerSanityProcedure.execute(entity, Mth.nextInt(RandomSource.create(), 32, 96));
			}
			if ((LevelAccessor) world instanceof ServerLevel _level)
				_level.sendParticles(ParticleTypes.ELECTRIC_SPARK, (x + 0.5), (y + 0.5), (z + 0.5), 16, 0.75, 0.75, 0.75, 0.1);
		}
		return super.onDestroyedByPlayer(blockstate, world, pos, entity, willHarvest, fluid);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void entityInside(BlockState blockstate, Level world, BlockPos pos, Entity entity) {
		GiveTrailBuffProcedure.execute(entity);
		super.entityInside(blockstate, world, pos, entity);
	}

	@Override
	@ParametersAreNonnullByDefault
	public void stepOn(Level world, BlockPos pos, BlockState blockstate, Entity entity) {
		GiveTrailBuffProcedure.execute(entity);
		super.stepOn(world, pos, blockstate, entity);
	}
}
