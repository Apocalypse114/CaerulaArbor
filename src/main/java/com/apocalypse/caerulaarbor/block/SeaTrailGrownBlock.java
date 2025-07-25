package com.apocalypse.caerulaarbor.block;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import com.apocalypse.caerulaarbor.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.AABB;

public class SeaTrailGrownBlock extends SeaTrailBaseBlock {
    private static final int MAX_LONGEVITY = 64;
    private static final IntegerProperty LONGEVITY = IntegerProperty.create("longevity", 0, MAX_LONGEVITY);

    public SeaTrailGrownBlock() {
        super(BlockBehaviour.Properties.of()
                .sound(SoundType.SCULK_VEIN)
                .strength(4f, 8f)
                .lightLevel(s -> 4)
                .requiresCorrectToolForDrops()
                .friction(0.4f)
                .speedFactor(0.7f)
                .jumpFactor(0.7f)
                .noOcclusion()
                .pushReaction(PushReaction.DESTROY)
                .isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.stateDefinition.any().setValue(GROW_AGE, 0).setValue(WATERLOGGED, false));
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState blockstate, boolean clientSide) {
        return blockstate.getValue(GROW_AGE) < MAX_AGE;
    }

    @Override
    protected void onGrown(ServerLevel level, BlockPos pos, BlockState state) {
    }

    @Override
    public void tick(BlockState blockstate, ServerLevel level, BlockPos pos, RandomSource random) {
        int longevity = blockstate.getValue(LONGEVITY);
        if (longevity < 64) {
            level.setBlock(pos, blockstate.setValue(LONGEVITY, longevity + 1), 2);
        }
        expandOrDie(level, pos, blockstate, random);
        super.tick(blockstate, level, pos, random);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(LONGEVITY, 0);
    }

    @Override
    public int getFlammability(BlockState state, BlockGetter world, BlockPos pos, Direction face) {
        return 1;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LONGEVITY);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        tryCauseSanityDamage(player);
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    12,
                    0.75, 0.75, 0.75,
                    0.1);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    private void tryCauseSanityDamage(Player player) {
        if (ModCapabilities.getPlayerVariables(player).player_oceanization >= 3) return;
        ModCapabilities.getSanityInjury(player).hurt(player.getRandom().nextInt(32, 96));
    }

    private static void expandOrDie(ServerLevel world, BlockPos pos, BlockState blockstate, RandomSource random) {
        if (blockstate.getValue(GROW_AGE) < MAX_AGE) return;
        if (random.nextFloat() >= 0.2F) return;
        if (blockstate.getValue(LONGEVITY) < MAX_LONGEVITY) {
            if (mayExpand(world, pos, blockstate)) {
                tryExpandToDirection(world, pos, Direction.Plane.HORIZONTAL.getRandomDirection(random));
            }
        } else {
            world.setBlock(pos, blockstate.getFluidState().createLegacyBlock(), 3);
            if (world.getBlockState(pos.below()).is(ModTags.Blocks.ERRODABLE)) {
                world.setBlock(pos.below(), ModBlocks.SEA_TRAIL_SOLID.get().defaultBlockState(), 3);
                world.playSound(null, pos, SoundEvents.SCULK_BLOCK_BREAK, SoundSource.BLOCKS, 1, 1);
            }
        }
    }

    private static boolean mayExpand(ServerLevel level, BlockPos pos, BlockState state) {
        return level.getEntitiesOfClass(LivingEntity.class,
                new AABB(pos).inflate(0.1),
                e -> e.getHealth() > 5).isEmpty();
    }

    private static void tryExpandToDirection(ServerLevel level, BlockPos originPos, Direction direction) {
        BlockPos posToPlace = originPos.relative(direction);
        if (tryExpand(level, posToPlace, null)) return;
        if (tryExpand(level, posToPlace.below(), posToPlace)) return;
        tryExpand(level, posToPlace.above(), originPos.above());
    }

    private static boolean tryExpand(ServerLevel level, BlockPos posToPlace, BlockPos posToCheck) {
        if (posToCheck != null && !level.getBlockState(posToCheck).isAir()) return false;
        if (!level.getBlockState(posToPlace).canBeReplaced()) return false;
        BlockState blockToPlace =
                ModBlocks.SEA_TRAIL_INIT.get().defaultBlockState()
                        .setValue(WATERLOGGED, level.getFluidState(posToPlace).getType() == Fluids.WATER);
        if (!blockToPlace.canSurvive(level, posToPlace)) return false;
        level.setBlock(posToPlace, blockToPlace, 3);
        level.levelEvent(2001, posToPlace, Block.getId(blockToPlace));
        return true;
    }

    @Override
    public void stepOn(Level pLevel, BlockPos pPos, BlockState pState, Entity pEntity) {
        if (!(pEntity instanceof LivingEntity livingEntity)) return;
        if (pLevel.isClientSide()) return;
        if (livingEntity.hasEffect(ModMobEffects.TRAIL_BUFF.get())) return;
        livingEntity.addEffect(new MobEffectInstance(ModMobEffects.TRAIL_BUFF.get(), 40, 0, false, false));
        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}
