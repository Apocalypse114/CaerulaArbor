
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class RoyalFateItem extends Item {
    public RoyalFateItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean isFoil(@NotNull ItemStack itemstack) {
        return true;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.royal_fate.description_0"));
        list.add(Component.translatable("item.caerula_arbor.royal_fate.description_1"));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        ItemStack stack = ar.getObject();

        var cap = entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());
        if (cap.maxLive > 1) {
            if (!world.isClientSide()) {
                world.playSound(null, BlockPos.containing(x, y, z), SoundEvents.WARDEN_DEATH, SoundSource.NEUTRAL, 2, 1);
                ((ServerLevel) world).sendParticles(ParticleTypes.END_ROD, x, y, z, 72, 1, 1, 1, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.WARDEN_DEATH, SoundSource.NEUTRAL, 2, 1, false);
                Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
            }

            double lives_left = cap.maxLive;

            cap.maxLive = 1;
            cap.life = 1;
            cap.shield = (int) (cap.shield + lives_left + 3);

            stack.shrink(1);
        }
        Relic.SARKAZ_KING_RYLFATE.gain(cap);
        cap.syncPlayerVariables(entity);

        return ar;
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        super.useOn(context);
        LevelAccessor world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        BlockState blockstate = context.getLevel().getBlockState(pos);
        Entity entity = context.getPlayer();
        ItemStack itemstack = context.getItemInHand();

        if (entity == null) return InteractionResult.PASS;

        if (blockstate.getBlock() == Blocks.DEEPSLATE_BRICK_SLAB) {
            world.setBlock(pos, ModBlocks.BLOCK_FATE.get().defaultBlockState(), 3);
            BlockState _bs = world.getBlockState(pos);
            world.setBlock(pos, _bs.setValue(BlockStateProperties.FACING, entity.getDirection().getOpposite()), 3);

            itemstack.shrink(1);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }
}
