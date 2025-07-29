
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.block.EmergencyLightBlock;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class RelicCurseEMELIGHTItem extends Item {
    public RelicCurseEMELIGHTItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.relic_curse_emelight.description_0"));
        list.add(Component.translatable("item.caerula_arbor.relic_curse_emelight.description_1"));
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        super.useOn(context);
        var world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        Direction direction = context.getClickedFace();
        Entity entity = context.getPlayer();
        ItemStack itemstack = context.getItemInHand();
        if (entity == null) return InteractionResult.PASS;

        BlockPos targetPos = pos.relative(direction);

        if (ModBlocks.EMERGENCY_LIGHT.get().defaultBlockState().canSurvive(world, targetPos)) {
            if (world.getBlockState(targetPos.above()).isFaceSturdy(world, targetPos.above(), Direction.DOWN)) {
                world.setBlock(targetPos, ModBlocks.EMERGENCY_LIGHT.get().defaultBlockState(), 3);
                world.setBlock(targetPos, world.getBlockState(targetPos).setValue(EmergencyLightBlock.BLOCKSTATE, 2), 3);
            } else {
                world.setBlock(targetPos, ModBlocks.EMERGENCY_LIGHT.get().defaultBlockState(), 3);
                world.setBlock(targetPos, world.getBlockState(targetPos).setValue(EmergencyLightBlock.BLOCKSTATE, 1), 3);
            }

            if (!world.isClientSide()) {
                world.playSound(null, BlockPos.containing(x, y, z), SoundEvents.LANTERN_PLACE, SoundSource.NEUTRAL, 1, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.LANTERN_PLACE, SoundSource.NEUTRAL, 1, 1, false);
            }

            if (entity instanceof Player player && !player.isCreative()) {
                itemstack.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(stack, world, entity, slot, selected);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        var cap = ModCapabilities.getPlayerVariables(entity);
        if (!Relic.CURSED_EMELIGHT.gained(cap)) {
            Relic.CURSED_EMELIGHT.set(cap, 1);
            cap.syncPlayerVariables(entity);

            if (world instanceof ServerLevel server) {
                world.playSound(null, BlockPos.containing(x, y, z), SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.get(), SoundSource.NEUTRAL, 2, 1);
                server.sendParticles(ParticleTypes.CRIMSON_SPORE, x, y, z, 99, 1, 1, 1, 1);
            } else {
                Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
                world.playLocalSound(x, y, z, SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.get(), SoundSource.NEUTRAL, 2, 1, false);
            }
        }
    }
}
