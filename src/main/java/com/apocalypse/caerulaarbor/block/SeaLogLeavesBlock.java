package com.apocalypse.caerulaarbor.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import javax.annotation.ParametersAreNonnullByDefault;

public class SeaLogLeavesBlock extends LeavesBlock {
    public SeaLogLeavesBlock() {
        super(BlockBehaviour.Properties.of()
                .mapColor(MapColor.COLOR_CYAN)
                .sound(SoundType.GRASS)
                .strength(0.2F)
                .noOcclusion()
                .isValidSpawn((state, world, pos, type) -> false)
                .isSuffocating((state, world, pos) -> false)
                .isViewBlocking((state, world, pos) -> false)
                .ignitedByLava());
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 30;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getFireSpreadSpeed(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 60;
    }
}