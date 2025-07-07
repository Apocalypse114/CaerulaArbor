package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import com.apocalypse.caerulaarbor.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.AABB;

// TODO 优化伊莎玛拉现实稳定锚逻辑（改为以下实现）
/*
将世界分为64x32x64的块，每个块记录其中锚的位置
溟痕扩散时：
- 在附近27个块中查找与自身为中心49x17x19相交的块
- 获取所有相交块中的锚的位置，检查到任意一个在范围内则取消生长
*/
public class DetectActivityProcedure {
    public static void onAnvilUpdate(LevelAccessor world, double x, double y, double z, BlockState blockstate) {
        double attr;
        if (blockstate.getBlock().getStateDefinition().getProperty("activated") instanceof BooleanProperty _getbp1 && blockstate.getValue(_getbp1)) {
            if (!((world.getBlockState(BlockPos.containing(x, y + 1, z))).getBlock() == ModBlocks.ANCHOR_UPPER.get() && (new Object() {
                public Direction getDirection(BlockPos pos) {
                    BlockState _bs = world.getBlockState(pos);
                    Property<?> property = _bs.getBlock().getStateDefinition().getProperty("facing");
                    if (property != null && _bs.getValue(property) instanceof Direction _dir)
                        return _dir;
                    else if (_bs.hasProperty(BlockStateProperties.AXIS))
                        return Direction.fromAxisAndDirection(_bs.getValue(BlockStateProperties.AXIS), Direction.AxisDirection.POSITIVE);
                    else if (_bs.hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
                        return Direction.fromAxisAndDirection(_bs.getValue(BlockStateProperties.HORIZONTAL_AXIS), Direction.AxisDirection.POSITIVE);
                    return Direction.NORTH;
                }
            }.getDirection(BlockPos.containing(x, y + 1, z))) == (new Object() {
                public Direction getDirection(BlockState _bs) {
                    Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
                    if (_prop instanceof DirectionProperty _dp)
                        return _bs.getValue(_dp);
                    _prop = _bs.getBlock().getStateDefinition().getProperty("axis");
                    return _prop instanceof EnumProperty<?> _ep && _ep.getPossibleValues().toArray()[0] instanceof Direction.Axis ? Direction.fromAxisAndDirection((Direction.Axis) _bs.getValue(_ep), Direction.AxisDirection.POSITIVE) : Direction.NORTH;
                }
            }.getDirection(blockstate)) && (world.getBlockState(BlockPos.containing(x, y - 1, z))).getBlock() == ModBlocks.ANCHOR_LOWER.get() && (new Object() {
                public Direction getDirection(BlockPos pos) {
                    BlockState _bs = world.getBlockState(pos);
                    Property<?> property = _bs.getBlock().getStateDefinition().getProperty("facing");
                    if (property != null && _bs.getValue(property) instanceof Direction _dir)
                        return _dir;
                    else if (_bs.hasProperty(BlockStateProperties.AXIS))
                        return Direction.fromAxisAndDirection(_bs.getValue(BlockStateProperties.AXIS), Direction.AxisDirection.POSITIVE);
                    else if (_bs.hasProperty(BlockStateProperties.HORIZONTAL_AXIS))
                        return Direction.fromAxisAndDirection(_bs.getValue(BlockStateProperties.HORIZONTAL_AXIS), Direction.AxisDirection.POSITIVE);
                    return Direction.NORTH;
                }
            }.getDirection(BlockPos.containing(x, y - 1, z))) == (new Object() {
                public Direction getDirection(BlockState _bs) {
                    Property<?> _prop = _bs.getBlock().getStateDefinition().getProperty("facing");
                    if (_prop instanceof DirectionProperty _dp)
                        return _bs.getValue(_dp);
                    _prop = _bs.getBlock().getStateDefinition().getProperty("axis");
                    return _prop instanceof EnumProperty<?> _ep && _ep.getPossibleValues().toArray()[0] instanceof Direction.Axis ? Direction.fromAxisAndDirection((Direction.Axis) _bs.getValue(_ep), Direction.AxisDirection.POSITIVE) : Direction.NORTH;
                }
            }.getDirection(blockstate)))) {
                {
                    int _value = 0;
                    BlockPos _pos = BlockPos.containing(x, y, z);
                    BlockState _bs = world.getBlockState(_pos);
                    if (_bs.getBlock().getStateDefinition().getProperty("blockstate") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
                        world.setBlock(_pos, _bs.setValue(_integerProp, _value), 3);
                }
                {
                    BlockPos _pos = BlockPos.containing(x, y, z);
                    BlockState _bs = world.getBlockState(_pos);
                    if (_bs.getBlock().getStateDefinition().getProperty("activated") instanceof BooleanProperty _booleanProp)
                        world.setBlock(_pos, _bs.setValue(_booleanProp, false), 3);
                }
                if (world instanceof Level _level) {
                    if (!_level.isClientSide()) {
                        _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.CONDUIT_DEACTIVATE, SoundSource.NEUTRAL, 2, 1);
                    } else {
                        _level.playLocalSound(x, y, z, SoundEvents.CONDUIT_DEACTIVATE, SoundSource.NEUTRAL, 2, 1, false);
                    }
                }
            }
            for (int index0 = 0; index0 < 16; index0++) {
                if (world instanceof ServerLevel _level) {
                    _level.sendParticles(ParticleTypes.NAUTILUS, (x + Mth.nextDouble(RandomSource.create(), -24, 25)), y, (z - 24), 4, 0.5, 4, 0.5, 0.1);
                    _level.sendParticles(ParticleTypes.NAUTILUS, (x + Mth.nextDouble(RandomSource.create(), -24, 25)), y, (z + 25), 4, 0.5, 4, 0.5, 0.1);
                    _level.sendParticles(ParticleTypes.NAUTILUS, (x - 24), y, (z + Mth.nextDouble(RandomSource.create(), -24, 25)), 4, 0.5, 4, 0.5, 0.1);
                    _level.sendParticles(ParticleTypes.NAUTILUS, (x + 25), y, (z + Mth.nextDouble(RandomSource.create(), -24, 25)), 4, 0.5, 4, 0.5, 0.1);
                }
            }

            BlockPos anchorPos = BlockPos.containing(x, y, z);
            for (BlockPos blockPos : BlockPos.betweenClosed(
                    anchorPos.offset(-24, -8, -24),
                    anchorPos.offset(24, 8, 24))) {
                if (world.getBlockState(blockPos).is(ModTags.Blocks.SEA_TRAIL)) {
                    attr = (world.getBlockState(blockPos)).getBlock().getStateDefinition().getProperty("grow_age") instanceof IntegerProperty _getip28
                            ? (world.getBlockState(blockPos)).getValue(_getip28)
                            : -1;
                    if (attr < 64) {
                        {
                            int _value = (int) (attr - 4);
                            BlockState _bs = world.getBlockState(blockPos);
                            if (_bs.getBlock().getStateDefinition().getProperty("grow_age") instanceof IntegerProperty _integerProp && _integerProp.getPossibleValues().contains(_value))
                                world.setBlock(blockPos, _bs.setValue(_integerProp, _value), 3);
                        }
                    }
                }
            }
            if (!world.isClientSide()) {
                for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, new AABB((x + 25), (y + 9), (z + 25), (x - 24), (y - 9), (z - 24)))) {
                    if (!entity.hasEffect(ModMobEffects.POWER_OF_ANCHOR.get())) {
                        entity.addEffect(new MobEffectInstance(ModMobEffects.POWER_OF_ANCHOR.get(), 80, 0, false, false));
                    }
                }
            }
        }
    }
}
