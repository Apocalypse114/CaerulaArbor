
package com.apocalypse.caerulaarbor.block;

import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.init.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class BlockKettleBlock extends Block {
    public static final IntegerProperty BLOCKSTATE = IntegerProperty.create("blockstate", 0, 1);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERED = BooleanProperty.create("watered");
    public static final BooleanProperty BOILING = BooleanProperty.create("boiling");
    public static final BooleanProperty NOODLED = BooleanProperty.create("noodled");

    public BlockKettleBlock() {
        super(BlockBehaviour.Properties.of().sound(SoundType.METAL).strength(0.5f, 2f).lightLevel(s -> 0).noOcclusion().isRedstoneConductor((bs, br, bp) -> false));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERED, false).setValue(BOILING, false).setValue(NOODLED, false));
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean propagatesSkylightDown(BlockState state, BlockGetter reader, BlockPos pos) {
        return true;
    }

    @Override
    @ParametersAreNonnullByDefault
    public int getLightBlock(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return 0;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getVisualShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.or(box(2.5, 3, 2.5, 13.5, 6, 13.5), box(3.5, 6, 3.5, 12.5, 9, 12.5), box(3.5, 2, 3.5, 12.5, 3, 12.5), box(4.5, 10, 4.5, 11.5, 12, 11.5), box(5.5, 12, 5.5, 10.5, 13, 10.5), box(7.5, 13, 7.5, 8.5, 14, 8.5),
                box(4.5, 1, 4.5, 11.5, 2, 11.5), box(3.5, 0, 3.5, 12.5, 1, 12.5), box(4.5, 9, 4.5, 11.5, 10, 11.5));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING, WATERED, BOILING, NOODLED, BLOCKSTATE);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(WATERED, false).setValue(BOILING, false).setValue(NOODLED, false);
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
        world.scheduleTick(pos, this, 5);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void neighborChanged(BlockState blockstate, Level world, BlockPos pos, Block neighborBlock, BlockPos fromPos, boolean moving) {
        super.neighborChanged(blockstate, world, pos, neighborBlock, fromPos, moving);
        setBoiling(world, pos);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void tick(BlockState blockstate, ServerLevel world, BlockPos pos, RandomSource random) {
        super.tick(blockstate, world, pos, random);

        setBoiling(world, pos);
        world.scheduleTick(pos, this, 5);
    }

    private void setBoiling(Level world, BlockPos pos) {
        boolean valid = false;

        var state = world.getBlockState(pos);
        var stateBelow = world.getBlockState(pos.below());

        if (stateBelow.is(BlockTags.create(new ResourceLocation("caerula_arbor:heat")))) {
            valid = true;
        } else {
            for (String source : GameplayConfig.KETTLE_HEAT_SOURCE.get()) {
                if (ForgeRegistries.BLOCKS.getKey(stateBelow.getBlock()).toString().equals(source)) {
                    valid = true;
                    break;
                }
            }
        }
        if (valid) {
            world.setBlock(pos, state.setValue(BLOCKSTATE, 1), 3);
            world.setBlock(pos, state.setValue(BOILING, true), 3);
        } else {
            world.setBlock(pos, state.setValue(BLOCKSTATE, 0), 3);
            world.setBlock(pos, state.setValue(BOILING, false), 3);
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResult use(BlockState blockstate, Level world, BlockPos pos, Player entity, InteractionHand hand, BlockHitResult hit) {
        super.use(blockstate, world, pos, entity, hand, hit);
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        var state = world.getBlockState(pos);

        if (entity.getMainHandItem().getItem() == ModItems.CANNED_WATER.get() && !blockstate.getValue(WATERED)) {
            world.setBlock(pos, state.setValue(WATERED, true), 3);

            entity.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.EMPTY_CAN.get(), entity.getMainHandItem().getCount()));
            entity.getInventory().setChanged();

            if (!world.isClientSide()) {
                world.playSound(null, pos, SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 1, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.BUCKET_FILL, SoundSource.NEUTRAL, 1, 1, false);
            }
            return InteractionResult.SUCCESS;
        }

        if (blockstate.getValue(WATERED) && blockstate.getValue(BOILING)) {
            if (entity.getMainHandItem().getItem() == ModItems.EMPTY_CAN.get()) {
                world.setBlock(pos, state.setValue(WATERED, false), 3);
                world.setBlock(pos, state.setValue(NOODLED, false), 3);

                entity.getMainHandItem().shrink(1);
                ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ModItems.CANNED_NOODLE.get()));

                return InteractionResult.SUCCESS;
            } else {
                if (entity.getMainHandItem().getItem() == ModItems.EMPTY_CAN.get()
                        // TODO 这条件对吗？
                        && !blockstate.getValue(NOODLED)
                ) {
                    world.setBlock(pos, state.setValue(WATERED, false), 3);

                    entity.getMainHandItem().shrink(1);
                    ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ModItems.CANNED_BOILED_WATER.get()));

                    return InteractionResult.SUCCESS;
                }
            }

            if (entity.getMainHandItem().getItem() == ModItems.INSTANT_NOODLE.get()) {
                world.setBlock(pos, state.setValue(NOODLED, true), 3);
                entity.getMainHandItem().shrink(1);

                if (!world.isClientSide()) {
                    world.playSound(null, pos, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.NEUTRAL, 2, 1);
                } else {
                    world.playLocalSound(x, y, z, SoundEvents.REDSTONE_TORCH_BURNOUT, SoundSource.NEUTRAL, 2, 1, false);
                }
                return InteractionResult.SUCCESS;
            }

            if (entity.getMainHandItem().getItem() == ModItems.EMPTY_CAN.get()) {
                entity.getMainHandItem().shrink(1);

                ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ModItems.CANNED_WATER.get()));

                world.setBlock(pos, state.setValue(WATERED, false), 3);

                if (!world.isClientSide()) {
                    world.playSound(null, pos, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1, 1);
                } else {
                    world.playLocalSound(x, y, z, SoundEvents.BOTTLE_FILL, SoundSource.NEUTRAL, 1, 1, false);
                }

                return InteractionResult.SUCCESS;
            }

            if (entity.getMainHandItem().getItem() == Blocks.SPONGE.asItem()) {
                entity.getMainHandItem().shrink(1);

                world.setBlock(pos, state.setValue(WATERED, false), 3);
                ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(Blocks.WET_SPONGE));

//				if (world instanceof Level world) {
//					if (!world.isClientSide()) {
//						world.playSound(null, pos, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("intentionally_empty")), SoundSource.NEUTRAL, 1, 1);
//					} else {
//						world.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("intentionally_empty")), SoundSource.NEUTRAL, 1, 1, false);
//					}
//				}
                return InteractionResult.SUCCESS;
            } else {
                if (entity.getMainHandItem().getItem() == ModItems.CANNED_LAVA.get()) {
                    entity.getMainHandItem().shrink(1);

                    world.setBlock(pos, state.setValue(WATERED, false), 3);

                    ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ModItems.OBSIDIAN_BALL.get()));
                    ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ModItems.EMPTY_CAN.get()));

                    if (!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.LAVA_EXTINGUISH, SoundSource.NEUTRAL, 1, 1);
                    } else {
                        world.playLocalSound(x, y, z, SoundEvents.LAVA_EXTINGUISH, SoundSource.NEUTRAL, 1, 1, false);
                    }

                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
